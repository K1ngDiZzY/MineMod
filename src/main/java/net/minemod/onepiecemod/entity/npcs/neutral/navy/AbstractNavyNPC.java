package net.minemod.onepiecemod.entity.npcs.neutral.navy;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.neutral.AbstractNeutralNPC;
import net.minemod.onepiecemod.entity.npcs.neutral.pirate.PirateNPC;

public abstract class AbstractNavyNPC extends AbstractNeutralNPC {

    /** Constructor */
    public AbstractNavyNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all Navy NPCs.
     * - (Example: Attack PirateNPCs on sight, Bribable with Emeralds, Arrest PirateNPCs, etc.)
     */
    @Override
    protected void registerGoals() {
        // Pull shared goals from AbstractNPC
        super.registerGoals();

        // Targeting logic
        // AlertOthers makes nearby NavyNPCs angry at whatever hit this mob.
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers()); // Retaliates when attacked

        // Makes NavyNPCs attack PirateNPCs in its vicinity (if "persistentAngerTarget" isn't another entity)
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PirateNPC.class, 10, true, false, null));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    /**
     * mobInteract()
     * - Handles player interactions. Allows an aggressive NavyNPC to be "bribed" with an Emerald,
     * - resetting its anger and clearing its current targeting priorities.
     * - (Eventually "bribe" mechanic will be abstracted to AbstractNeutralNPC)
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Ensure execution happens strictly on the server side to manipulate AI goals safely
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {

            // If the NPC is hostile to this specific player and the player holds an Emerald
            if (this.isAngryAt(player, serverLevel) && itemStack.is(Items.EMERALD)) {

                // Consume 1 emerald unless the player is in Creative Mode
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                // 1. Reset standard NeutralMob anger state
                this.stopBeingAngry();

                // 2. Clear instant combat memories and combat targets
                this.setTarget(null);
                this.lastHurtByPlayer = null;
                this.setLastHurtByMob(null);

                // 3. Force active attack/navigation tasks to instantly drop the player
                this.targetSelector.getAvailableGoals().forEach(wrappedGoal -> {
                    if (wrappedGoal.isRunning()) {
                        wrappedGoal.stop();
                    }
                });
                this.goalSelector.getAvailableGoals().forEach(wrappedGoal -> {
                    if (wrappedGoal.isRunning()) {
                        wrappedGoal.stop();
                    }
                });

                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

}
