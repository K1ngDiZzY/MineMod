package net.minemod.onepiecemod.client.pirate;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class PirateRenderState extends HumanoidRenderState {

    public PirateVariant variant = PirateVariant.DEFAULT;

    public PirateVariant getVariant() {
        return variant;
    }

    public void setVariant(PirateVariant variant) {
        this.variant = variant;
    }
}
