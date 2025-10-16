package net.minemod.onepiecemod.client.pirate;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;

public abstract class AbstractPirateNPCRenderer <T extends PirateNPC, S extends PirateRenderState> extends HumanoidMobRenderer<T, S, HumanoidModel<S>> {

    public AbstractPirateNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }
}