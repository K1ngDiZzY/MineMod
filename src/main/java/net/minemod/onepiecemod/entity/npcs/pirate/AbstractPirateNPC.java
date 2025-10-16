package net.minemod.onepiecemod.entity.npcs.pirate;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class AbstractPirateNPC extends PathfinderMob {

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
        // (if player has devil fruit, sing has priority 0)
        this.goalSelector.addGoal(1, new FloatGoal(this)); // Allows swimming

        this.goalSelector.addGoal(2, new PanicGoal(this, 2.0)); // When hit
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D)); // Wandering

        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Looks at players
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this)); // Idle head movement
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
}
