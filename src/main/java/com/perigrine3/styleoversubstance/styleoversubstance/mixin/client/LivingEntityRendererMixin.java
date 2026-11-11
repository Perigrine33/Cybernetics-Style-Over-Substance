package com.perigrine3.styleoversubstance.styleoversubstance.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinModifier;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinModifierManager;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinModifierState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    protected M model;

    @Unique
    private boolean styleoversubstance$previousHatVisible;

    @Unique
    private boolean styleoversubstance$previousJacketVisible;

    @Unique
    private boolean styleoversubstance$previousLeftSleeveVisible;

    @Unique
    private boolean styleoversubstance$previousRightSleeveVisible;

    @Unique
    private boolean styleoversubstance$previousLeftPantsVisible;

    @Unique
    private boolean styleoversubstance$previousRightPantsVisible;

    @Unique
    private boolean styleoversubstance$visibilityModified;

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
                    shift = At.Shift.BEFORE
            ),
            require = 1
    )
    private void styleoversubstance$hideVanillaOuterLayers(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.styleoversubstance$visibilityModified = false;

        if (!((Object) this instanceof PlayerRenderer)) return;
        if (!(entity instanceof AbstractClientPlayer player)) return;
        if (!(this.model instanceof PlayerModel<?> playerModel)) return;

        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null) return;

        EnumSet<SkinModifier.HideVanilla> hideMask = state.getHideMask();
        if (hideMask.isEmpty()) return;

        this.styleoversubstance$previousHatVisible = playerModel.hat.visible;
        this.styleoversubstance$previousJacketVisible = playerModel.jacket.visible;
        this.styleoversubstance$previousLeftSleeveVisible = playerModel.leftSleeve.visible;
        this.styleoversubstance$previousRightSleeveVisible = playerModel.rightSleeve.visible;
        this.styleoversubstance$previousLeftPantsVisible = playerModel.leftPants.visible;
        this.styleoversubstance$previousRightPantsVisible = playerModel.rightPants.visible;
        this.styleoversubstance$visibilityModified = true;

        if (hideMask.contains(SkinModifier.HideVanilla.HAT)) {
            playerModel.hat.visible = false;
        }

        if (hideMask.contains(SkinModifier.HideVanilla.JACKET)) {
            playerModel.jacket.visible = false;
        }

        if (hideMask.contains(SkinModifier.HideVanilla.LEFT_SLEEVE)) {
            playerModel.leftSleeve.visible = false;
        }

        if (hideMask.contains(SkinModifier.HideVanilla.RIGHT_SLEEVE)) {
            playerModel.rightSleeve.visible = false;
        }

        if (hideMask.contains(SkinModifier.HideVanilla.LEFT_PANTS)) {
            playerModel.leftPants.visible = false;
        }

        if (hideMask.contains(SkinModifier.HideVanilla.RIGHT_PANTS)) {
            playerModel.rightPants.visible = false;
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V",
                    shift = At.Shift.AFTER
            ),
            require = 1
    )
    private void styleoversubstance$restoreVanillaOuterLayers(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!this.styleoversubstance$visibilityModified) return;
        if (!(this.model instanceof PlayerModel<?> playerModel)) return;

        playerModel.hat.visible = this.styleoversubstance$previousHatVisible;
        playerModel.jacket.visible = this.styleoversubstance$previousJacketVisible;
        playerModel.leftSleeve.visible = this.styleoversubstance$previousLeftSleeveVisible;
        playerModel.rightSleeve.visible = this.styleoversubstance$previousRightSleeveVisible;
        playerModel.leftPants.visible = this.styleoversubstance$previousLeftPantsVisible;
        playerModel.rightPants.visible = this.styleoversubstance$previousRightPantsVisible;
        this.styleoversubstance$visibilityModified = false;
    }
}