package net.minemod.onepiecemod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.block.ModBlocks;
import net.minemod.onepiecemod.entity.ModEntities;
import net.minemod.onepiecemod.item.ModItems;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static net.minemod.onepiecemod.block.ModBlocks.BLOCKS;
import static net.minemod.onepiecemod.entity.ModEntities.ENTITY_TYPES;

/**
 * ModLootProvider class provides a spot to declare Custom ModBlocks and Loot Tables for Data Generation
 * - Blocks subclass: Iterates through all ModBlocks in BLOCKS set to have a .json file created for each one
 * - Entities subclass: (TODO) Add Entity Loot Tables here
 */
public class ModLootProvider extends LootTableProvider {

    public ModLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK),

                new SubProviderEntry(Entities::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(Gameplay::new, LootContextParamSets.CHEST)
                ), lookupProvider);


    }

    // Declare loot table .json files to be created for custom ModBlocks
    // TODO: Add resourceLocation to make a Blocks Folder? Otherwise will land in items. may not be a big deal
    private static class Blocks extends BlockLootSubProvider {
        protected Blocks(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        // Generates .json files for each ModBlock. If any changes are made to existing ModBlocks, that .json is updated.
        @Override
        protected void generate() {
            // "dropSelf(ModBlocks.BLOCK_OF_BERRY)" - (Item will drop itself)

            // Create a loot table for Berry Ore. - (Call createMultipleOreDrops() with custom Min/Max values)
            this.add(ModBlocks.BERRY_ORE.get(),
                    block -> createMultipleOreDrops(ModBlocks.BERRY_ORE.get(), ModItems.BERRY.get(), 2, 6));
        }

        // Builder Method "createMultupleOreDrops()" can be used with any "Ore" type ModBlock. - (drops multiple resources)
        protected LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
            return this.createSilkTouchDispatchTable(
                    block, this.applyExplosionDecay(
                            block, LootItem.lootTableItem(item)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                                    .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                    )
            );
        }

        // Iterate through all the custom ModBlocks. - (Note: Each block must be called in generate() method)
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
        }
    }

    // Declare loot table .json files to be created for custom ModEntities?
    // private static class Entities extends EntityLootSubProvider {}
    // TODO: Add resourceLocation to make a Entities Folder? Otherwise will land in items. may not be a big deal
    private static class Entities extends EntityLootSubProvider {

        protected Entities(HolderLookup.Provider lookupProvider) {
            super(FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

        // Generates .json files for each ModEntity. If any changes are made to existing ModEntities, that .json is updated.
        @Override
        public void generate() {
            // Death Loot for a NavyNPC is between 3 and 8 Diamonds
            this.add(ModEntities.NAVY_NPC.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.DIAMOND)
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 8.0F)))))
            );

            // Death Loot for a PirateNPC is between 1 and 5 Berry items.
            this.add(ModEntities.PIRATE_NPC.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ModItems.BERRY.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 5.0F)))))
            );

            this.add(ModEntities.NPC.get(), LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.COMMAND_BLOCK)))
                    );
        }

        // Iterate through all the custom ModEntities. - (Note: Each entity must be called in generate() method)
        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes(){
            return ENTITY_TYPES.getEntries().stream().map(RegistryObject::get);
        }
    }

    // Declare loot table .json files to be created for custom Gameplay events. (chests, Inventory generation, etc..)
    // TODO: Fill in generate() method. Create custom LootPools here.
    // TODO: Figure out how to make this work
    private static class Gameplay implements LootTableSubProvider {

        protected Gameplay(HolderLookup.Provider lookupProvider) {}

        public static final ResourceKey<LootTable> PIRATE_INVENTORY = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "gameplay/npc_inventory/pirate")
        );

        public static final ResourceKey<LootTable> NAVY_INVENTORY = ResourceKey.create(
                Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "gameplay/npc_inventory/navy")
        );

        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
            // Pirate Initial Inventory Pool
            builder.accept(PIRATE_INVENTORY, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(5))
                            .add(LootItem.lootTableItem(Items.BOW).setWeight(3)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(ModItems.BERRY.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(10.0F, 25.0F)))))
            );

            // Navy Initial Inventory Pool
            builder.accept(NAVY_INVENTORY, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.CROSSBOW).setWeight(5))
                            .add(LootItem.lootTableItem(Items.IRON_SWORD).setWeight(5)))
            );
        }
    }
}
