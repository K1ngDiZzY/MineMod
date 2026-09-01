package net.minemod.onepiecemod.entity.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minemod.onepiecemod.client.gui.PickpocketSkillCheckScreen;
import net.minemod.onepiecemod.entity.npcs.AbstractNPC;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public interface Pickpocketable {

    /** States determining Pickpocket behavior and strike count */
    enum PickpocketState {
        CAN_PICKPOCKET,
        STRIKE_ONE,
        STRIKE_TWO,
        FAILED_PERMANENT
    }

    /** Master check determining if an entity can currently be pickpocketed by a player. */
    default boolean isPickpocketable(Player player) {
        return getPickpocketState(player) != PickpocketState.FAILED_PERMANENT;
    }

    /** Returns current Pickpocket state for a specific player. */
    PickpocketState getPickpocketState(Player player);

    /** Sets the Pickpocket state on the entity implementation. */
    void setPickpocketState(PickpocketState state);

    /** TODO: PICKPOCKET CHANCE TEMPORARILY SET TO 100% */
    /** Base success chance between 0.0 (0%) and 1.0 (100%). Default is 1.0 (100%). */
    default float getPickpocketChance() {
        return 1.0f;
    }

    boolean requiresSkillCheck();

    /** Determines the Timeout Period of the Skill Check screen. Default is 5 seconds (1 second   */
    default int getSkillCheckMaxTicks() { return 100; }

    /** Determines the total number of key presses required for the Skill Check */
    default int getSkillCheckKeyCount(Player player) {
        return 4;
    }

    default InteractionResult processPickpocket(PathfinderMob mob, Player player, InteractionHand hand) {
        // Check state before proceeding
        if (!isPickpocketable(player)) {
            if (!mob.level().isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("%s is watching you closely and cannot be pickpocketed!", mob.getDisplayName())
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
            return InteractionResult.FAIL;
        }

        // If the NPC does not require a SKil
        if(!this.requiresSkillCheck()){
           onPickpocketSuccess(player, mob);
           return InteractionResult.PASS;
        }

        if(mob.level().isClientSide()){
            int keyCount = getSkillCheckKeyCount(player);
            int maxTicks = getSkillCheckMaxTicks();
            List<SkillCheckKey> sequence = generateSkillCheckSequence(mob.getRandom(), keyCount);

            // Extract GKFW keycodes and labels for the screen
            List<Integer> keyCodes = sequence.stream().map(SkillCheckKey::keyCode).toList();
            List<String> labels = sequence.stream().map(SkillCheckKey::label).toList();


            net.minecraft.client.Minecraft.getInstance().setScreen(
                    new PickpocketSkillCheckScreen(mob, keyCodes, labels, maxTicks)
            );
        }

        return InteractionResult.SUCCESS;
    }

    /** Helper Struct to hold GLFW Keycode and matching UI label. */
    record SkillCheckKey(int keyCode, String label) {}

    /** Pool of possible keys used for the Skill Check sequence. */
    List<SkillCheckKey> SKILL_CHECK_KEY_POOL = List.of(
            new SkillCheckKey(GLFW.GLFW_KEY_W, "W"),
            new SkillCheckKey(GLFW.GLFW_KEY_A, "A"),
            new SkillCheckKey(GLFW.GLFW_KEY_S, "S"),
            new SkillCheckKey(GLFW.GLFW_KEY_D, "D")
    );

    default List<SkillCheckKey> generateSkillCheckSequence(RandomSource random, int count) {
        List<SkillCheckKey> sequence = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // Picking a random element allows repeats (e.g., W, A, W, S or D, D, S, A)
            sequence.add(SKILL_CHECK_KEY_POOL.get(random.nextInt(SKILL_CHECK_KEY_POOL.size())));
        }
        return sequence;
    }
    /**
     * Opens the NPC's inventory UI for the interacting player.
     */
    default void openPickpocketMenu(Player player, PathfinderMob mob) {
        if (mob instanceof AbstractNPC abstractNPC) {
            int totalSlots = abstractNPC.getInventorySize();
            int rows = Math.max(1, Math.min(6, totalSlots / 9)); // Calculate rows based on size (1 to 6)

            // Select the matching vanilla MenuType for the row count
            MenuType<?> menuType = switch (rows) {
                case 1 -> MenuType.GENERIC_9x1;
                case 2 -> MenuType.GENERIC_9x2;
                case 4 -> MenuType.GENERIC_9x4;
                case 5 -> MenuType.GENERIC_9x5;
                case 6 -> MenuType.GENERIC_9x6;
                default -> MenuType.GENERIC_9x3; // Default 3 rows (27 slots)
            };

            MenuProvider menuProvider = new SimpleMenuProvider(
                    (containerId, playerInventory, p) ->
                            new ChestMenu(menuType, containerId, playerInventory, abstractNPC.getInventory(), rows),
                    Component.translatable("%s's Pockets", mob.getDisplayName())
            );

            // Opens the container screen on the client
            player.openMenu(menuProvider);
        }
    }

    default void onPickpocketSuccess(Player player, PathfinderMob mob) {
        // Reset strike state on successful pickpocket
        setPickpocketState(PickpocketState.CAN_PICKPOCKET);

        if (mob.level() instanceof ServerLevel serverLevel) {
            // 1. Allow player to open NPCs inventory
            openPickpocketMenu(player, mob);

            // 2. Visual Effects (Witch/Stealth particles)
            serverLevel.sendParticles(
                    ParticleTypes.WITCH,
                    mob.getX(), mob.getY() + mob.getEyeHeight(), mob.getZ(),
                    8,
                    0.2, 0.2, 0.2,
                    0.02
            );

            // 3. Audio Effect (Quiet item pickup sound)
            serverLevel.playSound(
                    null, mob.blockPosition(),
                    SoundEvents.ITEM_PICKUP,
                    SoundSource.PLAYERS,
                    0.5F, 1.2F
            );
        }
    }

    default void onPickpocketFailed(Player player, PathfinderMob mob) {
        if (mob.level() instanceof ServerLevel serverLevel) {

            // 1. Advance strike state
            PickpocketState currentState = getPickpocketState(player);
            PickpocketState nextState = switch (currentState) {
                case CAN_PICKPOCKET -> PickpocketState.STRIKE_ONE;
                case STRIKE_ONE -> PickpocketState.STRIKE_TWO;
                default -> PickpocketState.FAILED_PERMANENT;
            };
            setPickpocketState(nextState);

            // 2. Make Neutral Mob hostile toward the thief
            if (mob instanceof NeutralMob neutralMob) {
                neutralMob.startPersistentAngerTimer();
                neutralMob.setPersistentAngerTarget(player.getUUID());
                mob.setTarget(player);
            }

            // 3. Visual Effects (Angry Villager particles)
            serverLevel.sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    mob.getX(), mob.getY() + mob.getEyeHeight() + 0.5D, mob.getZ(),
                    5,
                    0.2, 0.2, 0.2,
                    0.02
            );

            // 4. Audio Effect (Villager No sound)
            serverLevel.playSound(
                    null, mob.blockPosition(),
                    SoundEvents.VILLAGER_NO,
                    SoundSource.NEUTRAL,
                    1.0F, 1.0F
            );

            // 5. Action Bar Warning Message
            Component message = switch (nextState) {
                case STRIKE_ONE -> Component.translatable("You were caught pickpocketing! (Strike 1)", mob.getDisplayName())
                        .withStyle(ChatFormatting.YELLOW);
                case STRIKE_TWO -> Component.translatable("You were caught again! Stop pressing your luck! (Strike 2)", mob.getDisplayName())
                        .withStyle(ChatFormatting.GOLD);
                default -> Component.translatable("%s is now on high alert and cannot be pickpocketed!", mob.getDisplayName())
                        .withStyle(ChatFormatting.RED);
            };

            player.displayClientMessage(message, true);
        }
    }

}
