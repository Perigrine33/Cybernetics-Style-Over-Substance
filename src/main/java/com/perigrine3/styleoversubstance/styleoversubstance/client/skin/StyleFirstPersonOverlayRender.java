package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.createcybernetics.client.render.CyberwareLimbHider;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderArmEvent;

@EventBusSubscriber(modid = Styleoversubstance.MODID, value = Dist.CLIENT)
public final class StyleFirstPersonOverlayRender {

    private StyleFirstPersonOverlayRender() {}

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderArm(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        if (player == null || player.isInvisible()) return;

        HumanoidArm arm = event.getArm();

        if (arm == HumanoidArm.LEFT && !CyberwareLimbHider.shouldRenderLeftArm(player)) return;
        if (arm == HumanoidArm.RIGHT && !CyberwareLimbHider.shouldRenderRightArm(player)) return;

        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null || (!state.hasModifiers() && !state.hasHighlights())) return;

        Minecraft minecraft = Minecraft.getInstance();
        EntityRenderer<? super AbstractClientPlayer> renderer = minecraft.getEntityRenderDispatcher().getRenderer(player);
        if (!(renderer instanceof PlayerRenderer playerRenderer)) return;

        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
        PlayerSkin.Model modelType = player.getSkin().model();

        ModelPart armPart = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        ModelPart sleevePart = arm == HumanoidArm.RIGHT ? model.rightSleeve : model.leftSleeve;

        SkinModifier.OverlayPart armOverlayPart = arm == HumanoidArm.RIGHT ? SkinModifier.OverlayPart.RIGHT_ARM : SkinModifier.OverlayPart.LEFT_ARM;
        SkinModifier.OverlayPart sleeveOverlayPart = arm == HumanoidArm.RIGHT ? SkinModifier.OverlayPart.RIGHT_SLEEVE : SkinModifier.OverlayPart.LEFT_SLEEVE;

        boolean sleeveEnabled = arm == HumanoidArm.RIGHT ? SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.RIGHT_SLEEVE) : SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.LEFT_SLEEVE);

        var previousRightPose = model.rightArmPose;
        var previousLeftPose = model.leftArmPose;
        boolean previousCrouching = model.crouching;
        float previousSwimAmount = model.swimAmount;
        float previousAttackTime = model.attackTime;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();

        poseStack.pushPose();

        try {
            model.attackTime = 0.0F;
            model.crouching = false;
            model.swimAmount = 0.0F;
            model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            model.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

            for (SkinModifier modifier : state.getModifiers()) {
                if (modifier == null) continue;

                boolean renderArm = modifier.rendersOverlayPart(armOverlayPart);
                boolean renderSleeve = sleeveEnabled && modifier.rendersOverlayPart(sleeveOverlayPart);

                if (!renderArm && !renderSleeve) continue;

                ResourceLocation texture = modifier.getTexture(modelType);
                RenderType baseRenderType = RenderType.entityTranslucent(texture);
                var baseVertexConsumer = buffer.getBuffer(baseRenderType);

                if (renderArm) {
                    armPart.render(poseStack, baseVertexConsumer, event.getPackedLight(), OverlayTexture.NO_OVERLAY, modifier.getColor());
                }

                if (renderSleeve) {
                    sleevePart.render(poseStack, baseVertexConsumer, event.getPackedLight(), OverlayTexture.NO_OVERLAY, modifier.getColor());
                }

                if (modifier.hasGlint()) {
                    RenderType glintRenderType = SkinRenderTypes.translucentGlintOverlay(texture);
                    var glintVertexConsumer = buffer.getBuffer(glintRenderType);

                    if (renderArm) {
                        armPart.render(poseStack, glintVertexConsumer, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                    }

                    if (renderSleeve) {
                        sleevePart.render(poseStack, glintVertexConsumer, event.getPackedLight(), OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
                    }
                }
            }

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            for (SkinHighlight highlight : state.getHighlights()) {
                if (highlight == null) continue;

                ResourceLocation texture = highlight.getTexture(modelType);
                RenderType renderType;
                int light;
                int color;

                if (highlight.isEmissive()) {
                    light = 0x00F000F0;

                    if (highlight.tintOnEmissive()) {
                        renderType = SkinRenderTypes.emissiveTinted(texture);
                        color = highlight.getColor();
                    } else {
                        renderType = RenderType.entityTranslucent(texture);
                        color = 0xFFFFFFFF;
                    }
                } else {
                    light = event.getPackedLight();
                    renderType = RenderType.entityTranslucent(texture);
                    color = highlight.getColor();
                }

                var vertexConsumer = buffer.getBuffer(renderType);
                armPart.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, color);

                if (sleeveEnabled) {
                    sleevePart.render(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, color);
                }
            }
        } finally {
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();

            model.rightArmPose = previousRightPose;
            model.leftArmPose = previousLeftPose;
            model.crouching = previousCrouching;
            model.swimAmount = previousSwimAmount;
            model.attackTime = previousAttackTime;

            poseStack.popPose();
        }
    }
}