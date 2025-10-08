package net.minemod.onepiecemod.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npc.NPC;
import org.jetbrains.annotations.NotNull;

public class NPCRenderer extends AbstractNPCRenderer<NPC, HumanoidRenderState> {
    /** This is where we can override the "AbstractNPC" Skin (I am using a Navy skin as a reference) */
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/navy_npc.png");

    public NPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }

    /// This is where the texture is passed to the Renderer.
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HumanoidRenderState renderState) {
        return TEXTURE;
    }
}
