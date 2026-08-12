package net.minemod.onepiecemod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.block.ModBlocks;
import net.minemod.onepiecemod.item.ModItems;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static net.minemod.onepiecemod.block.ModBlocks.BLOCKS;

/**
 * ModLootProvider class provides a spot to declare Custom ModBlocks and Loot Tables for Data Generation
 * - Blocks subclass: Iterates through all ModBlocks in BLOCKS set to have a .json file created for each one
 * - Entities subclass: (TODO) Add Entity Loot Tables here
 */
public class ModLootProvider extends LootTableProvider {

    public ModLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)), lookupProvider);
    }

    // Declare loot table .json files to be created for custom ModBlocks
    private static class Blocks extends BlockLootSubProvider {
        protected Blocks(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), lookupProvider);
        }

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
}
