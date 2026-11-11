package com.perigrine3.styleoversubstance.styleoversubstance.compat.cybernetics;

import com.perigrine3.createcybernetics.api.client.ISandevistanSkinOverlayProvider;
import com.perigrine3.createcybernetics.api.client.SandevistanSkinOverlayApi;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinHighlight;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinModifier;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinModifierManager;
import com.perigrine3.styleoversubstance.styleoversubstance.client.skin.SkinModifierState;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Styleoversubstance.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class CyberneticsSandevistanCompat {

    private CyberneticsSandevistanCompat() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> SandevistanSkinOverlayApi.register(CyberneticsSandevistanCompat::captureOverlays));
    }

    private static List<ISandevistanSkinOverlayProvider.OverlayPass> captureOverlays(AbstractClientPlayer player) {
        List<ISandevistanSkinOverlayProvider.OverlayPass> passes = new ArrayList<>();

        SkinModifierState state = SkinModifierManager.getPlayerSkinState(player);
        if (state == null) return passes;

        PlayerSkin.Model modelType = player.getSkin().model();

        for (SkinModifier modifier : state.getModifiers()) {
            if (modifier == null) continue;

            passes.add(new ISandevistanSkinOverlayProvider.OverlayPass(modifier.getTexture(modelType), modifier.getColor(), false));

            if (modifier.hasGlint()) {
                passes.add(new ISandevistanSkinOverlayProvider.OverlayPass(modifier.getTexture(modelType), 0xFFFFFFFF, true));
            }
        }

        for (SkinHighlight highlight : state.getHighlights()) {
            if (highlight == null) continue;

            int color = highlight.isEmissive() && !highlight.tintOnEmissive() ? 0xFFFFFFFF : highlight.getColor();
            passes.add(new ISandevistanSkinOverlayProvider.OverlayPass(highlight.getTexture(modelType), color, highlight.isEmissive()));
        }

        return passes;
    }
}