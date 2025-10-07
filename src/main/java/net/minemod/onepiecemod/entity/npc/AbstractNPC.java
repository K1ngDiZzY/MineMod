package net.minemod.onepiecemod.entity.npc;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;

public class AbstractNPC extends PathfinderMob {

    public AbstractNPC(EntityType<? extends NPC> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * registerGoals()
     * This method is where we can select Goals from the minecraft.world.entity.ai.goal library.
     * (Eventually we can create custom Goals that pertain to our mod, such as sink goals, targetNavy/Pirate goals, etc.)
     */
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1.0D));  // wandering
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));   // look around
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
