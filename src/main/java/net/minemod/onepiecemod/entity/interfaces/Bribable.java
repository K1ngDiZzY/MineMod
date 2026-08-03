package net.minemod.onepiecemod.entity.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minemod.onepiecemod.item.ModItems;

import java.nio.file.Path;

/**
 * This Interface handles the Bribe logic that will be implementable by any NPC.
 * - Angry NPCs can be bribed to make them passive, Guard NPCs can be bribed to gain access, etc.
 */
public interface Bribable {

    /** Different Enum states to determine Bribe Behavior */
    enum BribeState {
        CAN_BRIBE,
        STRIKE_ONE,
        STRIKE_TWO,
        FAILED_PERMANENT
    }

    /**
     * Master tag to determine if this entity is currently bribable.
     * - Can be turned on or off based on external conditions
     * - (Example: If the player is "Wanted" then a NavyCaptainNPC's "isBribable" tag would be "false.)
     */
    default boolean isBribable(Player player) {
        return getBribeState(player) != BribeState.FAILED_PERMANENT;
    }

    /** Returns current Bribe state for a specific player. */
    BribeState getBribeState(Player player);

    /** Sets the Bribe state on the entity implementation. */
    void setBribeState(BribeState state);

    /** Item required to Bribe this entity. (Default is a Gold Ingot - Similar to a Piglin) */
    default Item getBribeItem() {
        return Items.GOLD_INGOT;
    }

    /** Amount of the Bribe Item required per Bribe attempt. (Default is 1) */
    default int getBribeCost() {
        return 1;
    }

    /** Chance that a Bribe Attempt will succeed between 0.0 (0%) and 1.0 (100%). (Default is 1.0) */
    default float getBribeChance() {
        return 1.0f;
    }

    default void onBribeSuccess(Player player, PathfinderMob mob) {
        // Reset strike count back to default state upon successful bribe
        setBribeState(BribeState.CAN_BRIBE);

        // 1. Pacification logic for Neutral NPCs
        if (mob instanceof NeutralMob neutralMob && mob.level() instanceof ServerLevel serverLevel) {
            neutralMob.stopBeingAngry();
            mob.setTarget(null);
            mob.setLastHurtByMob(null);
            mob.getNavigation().stop();

            // Force active attack/navigation tasks to instantly drop the player
            mob.targetSelector.getAvailableGoals().forEach(goal -> {
                if (goal.isRunning()) goal.stop();
            });
            mob.goalSelector.getAvailableGoals().forEach(goal -> {
                if (goal.isRunning()) goal.stop();
            });

            // 2. Visual Effects (Happy Villager particles)
            serverLevel.sendParticles(
                    ParticleTypes.HAPPY_VILLAGER,
                    mob.getX(), mob.getY() + mob.getEyeHeight() + 0.5D, mob.getZ(),
                    5,               // count
                    0.2, 0.2, 0.2,   // offset spread
                    0.02             // speed
            );

            // 3. Audio Effect (Villager No / Refusal grunt)
            serverLevel.playSound(
                    null, mob.blockPosition(),
                    SoundEvents.VILLAGER_YES,
                    SoundSource.NEUTRAL,
                    1.0F, 1.0F
            );

            // 4. Chat Message (System message in chat or action bar)
            player.displayClientMessage(
                    Component.translatable("Bribe Succeeded!", mob.getDisplayName())
                            .withStyle(ChatFormatting.GREEN),
                    true // true = Action Bar
            );
        }
    }

    default void onBribeFailed(Player player, PathfinderMob mob) {
        if (mob.level() instanceof ServerLevel serverLevel) {

            // Calculate next strike state
            BribeState currentState = getBribeState(player);
            BribeState nextState;

            switch (currentState) {
                case CAN_BRIBE -> nextState = BribeState.STRIKE_ONE;
                case STRIKE_ONE -> nextState = BribeState.STRIKE_TWO;
                default -> nextState = BribeState.FAILED_PERMANENT;
            }
            setBribeState(nextState);

            if (mob instanceof NeutralMob neutralMob) {
                neutralMob.startPersistentAngerTimer();
                neutralMob.setPersistentAngerTarget(player.getUUID());
                mob.setTarget(player);
            }

            // 2. Visual Effects (Angry Villager Thunder Cloud particles)
            serverLevel.sendParticles(
                    ParticleTypes.ANGRY_VILLAGER,
                    mob.getX(), mob.getY() + mob.getEyeHeight() + 0.5D, mob.getZ(),
                    5,               // count
                    0.2, 0.2, 0.2,   // offset spread
                    0.02             // speed
            );

            // 3. Audio Effect (Villager No / Refusal grunt)
            serverLevel.playSound(
                    null, mob.blockPosition(),
                    SoundEvents.VILLAGER_NO,
                    SoundSource.NEUTRAL,
                    1.0F, 1.0F
            );

            // 4. Chat Message (System message in chat or action bar)
            Component message = switch (nextState) {
                case STRIKE_ONE -> Component.translatable("Bribe Failed! (Strike 1)", mob.getDisplayName())
                        .withStyle(ChatFormatting.YELLOW);
                case STRIKE_TWO -> Component.translatable("Bribe Failed! Be careful... (Strike 2)", mob.getDisplayName())
                        .withStyle(ChatFormatting.GOLD);
                default -> Component.translatable("This NPC refuses to be bribed anymore!", mob.getDisplayName())
                        .withStyle(ChatFormatting.RED);
            };

            player.displayClientMessage(message, true);
        }
    }

    default InteractionResult processBribe(PathfinderMob mob, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // 1. Check if player is holding the required bribe item and stack size
        if (!heldItem.is(getBribeItem()) || heldItem.getCount() < getBribeCost()) {
            return InteractionResult.PASS;
        }

        // 2. Check if the NPC refuses bribes from this player (FAILED_PERMANENT or custom condition)
        if (!isBribable(player)) {
            if (!mob.level().isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("This NPC refuses to take your bribes!!", mob.getDisplayName())
                                .withStyle(ChatFormatting.RED),
                        true
                );
            }
            return InteractionResult.FAIL;
        }

        // 3. Process the bribe logic
        if (!mob.level().isClientSide()) {
            // Consume item unless in Creative
            if (!player.getAbilities().instabuild) {
                heldItem.shrink(getBribeCost());
            }

            // Roll probability check
            if (mob.getRandom().nextFloat() <= getBribeChance()) {
                onBribeSuccess(player, mob);
            } else {
                onBribeFailed(player, mob);
            }
        }

        return InteractionResult.SUCCESS;
    }
}