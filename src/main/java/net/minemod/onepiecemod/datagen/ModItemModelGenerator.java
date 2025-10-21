package net.minemod.onepiecemod.datagen;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.item.ModItems;

import java.util.function.BiConsumer;

public class ModItemModelGenerator extends ItemModelGenerators {

    public ModItemModelGenerator(ItemModelOutput pItemModelOutput, BiConsumer<ResourceLocation, ModelInstance> pModelOutput) {
        super(pItemModelOutput, pModelOutput);
    }

    @Override
    public void run() {
        for(RegistryObject<Item> entry : ModItems.ITEMS.getEntries()) {
            this.generateFlatItem(entry.get(), ModelTemplates.FLAT_ITEM);
        }
    }
}
