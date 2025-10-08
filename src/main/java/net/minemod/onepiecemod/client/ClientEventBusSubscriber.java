package net.minemod.onepiecemod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.ModEntities;
import net.minemod.onepiecemod.entity.npc.NPC;


@Mod.EventBusSubscriber(modid = OnePieceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NPC.get(), NPCRenderer::new);
    }

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.NPC.get(), NPC.createAttributes().build());
    }

}