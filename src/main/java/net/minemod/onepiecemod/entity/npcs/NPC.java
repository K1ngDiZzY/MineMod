package net.minemod.onepiecemod.entity.npcs;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class NPC extends AbstractNPC {

    public NPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    /**
     * createAttributes()
     * This method is where we assign custom attributes to the NPCs (such as Speed, Health, Devil Fruit Effects, etc.)
     * @return custom attributes for the NPC that extends AbstractNPC
     */
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D) // default 20 HP (Set to 40 for testing)
                .add(Attributes.MOVEMENT_SPEED, 0.25D); // default walking speed
    }
}
