package net.minemod.onepiecemod.client.pirate;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;

import java.util.Map;
import java.util.Random;

public class PirateNPCRenderer extends AbstractPirateNPCRenderer<PirateNPC, HumanoidRenderState> {

    /**
     * This is a TEST NPC with a test skin to see how it works. will use this to model future NPCs
     * This is where we can override the "AbstractNPC" Skin (I am using a Test dummy skin as a reference)
     *
     */
    private static final Map<PirateVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PirateVariant.class), map -> {
                map.put(PirateVariant.ZERO,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/pirate/pirate_npc_v0.png"));
                map.put(PirateVariant.ONE,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v1.png"));
                map.put(PirateVariant.TWO,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v2.png"));
                map.put(PirateVariant.THREE,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v3.png"));
                map.put(PirateVariant.FOUR,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v4.png"));
            });

    /*
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/pirate/pirate_npc_v0.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v1.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v2.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v3.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v4.png")
    };
    */
    public PirateNPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public PirateRenderState createRenderState() {
        PirateRenderState renderState = new PirateRenderState();
        PirateVariant[] variants = PirateVariant.values();
        PirateVariant variant = variants[new Random().nextInt(variants.length)];
        renderState.setVariant(variant);
        return new PirateRenderState();
    }


    // TODO: FIX THIS METHOD: This method is called every tick, and is setting a random skin every tick
    // We need to figure out how to get this setup with the individual entity
    // The tutorial series passes (PirateNPC entity) as the parameter, but that doesn't work in 1.21.8 :/
    @Override
    public ResourceLocation getTextureLocation(HumanoidRenderState pRenderState) {
        PirateVariant[] variants = PirateVariant.values();
        PirateVariant randomVariant = variants[new Random().nextInt(variants.length)];
        return LOCATION_BY_VARIANT.get(randomVariant);
    }
}