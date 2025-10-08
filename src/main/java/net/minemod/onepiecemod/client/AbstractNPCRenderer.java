package net.minemod.onepiecemod.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npc.NPC;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractNPCRenderer <T extends NPC, S extends HumanoidRenderState> extends HumanoidMobRenderer<T, S, HumanoidModel<S>> {

    /**
     * Abstract NPC Renderer contains the RENDERING LOGIC including: NPC Model and NPC Texture.
     * */
    /** This is where the model is passed to the Renderer. */
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/npc.png");

    public AbstractNPCRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    /// This is where the texture is passed to the Renderer.
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull S renderState) {
        return TEXTURE;
    }
}
