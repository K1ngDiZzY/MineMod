package net.minemod.examplemod.entity.grunt;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractGrunt extends Monster {

    protected AbstractGrunt(EntityType<? extends AbstractGrunt> entityType, Level level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
    }

    /**
     * Default grunt attributes.
     * Each subclass can override or extend these.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)      // base grunt health
                .add(Attributes.MOVEMENT_SPEED, 0.35D) // base grunt speed
                .add(Attributes.ATTACK_DAMAGE, 4.0D);  // base grunt damage
    }

    // ==========================================================
    // Placeholder Methods — implement later as needed
    // ==========================================================

    /**
     * Future: Hook for grunt trading logic (like villagers).
     * Subclasses can override to open custom trading screen.
     */
    public void openTradingScreen() {
        // TODO: Implement trading logic in future (villager-like).
    }

    /**
     * Future: Mob mentality (piglin-like group aggression).
     * Call this from subclasses when they are attacked.
     */
    public void alertAlliesOnAttacked() {
        // TODO: Implement ally aggression in future.
    }

    /**
     * Future: Hook for playing custom Blockbench animations.
     * Subclasses can trigger animations from here.
     */
    public void playCustomAnimation(String animationName) {
        // TODO: Integrate with client animation system later.
    }

    //TODO: Add Custom Goals here
    @Override
    protected void registerGoals() {
        // (if player has devil fruit, sing has priority 0)
        this.goalSelector.addGoal(1, new FloatGoal(this)); // Allows swimming

        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0)); // When hit
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D)); // Wandering

        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Looks at players
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this)); // Idle head movement
    }
    //TODO: Add Custom Sounds here
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.VILLAGER_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }
    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ZOMBIE_VILLAGER_STEP, 0.15F, 1.0F);
    }

}
