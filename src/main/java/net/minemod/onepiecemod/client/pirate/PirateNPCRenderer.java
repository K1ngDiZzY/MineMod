package net.minemod.onepiecemod.client.pirate;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minemod.onepiecemod.OnePieceMod;
import net.minemod.onepiecemod.entity.npcs.pirate.PirateNPC;
import org.jetbrains.annotations.NotNull;

public class PirateNPCRenderer extends AbstractPirateNPCRenderer<PirateNPC, HumanoidRenderState> {

    /**
     * This is a TEST NPC with a test skin to see how it works. will use this to model future NPCs
     * This is where we can override the "AbstractNPC" Skin (I am using a Test dummy skin as a reference)
     *
     */
    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] {
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID, "textures/entity/pirate/pirate_npc_v0.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v1.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v2.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v3.png"),
            ResourceLocation.fromNamespaceAndPath(OnePieceMod.MODID,"textures/entity/pirate/pirate_npc_v4.png")
    };
    private PirateNPC currentEntity;

    public PirateNPCRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public HumanoidRenderState createRenderState() {
        return new HumanoidRenderState();
    }

    // Helper to access the current entity
    private PirateNPC getRenderedEntity() {
        return this.currentEntity;
    }

    @Override
    public ResourceLocation getTextureLocation(HumanoidRenderState pRenderState) {
        PirateNPC entity = getRenderedEntity();
        if (entity != null) {
            int variant = entity.getSkinVariant();
            return TEXTURES[Math.max(0, Math.min(TEXTURES.length - 1, variant))];
        }
        return TEXTURES[0]; // fallback
    }
}