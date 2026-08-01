package net.minemod.onepiecemod.entity.npcs.hostile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.minemod.onepiecemod.entity.npcs.AbstractNPC;

/**
 * This Class is the Parent Class to any NPC Group that will be Hostile by default.
 * - Any hostile-specific behavior will go here.
 * - (Example: Chasing entities, Always attacks nearest entity, Flees only when low HP, etc.)
 */
public class AbstractHostileNPC extends AbstractNPC {

    /** Constructor */
    public AbstractHostileNPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }


}
