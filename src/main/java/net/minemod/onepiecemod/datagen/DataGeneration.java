package net.minemod.onepiecemod.datagen;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minemod.onepiecemod.OnePieceMod;

@Mod.EventBusSubscriber(modid = OnePieceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var output = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var helper = event.getExistingFileHelper(); // Will be used for ModTagProvider

        generator.addProvider(event.includeServer(), new ModLootProvider(output, lookupProvider));
        generator.addProvider(event.includeServer(), new ModTagProvider(output, lookupProvider, helper));

        generator.addProvider(event.includeClient(), new ModModelProvider(output));
    }
}
