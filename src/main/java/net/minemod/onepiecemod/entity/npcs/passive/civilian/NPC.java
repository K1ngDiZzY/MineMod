package net.minemod.onepiecemod.entity.npcs.passive.civilian;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minemod.onepiecemod.entity.npcs.passive.AbstractPassiveNPC;

/**
 * This class represents a "Test-Dummy" NPC that is used for testing purposes. Does not move, and has High HP.
 * - (This class is placed in the "npcs.passive.civilian" package, because Civilian NPCs are coming soon.)
 */
public class NPC extends AbstractPassiveNPC {

    public NPC(EntityType<? extends PathfinderMob> type, Level pLevel) {
        super(type, pLevel);
    }

    @Override
    public ResourceKey<LootTable> getNPCInventoryLootTable() {
        return null;
    }

    /**
     * createAttributes()
     * - This method builds the attributes for a "Test-Dummy" NPC. Has high HP, and low Speed values for testing.
     * @return custom attributes for the NPC that extends AbstractNPC
     */
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D) // default 20 HP (Set to 40 for testing)
                .add(Attributes.MOVEMENT_SPEED, 0.25D); // default walking speed
    }


}
