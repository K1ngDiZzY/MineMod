package net.minemod.onepiecemod.entity.npcs.passive;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.AbstractNPC;


/**
 * This Class is the Parent Class to any NPC Group that will be Passive by default.
 * - Any passive-specific behavior will go here.
 * - (Example: Will never attack, Flees when attacked, etc.)
 */
public abstract class AbstractPassiveNPC extends AbstractNPC {

    public AbstractPassiveNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * registerGoals()
     * - Goals that will be applied globally to all PassiveNPCs.
     * - (Example: Randomly walking, Actively avoid certain NPCs, etc.)
     */
    @Override
    protected void registerGoals() {
        // Pull shared goals from AbstractNPC
        super.registerGoals();

        // Movement and idle behavior
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.5D));
    }
}
