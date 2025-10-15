package net.minemod.onepiecemod.client.pirate;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class PirateRenderState extends HumanoidRenderState {

    private PirateVariant variant = PirateVariant.ZERO;

    public PirateVariant getVariant() {
        return variant;
    }

    public void setVariant(PirateVariant variant) {
        this.variant = variant;
    }
}
