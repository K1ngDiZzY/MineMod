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
     * LOCATION_BY_VARIANT is a map of PirateVariants and ResourceLocations. This is how the getTextureLocation is able
     * to draw the correct ResourceLocation for the correct Variant.
     */
    private static final Map<PirateVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(PirateVariant.class), map -> {
                map.put(PirateVariant.DEFAULT,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_default.png"));
                map.put(PirateVariant.FRANKY,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_franky.png"));
                map.put(PirateVariant.SANJI,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_sanji.png"));
                map.put(PirateVariant.LUFFY,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_luffy.png"));
                map.put(PirateVariant.ZORO,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_zoro.png"));
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