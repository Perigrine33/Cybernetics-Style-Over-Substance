package com.perigrine3.styleoversubstance.styleoversubstance.client.skin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;

public final class SkinVanillaWearVisibility {

    private static final ThreadLocal<Integer> SUPPRESSION_DEPTH = ThreadLocal.withInitial(() -> 0);

    private SkinVanillaWearVisibility() {
    }

    public static void pushSuppress() {
        SUPPRESSION_DEPTH.set(SUPPRESSION_DEPTH.get() + 1);
    }

    public static void popSuppress() {
        int depth = SUPPRESSION_DEPTH.get();

        if (depth <= 1) {
            SUPPRESSION_DEPTH.remove();
        } else {
            SUPPRESSION_DEPTH.set(depth - 1);
        }
    }

    public static boolean isSuppressed() {
        return SUPPRESSION_DEPTH.get() > 0;
    }

    public static boolean isModelPartShownNow(Player player, PlayerModelPart part) {
        if (player == null || part == null) return false;
        return player.isModelPartShown(part);
    }
}