package net.minemod.examplemod.entity;

import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.examplemod.ExampleMod;
import net.minemod.examplemod.entity.grunt.Grunt;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ExampleMod.MODID);

    public static final RegistryObject<EntityType<Grunt>> GRUNT =
            ENTITY_TYPES.register("grunt", () -> EntityType.Builder
                    .of(Grunt::new, MobCategory.MONSTER)
                    .sized(1f, 2f)
                    .clientTrackingRange(8)
                    .build(ResourceKey.create(ForgeRegistries.ENTITY_TYPES.getRegistryKey(),
                            ResourceLocation.fromNamespaceAndPath(ExampleMod.MODID, "grunt")))); // Let Forge handle the registry key


    public static void register(FMLJavaModLoadingContext context) {
        ENTITY_TYPES.register(context.getModBusGroup());
    }
}
