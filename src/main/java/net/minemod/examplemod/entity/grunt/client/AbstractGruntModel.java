package net.minemod.examplemod.entity.grunt.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AbstractGruntModel extends HumanoidModel<PlayerRenderState> {
    private static final String LEFT_SLEEVE = "left_sleeve";
    private static final String RIGHT_SLEEVE = "right_sleeve";
    private static final String LEFT_PANTS = "left_pants";
    private static final String RIGHT_PANTS = "right_pants";
    private final List<ModelPart> bodyParts;
    public final ModelPart leftSleeve;
    public final ModelPart rightSleeve;
    public final ModelPart leftPants;
    public final ModelPart rightPants;
    public final ModelPart jacket;
    private final boolean slim;

    public AbstractGruntModel(ModelPart root, boolean pSlim) {
        super(root, RenderType::entityTranslucent);
        this.slim = pSlim;
        this.leftSleeve = this.leftArm.getChild("left_sleeve");
        this.rightSleeve = this.rightArm.getChild("right_sleeve");
        this.leftPants = this.leftLeg.getChild("left_pants");
        this.rightPants = this.rightLeg.getChild("right_pants");
        this.jacket = this.body.getChild("jacket");
        this.bodyParts = List.of(this.head, this.body, this.leftArm, this.rightArm, this.leftLeg, this.rightLeg);
    }

    public static MeshDefinition createMesh(CubeDeformation pCubeDeformation, boolean pSlim) {
        MeshDefinition meshdefinition = HumanoidModel.createMesh(pCubeDeformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        float f = 0.25F;
        if (pSlim) {
            PartDefinition partdefinition1 = partdefinition.addOrReplaceChild(
                    "left_arm",
                    CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, pCubeDeformation),
                    PartPose.offset(5.0F, 2.0F, 0.0F)
            );
            PartDefinition partdefinition2 = partdefinition.addOrReplaceChild(
                    "right_arm",
                    CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, pCubeDeformation),
                    PartPose.offset(-5.0F, 2.0F, 0.0F)
            );
            partdefinition1.addOrReplaceChild(
                    "left_sleeve",
                    CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                    PartPose.ZERO
            );
            partdefinition2.addOrReplaceChild(
                    "right_sleeve",
                    CubeListBuilder.create().texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                    PartPose.ZERO
            );
        } else {
            PartDefinition partdefinition4 = partdefinition.addOrReplaceChild(
                    "left_arm",
                    CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, pCubeDeformation),
                    PartPose.offset(5.0F, 2.0F, 0.0F)
            );
            PartDefinition partdefinition6 = partdefinition.getChild("right_arm");
            partdefinition4.addOrReplaceChild(
                    "left_sleeve",
                    CubeListBuilder.create().texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                    PartPose.ZERO
            );
            partdefinition6.addOrReplaceChild(
                    "right_sleeve",
                    CubeListBuilder.create().texOffs(40, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                    PartPose.ZERO
            );
        }

        PartDefinition partdefinition5 = partdefinition.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create().texOffs(16, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, pCubeDeformation),
                PartPose.offset(1.9F, 12.0F, 0.0F)
        );
        PartDefinition partdefinition7 = partdefinition.getChild("right_leg");
        partdefinition5.addOrReplaceChild(
                "left_pants",
                CubeListBuilder.create().texOffs(0, 48).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                PartPose.ZERO
        );
        partdefinition7.addOrReplaceChild(
                "right_pants",
                CubeListBuilder.create().texOffs(0, 32).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                PartPose.ZERO
        );
        PartDefinition partdefinition3 = partdefinition.getChild("body");
        partdefinition3.addOrReplaceChild(
                "jacket",
                CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, pCubeDeformation.extend(0.25F)),
                PartPose.ZERO
        );
        return meshdefinition;
    }

    public void setupAnim(PlayerRenderState pRenderState) {
        boolean flag = !pRenderState.isSpectator;
        this.body.visible = flag;
        this.rightArm.visible = flag;
        this.leftArm.visible = flag;
        this.rightLeg.visible = flag;
        this.leftLeg.visible = flag;
        this.hat.visible = pRenderState.showHat;
        this.jacket.visible = pRenderState.showJacket;
        this.leftPants.visible = pRenderState.showLeftPants;
        this.rightPants.visible = pRenderState.showRightPants;
        this.leftSleeve.visible = pRenderState.showLeftSleeve;
        this.rightSleeve.visible = pRenderState.showRightSleeve;
        super.setupAnim(pRenderState);
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        this.leftSleeve.visible = pVisible;
        this.rightSleeve.visible = pVisible;
        this.leftPants.visible = pVisible;
        this.rightPants.visible = pVisible;
        this.jacket.visible = pVisible;
    }

    @Override
    public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack) {
        this.root().translateAndRotate(pPoseStack);
        ModelPart modelpart = this.getArm(pSide);
        if (this.slim) {
            float f = 0.5F * (pSide == HumanoidArm.RIGHT ? 1 : -1);
            modelpart.x += f;
            modelpart.translateAndRotate(pPoseStack);
            modelpart.x -= f;
        } else {
            modelpart.translateAndRotate(pPoseStack);
        }
    }
}
