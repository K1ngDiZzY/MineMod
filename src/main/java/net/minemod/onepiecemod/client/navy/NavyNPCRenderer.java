package net.minemod.onepiecemod.client.navy;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npcs.navy.NavyNPC;
import org.jetbrains.annotations.NotNull;

public class NavyNPCRenderer extends AbstractNavyNPCRenderer<NavyNPC, HumanoidRenderState> {

    /**
     * This is a TEST NPC with a test skin to see how it works. will use this to model future NPCs
     * This is where we can override the "AbstractNPC" Skin (I am using a Test dummy skin as a reference)
     * */
    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/abstract_navy_npc.png");

    public NavyNPCRenderer(EntityRendererProvider.Context context) {
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