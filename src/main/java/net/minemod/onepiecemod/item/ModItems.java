package net.minemod.onepiecemod.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.ModEntities;

public class ModItems {
    /* Deferred Register essentially tells Minecraft to "Create" the custom items */
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, OnePieceMod.MODID);

    // Need to set ID for each object here. (Should make a new Creative Tab for the Test Mod)
    public static final RegistryObject<Item> GUM_GUM_FRUIT = ITEMS.register("gumgumfruit",
            () -> new Item(new Item.Properties()
                    .food(ModFoodProperties.DEVIL_FRUIT, ModFoodProperties.DEVIL_FRUIT_CONSUMABLE)
                    .setId(ITEMS.key("gumgumfruit"))));

    public static final RegistryObject<Item> FLAME_FLAME_FRUIT = ITEMS.register("flameflamefruit",
            () -> new Item(new Item.Properties()
                    .food(ModFoodProperties.DEVIL_FRUIT, ModFoodProperties.DEVIL_FRUIT_CONSUMABLE)
                    .setId(ITEMS.key("flameflamefruit"))));


    public static final RegistryObject<Item> BERRY = ITEMS.register("berry",
            () -> new Item(new Item.Properties().setId(ITEMS.key("berry"))));


    public static final RegistryObject<Item> NPC_SPAWN_EGG = ITEMS.register("npc_spawn_egg",
            () -> new SpawnEggItem(ModEntities.NPC.get(), new Item.Properties().setId(ITEMS.key("npc_spawn_egg"))));

    public static final RegistryObject<Item> NAVY_NPC_SPAWN_EGG = ITEMS.register("navy_npc_spawn_egg",
            () -> new SpawnEggItem(ModEntities.NAVY_NPC.get(), new Item.Properties().setId(ITEMS.key("navy_npc_spawn_egg"))));

    public static final RegistryObject<Item> PIRATE_NPC_SPAWN_EGG = ITEMS.register("pirate_npc_spawn_egg",
            () -> new SpawnEggItem(ModEntities.PIRATE_NPC.get(), new Item.Properties().setId(ITEMS.key("pirate_npc_spawn_egg"))));


    /* This is different from 1.20 and 1.21; Now in 1.21.8, the EventBus is condensed into one thing instead
     * of multiple different Event Busses to handle separate things. In order to get the BusGroup, you need to
     * grab it from the Java ModLoading context. */
    public static void register(FMLJavaModLoadingContext context) {
        ITEMS.register(context.getModBusGroup());
    }
}
