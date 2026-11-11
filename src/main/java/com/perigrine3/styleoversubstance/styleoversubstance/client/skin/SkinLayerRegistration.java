package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import com.perigrine3.createcybernetics.client.skin.ExosuitArmorStandLayer;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Styleoversubstance.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class SkinLayerRegistration {

    private SkinLayerRegistration() {}

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        PlayerRenderer wide = event.getSkin(PlayerSkin.Model.WIDE);

        if (wide != null) {
            PlayerModel<?> wideModifierModel = new PlayerModel<>(event.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);
            PlayerModel<?> wideHighlightModel = new PlayerModel<>(event.getEntityModels().bakeLayer(ModelLayers.PLAYER), false);

            wide.addLayer(new SkinLayerHandler(wide, castPlayerModel(wideModifierModel)));
            wide.addLayer(new SkinHighlightLayer(wide, castPlayerModel(wideHighlightModel)));
        }

        PlayerRenderer slim = event.getSkin(PlayerSkin.Model.SLIM);

        if (slim != null) {
            PlayerModel<?> slimModifierModel = new PlayerModel<>(event.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);
            PlayerModel<?> slimHighlightModel = new PlayerModel<>(event.getEntityModels().bakeLayer(ModelLayers.PLAYER_SLIM), true);

            slim.addLayer(new SkinLayerHandler(slim, castPlayerModel(slimModifierModel)));
            slim.addLayer(new SkinHighlightLayer(slim, castPlayerModel(slimHighlightModel)));
        }

        EntityRenderer<? extends ArmorStand> armorStandRenderer = event.getRenderer(EntityType.ARMOR_STAND);

        if (armorStandRenderer instanceof ArmorStandRenderer renderer) {
            renderer.addLayer(new ExosuitArmorStandLayer(renderer, event.getEntityModels()));
        }
    }

    @SuppressWarnings("unchecked")
    private static PlayerModel<net.minecraft.client.player.AbstractClientPlayer> castPlayerModel(PlayerModel<?> model) {
        return (PlayerModel<net.minecraft.client.player.AbstractClientPlayer>) model;
    }
}