package net.minemod.onepiecemod.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npcs.NPC;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;

public class ModEntities {
    /** Deferred Register for Entities. (Any custom NPCs would be added here) */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, OnePieceMod.MODID);

    /** Base NPC (Framework for future NPCs to be added) */
    public static final RegistryObject<EntityType<NPC>> NPC =
            ENTITY_TYPES.register("npc",
                    () -> EntityType.Builder.of(NPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.8f)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.parse("onepiecemod:npc"))));

    public static final RegistryObject<EntityType<NavyNPC>> NAVY_NPC =
            ENTITY_TYPES.register("navy_npc",
                    () -> EntityType.Builder.of(NavyNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.8f)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.parse("onepiecemod:navy_npc"))));

    public static final RegistryObject<EntityType<PirateNPC>> PIRATE_NPC =
            ENTITY_TYPES.register("pirate_npc",
                    () -> EntityType.Builder.of(PirateNPC::new, MobCategory.CREATURE)
                            .sized(0.6f, 1.8f)
                            .build(ResourceKey.create(
                                    Registries.ENTITY_TYPE,
                                    ResourceLocation.parse("onepiecemod:pirate_npc"))));

    public static void register(FMLJavaModLoadingContext context) {
        ENTITY_TYPES.register(context.getModBusGroup());
    }
}
