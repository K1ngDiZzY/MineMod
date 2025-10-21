package net.minemod.onepiecemod.datagen;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.block.ModBlocks;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ModBlockModelGenerator extends BlockModelGenerators {
    public ModBlockModelGenerator(Consumer<BlockModelDefinitionGenerator> pBlockStateOutput, ItemModelOutput pItemModelOutput, BiConsumer<ResourceLocation, ModelInstance> pModelOutput) {
        super(pBlockStateOutput, pItemModelOutput, pModelOutput);
    }

    @Override
    public void run() {
        for (RegistryObject<Block> entry : ModBlocks.BLOCKS.getEntries()) {
            this.createTrivialCube(entry.get());
        }
        ModBlocks.BLOCK_ITEMS.getEntries().forEach((item) -> {
            itemModelOutput.accept(item.get(), ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(((BlockItem) item.get()).getBlock())));
        });
    }
}
