package com.perigrine3.styleoversubstance.styleoversubstance.gui.screen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.menu.GarmentTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class GarmentTableScreen extends AbstractContainerScreen<GarmentTableMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/gui/garment_table/garment_gui.png");

    public GarmentTableScreen(GarmentTableMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 256;
        this.imageHeight = 256;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos - 36, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
        if (hoveredSlot != null && !hoveredSlot.hasItem() && menu.getCarried().isEmpty()) {
            String tooltipKey = switch (hoveredSlot.index) {
                case GarmentTableMenu.STRING_SLOT -> "tooltip.styleoversubstance.garment_table.thread";
                case GarmentTableMenu.PRIMARY_DYE_SLOT -> "tooltip.styleoversubstance.garment_table.primary_dye";
                case GarmentTableMenu.SECONDARY_DYE_SLOT -> "tooltip.styleoversubstance.garment_table.secondary_dye";
                default -> hoveredSlot.index >= GarmentTableMenu.MODIFIER_START
                        && hoveredSlot.index < GarmentTableMenu.MODIFIER_END
                        ? "tooltip.styleoversubstance.garment_table.modifier" : null;
            };
            if (tooltipKey != null) {
                guiGraphics.renderTooltip(font, Component.translatable(tooltipKey), mouseX, mouseY);
            }
        }
    }
}
