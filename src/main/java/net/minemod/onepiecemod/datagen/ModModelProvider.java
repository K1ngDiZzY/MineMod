package net.minemod.onepiecemod.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.block.ModBlocks;
import net.minemod.onepiecemod.item.ModItems;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
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

    // --- Nested Generator Classes ---

    public static class ModBlockModelGenerator extends BlockModelGenerators {
        public ModBlockModelGenerator(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
            super(blockStateOutput, itemModelOutput, modelOutput);
        }

        @Override
        public void run() {
            for (RegistryObject<Block> entry : ModBlocks.BLOCKS.getEntries()) {
                this.createTrivialCube(entry.get());
            }
            ModBlocks.BLOCK_ITEMS.getEntries().forEach((item) -> {
                this.itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(((BlockItem) item.get()).getBlock())));
            });
        }
    }

    public static class ModItemModelGenerator extends ItemModelGenerators {
        public ModItemModelGenerator(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
            super(itemModelOutput, modelOutput);
        }

        @Override
        public void run() {
            for (RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
                this.generateFlatItem(entry.get(), ModelTemplates.FLAT_ITEM);
            }
        }
    }
}
