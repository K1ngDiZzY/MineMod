package net.minemod.onepiecemod.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.block.ModBlocks;
import net.minemod.onepiecemod.item.ModItems;

import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected Stream<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get);
    }

    @Override
    protected Stream<Item> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream().map(RegistryObject::get);
    }

    @Override
    protected BlockModelGenerators getBlockModelGenerators(BlockStateGeneratorCollector blocks, ItemInfoCollector items, SimpleModelCollector models) {
        return new ModBlockModelGenerator(blocks, items, models);
    }

    @Override
    protected ItemModelGenerators getItemModelGenerators(ItemInfoCollector items, SimpleModelCollector models) {
        return new ModItemModelGenerator(items, models);
    }
}
