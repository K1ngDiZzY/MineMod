package net.minemod.onepiecemod.client.navy;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;

import java.util.Map;

public class NavyNPCRenderer extends AbstractNavyNPCRenderer<NavyNPC, NavyRenderState> {

    /**
     * LOCATION_BY_VARIANT is a map of NavyVariants and ResourceLocations. This is how the getTextureLocation is able
     * to draw the correct ResourceLocation for the correct Variant.
     */
    private static final Map<NavyVariant, ResourceLocation> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(NavyVariant.class), map -> {
                map.put(NavyVariant.DEFAULT,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_default.png"));
                map.put(NavyVariant.CADET,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_cadet.png"));
                map.put(NavyVariant.AKAINU,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_akainu.png"));
                map.put(NavyVariant.KOBY,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_koby.png"));
            });

    public NavyNPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(NavyNPC entity, NavyRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.setVariant(entity.getVariant());
    }

    @Override
    public NavyRenderState createRenderState() {
        return new NavyRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(NavyRenderState nRenderState) {
        return LOCATION_BY_VARIANT.get(nRenderState.getVariant());
    }
}