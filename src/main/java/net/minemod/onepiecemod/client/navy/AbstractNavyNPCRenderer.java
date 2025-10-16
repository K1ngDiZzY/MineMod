package net.minemod.onepiecemod.client.navy;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;

public abstract class AbstractNavyNPCRenderer <T extends NavyNPC, S extends NavyRenderState> extends HumanoidMobRenderer<T, S, HumanoidModel<S>> {

    public AbstractNavyNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }
}