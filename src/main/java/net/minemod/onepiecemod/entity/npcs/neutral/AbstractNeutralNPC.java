package net.minemod.onepiecemod.entity.npcs.neutral;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.AbstractNPC;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

/**
 * This Class is the Parent Class to any NPC Group that will be Neutral by default.
 * - Any neutral-specific behavior will go here.
 * - (Example: Passive by default but retaliate when attacked, etc.)
 */
public abstract class AbstractNeutralNPC extends AbstractNPC implements NeutralMob {

    /** Variables */
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20,39);

    /** Constructor */
    public AbstractNeutralNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all NeutralNPCs.
     * - (Example: Randomly walking, Will attack when they have a target, etc.)
     */
    @Override
    protected void registerGoals() {
        // Pull shared goals from AbstractNPC
        super.registerGoals();

        // Movement and idle behavior
        this.goalSelector.addGoal(5, new RandomStrollGoal(this,1.0D));

        // Combat behavior
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.2D, true)); // Attacks target when in range

    }

    /**
     * "Persistent Anger" methods
     * - These methods handle the "Neutral" behavior targeting, where NPCs will only be hostile if attacked.
     * - getRemainingPersistentAngerTime()  @return remainingPersistentAngerTime
     * - setRemainingPersistentAngerTime()
     * - getPersistentAngerTarget()         @return pPersistentAngerTarget
     * - setPersistentAngerTarget()
     * - startPersistentAngerTimer()
     */
    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int pRemainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = pRemainingPersistentAngerTime;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
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
     * customServerAiStep()
     * - Handles server-side AI steps. Updates the persistent anger timers for the NeutralMob mechanics.
     */
    @Override
    protected void customServerAiStep(ServerLevel pLevel) {
        // Access pLevel directly
        this.updatePersistentAnger(pLevel, true);

        // Pass parameter to super method
        super.customServerAiStep(pLevel);
    }
}
