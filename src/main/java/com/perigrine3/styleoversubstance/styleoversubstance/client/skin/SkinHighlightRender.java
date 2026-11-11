package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
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
public final class SkinHighlightRender {

    private SkinHighlightRender() {}

    public static void apply(SkinModifierState state, boolean enabled, ResourceLocation wide, ResourceLocation slim, int color, boolean emissive) {
        apply(state, enabled, wide, slim, color, emissive, false);
    }

    public static void apply(SkinModifierState state, boolean enabled, ResourceLocation wide, ResourceLocation slim, int color, boolean emissive, boolean tintOnEmissive) {
        if (state == null) return;

        if (!enabled) {
            state.clearHighlights();
            return;
        }

        state.addHighlight(new SkinHighlight(wide, slim, color, emissive, tintOnEmissive));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public static void onRenderArm(RenderArmEvent event) {
        AbstractClientPlayer player = event.getPlayer();
        if (player == null) return;

        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null || !state.hasHighlights()) return;

        Minecraft minecraft = Minecraft.getInstance();
        EntityRenderer<? super AbstractClientPlayer> renderer = minecraft.getEntityRenderDispatcher().getRenderer(player);
        if (!(renderer instanceof PlayerRenderer playerRenderer)) return;

        HumanoidArm arm = event.getArm();

        PlayerModel<AbstractClientPlayer> model = playerRenderer.getModel();
        PlayerSkin.Model modelType = player.getSkin().model();

        ModelPart armPart = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
        ModelPart sleevePart = arm == HumanoidArm.RIGHT ? model.rightSleeve : model.leftSleeve;

        boolean sleeveEnabled = isSleeveEnabled(player, arm);
        boolean hideSleeve = shouldHideSleeve(state, arm);
        boolean renderSleeve = sleeveEnabled && !hideSleeve;

        var previousRightPose = model.rightArmPose;
        var previousLeftPose = model.leftArmPose;
        boolean previousCrouching = model.crouching;
        float previousSwimAmount = model.swimAmount;
        float previousAttackTime = model.attackTime;

        boolean previousRightArmVisible = model.rightArm.visible;
        boolean previousLeftArmVisible = model.leftArm.visible;
        boolean previousRightSleeveVisible = model.rightSleeve.visible;
        boolean previousLeftSleeveVisible = model.leftSleeve.visible;

        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource buffer = event.getMultiBufferSource();

        poseStack.pushPose();

        try {
            model.attackTime = 0.0F;
            model.crouching = false;
            model.swimAmount = 0.0F;

            model.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            model.leftArmPose = HumanoidModel.ArmPose.EMPTY;

            model.rightArm.visible = arm == HumanoidArm.RIGHT;
            model.rightSleeve.visible = arm == HumanoidArm.RIGHT && renderSleeve;
            model.leftArm.visible = arm == HumanoidArm.LEFT;
            model.leftSleeve.visible = arm == HumanoidArm.LEFT && renderSleeve;

            model.setupAnim(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

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

                if (renderSleeve) {
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

            model.rightArm.visible = previousRightArmVisible;
            model.leftArm.visible = previousLeftArmVisible;
            model.rightSleeve.visible = previousRightSleeveVisible;
            model.leftSleeve.visible = previousLeftSleeveVisible;

            poseStack.popPose();
        }
    }

    private static boolean isSleeveEnabled(AbstractClientPlayer player, HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.RIGHT_SLEEVE) : SkinVanillaWearVisibility.isModelPartShownNow(player, PlayerModelPart.LEFT_SLEEVE);
    }

    private static boolean shouldHideSleeve(SkinModifierState state, HumanoidArm arm) {
        return arm == HumanoidArm.RIGHT ? state.getHideMask().contains(SkinModifier.HideVanilla.RIGHT_SLEEVE) : state.getHideMask().contains(SkinModifier.HideVanilla.LEFT_SLEEVE);
    }
}