package net.minemod.onepiecemod.client.pirate;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;

import java.util.Map;

public class PirateNPCRenderer extends AbstractPirateNPCRenderer<PirateNPC, PirateRenderState> {

    /**
     * LOCATION_BY_VARIANT is a map of PirateVariants and ResourseLocations. This is how the getTextureLocation is able
     * to draw the correct ResourceLocation for the correct Variant.
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

    public PirateNPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(PirateNPC entity, PirateRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.setVariant(entity.getVariant());
    }


    @Override
    public PirateRenderState createRenderState() {
        return new PirateRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(PirateRenderState pRenderState) {
        return LOCATION_BY_VARIANT.get(pRenderState.getVariant());
    }
}