package com.perigrine3.styleoversubstance.styleoversubstance.client.model;

import net.minecraft.resources.ResourceLocation;

public record AttachmentRenderLayer(ResourceLocation texture, int color, RenderMode renderMode) {

    public AttachmentRenderLayer(ResourceLocation texture, int color) {
        this(texture, color, RenderMode.NORMAL);
    }

    public AttachmentRenderLayer {
        if (texture == null) {
            throw new IllegalArgumentException("Attachment render-layer texture cannot be null");
        }

        if (renderMode == null) {
            throw new IllegalArgumentException("Attachment render-layer mode cannot be null");
        }
    }

    public enum RenderMode {
        NORMAL,
        FULL_BRIGHT,
        EMISSIVE,
        STRONG_EMISSIVE
    }
}