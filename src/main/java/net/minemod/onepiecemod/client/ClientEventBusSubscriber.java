package net.minemod.onepiecemod.client;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.client.navy.NavyNPCRenderer;
import net.minemod.onepiecemod.client.pirate.PirateNPCRenderer;
import net.minemod.onepiecemod.entity.ModEntities;
import net.minemod.onepiecemod.entity.npcs.NPC;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;


@Mod.EventBusSubscriber(modid = OnePieceMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        EntityRenderers.register(ModEntities.PIRATE_NPC.get(), PirateNPCRenderer::new);
        EntityRenderers.register(ModEntities.NAVY_NPC.get(), NavyNPCRenderer::new);
        EntityRenderers.register(ModEntities.NPC.get(), NPCRenderer::new);
    }

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.NPC.get(), NPC.createAttributes().build());
        event.put(ModEntities.NAVY_NPC.get(), NavyNPC.createAttributes().build());
        event.put(ModEntities.PIRATE_NPC.get(), PirateNPC.createAttributes().build());
    }

}