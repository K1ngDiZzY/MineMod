package net.minemod.onepiecemod.entity.npcs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * This Class is the Parent Class to all NPCs.
 * - Any global behavior should go here. Implements "PathfinderMob" to allow pathfinding for NPCs.
 * - (Example: Devil Fruit checks, Inventory declaration, Trade/Bribe/Pickpocket mechanics, etc.)
 */
public abstract class AbstractNPC extends PathfinderMob {

    /** Constructor */
    public AbstractNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all NPCs.
     * (Example: Devil Fruit behavior, NPC head-movement, etc.)
     */
    @Override
    protected void registerGoals() {
        // Movement and idle behavior
        // If Devil Fruit, disable FloatGoal (upcoming Feature)
        this.goalSelector.addGoal(1, new FloatGoal(this)); // Allows swimming

        // If Devil Fruit, enable WaterAvoidingRandomStrollGoal (upcoming Feature)
        //this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D)); // Wanders around and avoids water

        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F)); // Looks at nearby players
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this)); // Idle head movement
    }

    /**
     * isPushable()
     * - Abstract NPC will move when nudged by the Player.
     */
    @Override
    public boolean isPushable() {
        return true;
    }
}
