package com.perigrine3.styleoversubstance.styleoversubstance.client;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.api.StyleClothingApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = Styleoversubstance.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public final class StyleVisualEntityResolver {

    private StyleVisualEntityResolver() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> StyleClothingApi.setVisualEntityResolver(StyleVisualEntityResolver::resolve));
    }

    private static LivingEntity resolve(LivingEntity entity) {
        if (!(entity instanceof AbstractClientPlayer clientPlayer)) return entity;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return entity;

        Player levelPlayer = minecraft.level.getPlayerByUUID(clientPlayer.getUUID());
        if (levelPlayer == null) return entity;

        return levelPlayer;
    }
}