package net.minemod.onepiecemod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.ModEntities;
import net.minemod.onepiecemod.entity.npc.NPC;

/**
 * No idea how this class works tbh 
 * (TODO: Figure out how this class works, messed with it and almost crash my PC....)
 */

@Mod.EventBusSubscriber(modid = OnePieceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NPC.get(), NPCRenderer::new);
    }


    @Mod.EventBusSubscriber(modid = OnePieceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ModEventBusSubscriber {
        @SubscribeEvent
        public static void onEntityAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.NPC.get(), NPC.createAttributes().build());
        }
    }
}