package com.perigrine3.styleoversubstance.styleoversubstance.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public final class PlayerAttachmentLayer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public PlayerAttachmentLayer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        PlayerAttachmentState state = PlayerAttachmentManager.getState(player);
        if (state == null || state.isEmpty()) return;

        PlayerModel<AbstractClientPlayer> playerModel = getParentModel();
        PlayerSkin.Model modelType = player.getSkin().model();

        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();
        boolean firstPerson = minecraft.options.getCameraType().isFirstPerson();
        boolean localViewTarget = cameraEntity == player;
        boolean guiRender = minecraft.screen != null;
        boolean viewerCanSee = shouldRenderToViewer(player);

        for (PlayerAttachment attachment : state.all()) {
            if (attachment == null) continue;
            if (attachment.respectsInvisibility() && !viewerCanSee) continue;

            if (attachment.thirdPersonOnly() && firstPerson && localViewTarget && !guiRender) {
                continue;
            }

            ModelPart anchorPart = resolveAnchor(playerModel, attachment.anchor());
            if (anchorPart == null) continue;

            Model attachmentModel = attachment.model(modelType);
            if (attachmentModel == null) continue;

            poseStack.pushPose();

            try {
                anchorPart.translateAndRotate(poseStack);
                attachment.setupPose(poseStack, player, playerModel, modelType, partialTick);

                for (AttachmentRenderLayer renderLayer : attachment.renderLayers(modelType)) {
                    if (renderLayer == null) continue;

                    RenderType renderType = resolveRenderType(attachmentModel, renderLayer);
                    int layerLight = resolvePackedLight(renderLayer, packedLight);
                    var vertexConsumer = buffer.getBuffer(renderType);

                    attachmentModel.renderToBuffer(poseStack, vertexConsumer, layerLight, OverlayTexture.NO_OVERLAY, renderLayer.color());

                    if (renderLayer.renderMode() == AttachmentRenderLayer.RenderMode.STRONG_EMISSIVE) {
                        attachmentModel.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, renderLayer.color());
                    }
                }
            } finally {
                poseStack.popPose();
            }
        }
    }

    private static RenderType resolveRenderType(Model attachmentModel, AttachmentRenderLayer renderLayer) {
        return switch (renderLayer.renderMode()) {
            case NORMAL, FULL_BRIGHT -> attachmentModel.renderType(renderLayer.texture());
            case EMISSIVE, STRONG_EMISSIVE -> RenderType.entityTranslucentEmissive(renderLayer.texture());
        };
    }

    private static int resolvePackedLight(AttachmentRenderLayer renderLayer, int packedLight) {
        return switch (renderLayer.renderMode()) {
            case NORMAL -> packedLight;
            case FULL_BRIGHT, EMISSIVE, STRONG_EMISSIVE -> LightTexture.FULL_BRIGHT;
        };
    }

    private static ModelPart resolveAnchor(PlayerModel<?> playerModel, AttachmentAnchor anchor) {
        if (playerModel == null || anchor == null) return null;

        return switch (anchor) {
            case HEAD -> playerModel.head;
            case BODY -> playerModel.body;
            case RIGHT_ARM -> playerModel.rightArm;
            case LEFT_ARM -> playerModel.leftArm;
            case RIGHT_LEG -> playerModel.rightLeg;
            case LEFT_LEG -> playerModel.leftLeg;
        };
    }

    private static boolean shouldRenderToViewer(AbstractClientPlayer target) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity instanceof Player viewer) {
            return !target.isInvisibleTo(viewer);
        }

        return !target.isInvisible();
    }
}