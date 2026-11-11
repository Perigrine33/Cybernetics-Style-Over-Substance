package com.perigrine3.styleoversubstance.styleoversubstance.api;

import com.perigrine3.styleoversubstance.styleoversubstance.component.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.List;

public final class GarmentData {
    public static final int MODIFIER_SLOT_COUNT = 4;

    private GarmentData() {
    }

    public static boolean isGarment(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IStyleClothingItem;
    }

    public static void applyPrimaryDye(ItemStack garment, ItemStack dyeStack) {
        if (!(dyeStack.getItem() instanceof DyeItem dyeItem)) return;
        garment.set(DataComponents.DYED_COLOR, new DyedItemColor(dyeItem.getDyeColor().getTextureDiffuseColor(), true));
    }

    public static void applySecondaryDye(ItemStack garment, ItemStack dyeStack) {
        if (!(dyeStack.getItem() instanceof DyeItem dyeItem)) return;
        garment.set(ModDataComponents.SECONDARY_DYE_COLOR.get(), new DyedItemColor(dyeItem.getDyeColor().getTextureDiffuseColor(), true));
    }

    public static boolean hasPrimaryColor(ItemStack garment) {
        return garment.has(DataComponents.DYED_COLOR);
    }

    public static boolean hasSecondaryColor(ItemStack garment) {
        return garment.has(ModDataComponents.SECONDARY_DYE_COLOR.get());
    }

    public static int getPrimaryColor(ItemStack garment, int fallbackColor) {
        DyedItemColor color = garment.get(DataComponents.DYED_COLOR);
        return color != null ? toOpaqueArgb(color.rgb()) : normalizeRenderColor(fallbackColor);
    }

    public static int getSecondaryColor(ItemStack garment, int fallbackColor) {
        DyedItemColor color = garment.get(ModDataComponents.SECONDARY_DYE_COLOR.get());
        return color != null ? toOpaqueArgb(color.rgb()) : normalizeRenderColor(fallbackColor);
    }

    public static List<ItemStack> getModifiers(ItemStack garment) {
        NonNullList<ItemStack> modifiers = NonNullList.withSize(MODIFIER_SLOT_COUNT, ItemStack.EMPTY);
        ItemContainerContents contents = garment.getOrDefault(ModDataComponents.GARMENT_MODIFIERS.get(), ItemContainerContents.EMPTY);

        for (int slot = 0; slot < Math.min(contents.getSlots(), MODIFIER_SLOT_COUNT); slot++) {
            modifiers.set(slot, contents.getStackInSlot(slot));
        }

        return modifiers;
    }

    public static void setModifiers(ItemStack garment, List<ItemStack> modifiers) {
        NonNullList<ItemStack> storedModifiers = NonNullList.withSize(MODIFIER_SLOT_COUNT, ItemStack.EMPTY);

        for (int slot = 0; slot < Math.min(modifiers.size(), MODIFIER_SLOT_COUNT); slot++) {
            ItemStack modifier = modifiers.get(slot);
            storedModifiers.set(slot, modifier.isEmpty() ? ItemStack.EMPTY : modifier.copyWithCount(1));
        }

        boolean empty = storedModifiers.stream().allMatch(ItemStack::isEmpty);

        if (empty) {
            garment.remove(ModDataComponents.GARMENT_MODIFIERS.get());
        } else {
            garment.set(ModDataComponents.GARMENT_MODIFIERS.get(), ItemContainerContents.fromItems(storedModifiers));
        }
    }

    public static boolean hasModifiers(ItemStack garment) {
        return getModifiers(garment).stream().anyMatch(stack -> !stack.isEmpty());
    }

    private static int toOpaqueArgb(int rgb) {
        return FastColor.ARGB32.color(255, FastColor.ARGB32.red(rgb), FastColor.ARGB32.green(rgb), FastColor.ARGB32.blue(rgb));
    }

    private static int normalizeRenderColor(int color) {
        if (FastColor.ARGB32.alpha(color) != 0) return color;
        return toOpaqueArgb(color);
    }
}