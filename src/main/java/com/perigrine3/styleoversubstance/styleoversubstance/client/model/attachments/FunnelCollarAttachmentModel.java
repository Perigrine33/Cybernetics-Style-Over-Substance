package com.perigrine3.styleoversubstance.styleoversubstance.client.model.attachments;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public final class FunnelCollarAttachmentModel extends Model {

    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "funnel_collar"), "main");

    private final ModelPart collar;
    private final ModelPart collarBrace;

    public FunnelCollarAttachmentModel(ModelPart root) {
        super((Function<ResourceLocation, RenderType>) RenderType::entityCutoutNoCull);
        this.collar = root.getChild("collar");
        this.collarBrace = root.getChild("collar_brace");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition collar = partdefinition.addOrReplaceChild("collar", CubeListBuilder.create().texOffs(22, 7).addBox(-4.5F, -4.0F, 0.0F, 9.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5F, 4.5F, 0.1309F, 0.0F, 0.0F));

        collar.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(22, 21).addBox(-0.5F, -3.5F, -4.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.3F, -0.5F, 2.6F, 0.0F, -0.7418F, 0.0F));

        collar.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(22, 15).addBox(-0.5F, -3.5F, -4.5F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.3F, -0.5F, 2.6F, 0.0F, 0.7418F, 0.0F));

        collar.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(28, 19).addBox(-0.457F, -2.5F, -5.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.2F, -1.5F, -3.9F, 0.0F, -0.0436F, 0.0F));

        collar.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(2, 23).addBox(-0.5F, -3.5F, -5.5F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.2F, -0.5F, -2.9F, 0.0F, -0.0436F, 0.0F));

        collar.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 19).addBox(-0.543F, -2.5F, -5.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.2F, -1.5F, -3.9F, 0.0F, 0.0436F, 0.0F));

        collar.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(2, 9).addBox(-0.5F, -3.5F, -5.5F, 1.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.2F, -0.5F, -2.9F, 0.0F, 0.0436F, 0.0F));

        PartDefinition collarBrace = partdefinition.addOrReplaceChild("collar_brace", CubeListBuilder.create().texOffs(22, 27).addBox(-5.5F, 1.5F, 3.0F, 3.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, -1.0F));

        collarBrace.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(22, 12).addBox(-2.5F, -1.0F, -2.25F, 5.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 1.5F, 5.0F, 0.1309F, 0.0F, 0.0F));

        collarBrace.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -1.0F, -6.0F, 9.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 0.5F, 5.5F, 0.1309F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        collar.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        collarBrace.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }
}