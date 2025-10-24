package net.minemod.onepiecemod.entity.npcs.pirate;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;

import java.util.Objects;

public class AbstractPirateNPC extends PathfinderMob {

    private boolean playerAggroEnabled = false;

    public AbstractPirateNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    //TODO: Register new goals

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
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, NavyNPC.class, 10, true, false, null));
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
    protected void actuallyHurt(ServerLevel level, DamageSource source, float amount) {
        if (!playerAggroEnabled && source.getEntity() instanceof Player) {
            this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
            playerAggroEnabled = true;
        }
        super.actuallyHurt(level, source, amount);
    }



}
