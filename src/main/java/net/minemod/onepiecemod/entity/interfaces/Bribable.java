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
        FAILED_PERMANENT,
        COOLDOWN
    }

    /**
     * Master tag to determine if this entity is currently bribable.
     * - Can be turned on or off based on external conditions
     * - (Example: If the player is "Wanted" then a NavyCaptainNPC's "isBribable" tag would be "false.)
     */
    default boolean isBribable(Player player) {
        return getBribeState(player) == BribeState.CAN_BRIBE;
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
            // 1. Anger logic (Set "isBribable()" to false)
            setBribeState(BribeState.FAILED_PERMANENT);

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
            player.displayClientMessage(
                    Component.translatable("Bribe Failed", mob.getDisplayName())
                            .withStyle(ChatFormatting.RED),
                    true // true = Action Bar
            );
        }
    }

    default InteractionResult processBribe(PathfinderMob mob, Player player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);

        // Only proceed if holding the correct bribe item and sufficient quantity
        if (!heldItem.is(getBribeItem()) || heldItem.getCount() < getBribeCost()) {
            return InteractionResult.PASS;
        }

        BribeState state = getBribeState(player);

        switch (state) {
            case CAN_BRIBE -> {
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
            case FAILED_PERMANENT -> {
                if (!mob.level().isClientSide()) {
                    player.displayClientMessage(
                            Component.translatable("Won't Trade!", mob.getDisplayName())
                                    .withStyle(ChatFormatting.RED),
                            true
                    );
                }
                return InteractionResult.FAIL;
            }
            case COOLDOWN -> {
                if (!mob.level().isClientSide()) {
                    player.displayClientMessage(
                            Component.translatable("Maybe Later..", mob.getDisplayName())
                                    .withStyle(ChatFormatting.YELLOW),
                            true
                    );
                }
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.PASS;
    }
}