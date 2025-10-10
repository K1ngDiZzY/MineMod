package net.minemod.onepiecemod.client.pirate;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractPirateNPCRenderer <T extends PirateNPC, S extends HumanoidRenderState> extends HumanoidMobRenderer<T, S, HumanoidModel<S>> {

    public AbstractPirateNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }
}