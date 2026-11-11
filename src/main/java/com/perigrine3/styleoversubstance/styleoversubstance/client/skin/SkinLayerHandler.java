package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.createcybernetics.client.render.CyberwareLimbHider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;

public final class SkinLayerHandler extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    private final PlayerModel<AbstractClientPlayer> overlayModel;

    public SkinLayerHandler(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent, PlayerModel<AbstractClientPlayer> overlayModel) {
        super(parent);
        this.overlayModel = overlayModel;
    }

    private static boolean shouldRenderOverlaysFor(AbstractClientPlayer target) {
        Minecraft mc = Minecraft.getInstance();
        Entity camera = mc.getCameraEntity();

        if (camera instanceof Player viewer) {
            return !target.isInvisibleTo(viewer);
        }

        return !target.isInvisible();
    }

    private static void copyModelPose(PlayerModel<AbstractClientPlayer> source, PlayerModel<AbstractClientPlayer> target) {
        source.copyPropertiesTo(target);

        target.head.copyFrom(source.head);
        target.hat.copyFrom(source.hat);
        target.body.copyFrom(source.body);
        target.jacket.copyFrom(source.jacket);
        target.leftArm.copyFrom(source.leftArm);
        target.leftSleeve.copyFrom(source.leftSleeve);
        target.rightArm.copyFrom(source.rightArm);
        target.rightSleeve.copyFrom(source.rightSleeve);
        target.leftLeg.copyFrom(source.leftLeg);
        target.leftPants.copyFrom(source.leftPants);
        target.rightLeg.copyFrom(source.rightLeg);
        target.rightPants.copyFrom(source.rightPants);
    }

    private static void applyOverlayVisibility(AbstractClientPlayer player, PlayerModel<AbstractClientPlayer> model) {
        boolean hasLeftArm = CyberwareLimbHider.shouldRenderLeftArm(player);
        boolean hasRightArm = CyberwareLimbHider.shouldRenderRightArm(player);
        boolean hasLeftLeg = CyberwareLimbHider.shouldRenderLeftLeg(player);
        boolean hasRightLeg = CyberwareLimbHider.shouldRenderRightLeg(player);

        model.head.visible = true;
        model.body.visible = true;

        model.leftArm.visible = hasLeftArm;
        model.rightArm.visible = hasRightArm;
        model.leftLeg.visible = hasLeftLeg;
        model.rightLeg.visible = hasRightLeg;

        model.hat.visible = SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.HAT);
        model.jacket.visible = SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.JACKET);
        model.leftSleeve.visible = hasLeftArm && SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.LEFT_SLEEVE);
        model.rightSleeve.visible = hasRightArm && SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.RIGHT_SLEEVE);
        model.leftPants.visible = hasLeftLeg && SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.LEFT_PANTS_LEG);
        model.rightPants.visible = hasRightLeg && SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.RIGHT_PANTS_LEG);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!shouldRenderOverlaysFor(player)) return;

        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null || !state.hasModifiers()) return;

        PlayerModel<AbstractClientPlayer> parentModel = this.getParentModel();
        copyModelPose(parentModel, this.overlayModel);
        applyOverlayVisibility(player, this.overlayModel);

        PlayerSkin.Model modelType = player.getSkin().model();

        for (SkinModifier modifier : state.getModifiers()) {
            if (modifier == null) continue;

            poseStack.pushPose();

            try {
                ResourceLocation texture = modifier.getTexture(modelType);
                int color = modifier.getColor();

                var baseVertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(texture));
                SkinOverlayModelRenderer.renderModifier(this.overlayModel, modifier, poseStack, baseVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);

                if (modifier.hasGlint()) {
                    var glintVertexConsumer = buffer.getBuffer(SkinRenderTypes.translucentGlintOverlay(texture));
                    SkinOverlayModelRenderer.renderModifier(this.overlayModel, modifier, poseStack, glintVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                }
            } finally {
                poseStack.popPose();
            }
        }
    }
}