package com.perigrine3.styleoversubstance.styleoversubstance.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;

public final class StyleVisualPlayerResolver {

    private StyleVisualPlayerResolver() {}

    public static Player resolve(Player player) {
        if (!(player instanceof AbstractClientPlayer clientPlayer)) return player;

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return player;

        Player resolved = minecraft.level.getPlayerByUUID(clientPlayer.getUUID());

        if (resolved == null || resolved == player) {
            return player;
        }

        return resolved;
    }
}