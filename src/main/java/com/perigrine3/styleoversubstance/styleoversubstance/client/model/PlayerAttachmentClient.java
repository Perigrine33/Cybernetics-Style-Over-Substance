package com.perigrine3.styleoversubstance.styleoversubstance.client.model;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.client.model.attachments.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Styleoversubstance.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class PlayerAttachmentClient {

    private PlayerAttachmentClient() {}

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(CapAttachmentModel.LAYER, CapAttachmentModel::createLayer);
        event.registerLayerDefinition(Visor1AttachmentModel.LAYER, Visor1AttachmentModel::createLayer);
        event.registerLayerDefinition(Glasses1AttachmentModel.LAYER, Glasses1AttachmentModel::createLayer);
        event.registerLayerDefinition(FunnelCollarAttachmentModel.LAYER, FunnelCollarAttachmentModel::createLayer);
        event.registerLayerDefinition(OniMaskAttachmentModel.LAYER, OniMaskAttachmentModel::createLayer);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer wide = event.getSkin(PlayerSkin.Model.WIDE);
        if (wide != null) {
            wide.addLayer(new PlayerAttachmentLayer(wide));
        }

        PlayerRenderer slim = event.getSkin(PlayerSkin.Model.SLIM);
        if (slim != null) {
            slim.addLayer(new PlayerAttachmentLayer(slim));
        }
    }
}