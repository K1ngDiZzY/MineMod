package net.minemod.examplemod.entity.grunt.client;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GruntRenderState extends HumanoidRenderState {
    public boolean isNavy;
    public boolean isPirate;
    public boolean hasDevilFruit;
}
