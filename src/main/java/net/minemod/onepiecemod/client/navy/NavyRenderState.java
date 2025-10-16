package net.minemod.onepiecemod.client.navy;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class NavyRenderState extends HumanoidRenderState {

    public NavyVariant variant = NavyVariant.DEFAULT;

    public NavyVariant getVariant() {
        return variant;
    }

    public void setVariant(NavyVariant variant) {
        this.variant = variant;
    }
}
