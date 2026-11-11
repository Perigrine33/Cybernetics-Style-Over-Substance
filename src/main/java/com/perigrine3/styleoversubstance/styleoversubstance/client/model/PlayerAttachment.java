package com.perigrine3.styleoversubstance.styleoversubstance.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;

import java.util.List;

public interface PlayerAttachment {

    AttachmentAnchor anchor();

    Model model(PlayerSkin.Model modelType);

    List<AttachmentRenderLayer> renderLayers(PlayerSkin.Model modelType);

    default void setupPose(PoseStack poseStack,
                           AbstractClientPlayer player,
                           PlayerModel<AbstractClientPlayer> parentModel,
                           PlayerSkin.Model modelType,
                           float partialTick) {}

    default boolean respectsInvisibility() {
        return true;
    }

    default boolean thirdPersonOnly() {
        return false;
    }
}