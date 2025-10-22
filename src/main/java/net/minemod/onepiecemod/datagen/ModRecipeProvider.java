package net.minemod.onepiecemod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.block.Blocks;
import net.minemod.onepiecemod.block.ModBlocks;
import net.minemod.onepiecemod.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        var itemLookup = registries.lookupOrThrow(net.minecraft.core.registries.Registries.ITEM);

        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, ModBlocks.BERRY_ORE.get(), 1)
                .define('B', ModItems.BERRY.get())
                .define('S', Blocks.STONE)
                .pattern(" B ")
                .pattern("BSB")
                .pattern(" B ")
                .unlockedBy(getHasName(ModItems.BERRY.get()), has(ModItems.BERRY.get()))
                .save(output);
    }

    public static class Runner extends RecipeProvider.Runner {

        protected Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "One Piece Mod Recipes";
        }
    }
}
