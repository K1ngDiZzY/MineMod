package net.minemod.onepiecemod.entity.npcs.passive.civilian;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minemod.onepiecemod.datagen.ModLootProvider;
import net.minemod.onepiecemod.entity.npcs.passive.AbstractPassiveNPC;
import net.minemod.onepiecemod.entity.settings.DifficultyBuilder;
import net.minemod.onepiecemod.entity.trade.TradeBuilder;
import net.minemod.onepiecemod.item.ModItems;

/**
 * This class represents a "Test-Dummy" NPC that is used for testing purposes. Does not run, and has High HP.
 * - (This class is placed in the "npcs.passive.civilian" package, because Civilian NPCs are coming soon.)
 */
public class NPC extends AbstractPassiveNPC {

    public NPC(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level, new DifficultyBuilder()
                .trades(TradeBuilder.create()
                        .addTrade(ModItems.BERRY.get(), 64, Items.BARRIER, 1)
                        .addTrade(ModItems.BERRY.get(), 64, ModItems.BERRY.get(), 64, Items.COMMAND_BLOCK, 1)
                )
        );
    }

    @Override
    public ResourceKey<LootTable> getNPCInventoryLootTable() {
        return ModLootProvider.Gameplay.NPC_INVENTORY;
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
