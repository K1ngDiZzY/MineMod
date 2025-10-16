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
                map.put(NavyVariant.ZERO,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_v0.png"));
                map.put(NavyVariant.ONE,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_v1.png"));
                map.put(NavyVariant.TWO,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_v2.png"));
                map.put(NavyVariant.THREE,
                        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy/navy_npc_v3.png"));
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