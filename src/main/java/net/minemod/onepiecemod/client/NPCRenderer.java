package net.minemod.onepiecemod.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minemod.onepiecemod.entity.npc.NPC;

public class NPCRenderer extends AbstractNPCRenderer<NPC, HumanoidRenderState> {
    public NPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }

    //TODO: Figure out how to make NPCRenderer have the getTextureLocation instead of AbstractNPCRenderer
    /*
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HumanoidRenderState renderState) {
        // Example: use different textures based on a custom field in your NPC
        NPC npc = renderState.getEntity();
        String skinType = npc.getSkinType(); // You define this method in your NPC class

        return ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/npc/" + skinType + ".png");
    }
     */
}
