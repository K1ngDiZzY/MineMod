package net.minemod.onepiecemod.entity.npcs.pirate;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;

import java.util.UUID;

public class AbstractPirateNPC extends PathfinderMob implements NeutralMob {

    /** The remaining time (in ticks) that this NPC will stay angry at its target. */
    private int remainingPersistentAngerTime;

    /** The UUID of the player or entity that this NPC is currently angry at. */
    private UUID persistentAngerTarget;

    /** Defines the range of time (20 to 39 seconds) that anger will persist when triggered. */
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20,39);

    public AbstractPirateNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * registerGoals()
     * This method is where we can select Goals from the minecraft.world.entity.ai.goal library.
     * (Eventually we can create custom Goals that pertain to our mod, such as sink goals, targetNavy/Pirate goals, etc.)
     */
    @Override
    protected void registerGoals() {
        // Combat behavior
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.2D, true)); // Attacks target when in range

        // Movement and idle behavior
        this.goalSelector.addGoal(1, new FloatGoal(this)); // Allows swimming
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Wanders around
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Looks at nearby players
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Idle head movement

        // Targeting logic
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers()); // Retaliates when attacked

        // Makes PirateNPCs attack NavyNPCs in its vicinity (if "persistentAngerTarget" isn't another entity)
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, NavyNPC.class, 10, true, false, null));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));

    }
    
    /**
     * Custom behavior would go here. This would be inherited by any NPC that extends this class.
     * -
     * isPushable()
     * Abstract NPC will move when nudged by the Player.
     */
    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public int getRemainingPersistentAngerTime() {return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = pRemainingPersistentAngerTime;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pPersistentAngerTarget) {
        this.persistentAngerTarget = pPersistentAngerTarget;
    }

    /**
     * Starts the persistent anger timer by picking a random duration within the defined range.
     */
    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    /**
     * Handles server-side AI steps. Updates the persistent anger timers for the NeutralMob mechanics.
     */
    @Override
    protected void customServerAiStep(ServerLevel pLevel) {
        // Access pLevel directly
        this.updatePersistentAnger(pLevel, true);

        // Pass parameter to super method
        super.customServerAiStep(pLevel);
    }

    /**
     * Handles player interactions. Allows an aggressive PirateNPC to be "bribed" with a Diamond,
     * resetting its anger and clearing its current targeting priorities.
     * - Eventually "bribe" mechanic will be abstracted to AbstractNPC.
     */
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        // Ensure execution happens strictly on the server side to manipulate AI goals safely
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {

            // If the NPC is hostile to this specific player and the player holds a Diamond
            if (this.isAngryAt(player, serverLevel) && itemStack.is(Items.DIAMOND)) {

                // Consume 1 diamond unless the player is in Creative Mode
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
