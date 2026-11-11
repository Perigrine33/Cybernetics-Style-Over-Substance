package com.perigrine3.styleoversubstance.styleoversubstance.api;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public final class StyleClothingColorHelper {

    private static final String COLOR_DATA = "StyleClothingColors";
    private static final String PRIMARY_COLOR = "PrimaryColor";
    private static final String SECONDARY_COLOR = "SecondaryColor";

    private static final int DEFAULT_COLOR = 0xFFFFFFFF;

    private StyleClothingColorHelper() {
    }

    public static boolean hasPrimaryColor(ItemStack stack) {
        CompoundTag colors = getColorData(stack);
        return colors != null && colors.contains(PRIMARY_COLOR, CompoundTag.TAG_INT);
    }

    public static boolean hasSecondaryColor(ItemStack stack) {
        CompoundTag colors = getColorData(stack);
        return colors != null && colors.contains(SECONDARY_COLOR, CompoundTag.TAG_INT);
    }

    public static boolean hasAnyColor(ItemStack stack) {
        return hasPrimaryColor(stack) || hasSecondaryColor(stack);
    }

    public static boolean hasBothColors(ItemStack stack) {
        return hasPrimaryColor(stack) && hasSecondaryColor(stack);
    }

    public static int getPrimaryColor(ItemStack stack) {
        CompoundTag colors = getColorData(stack);
        if (colors == null || !colors.contains(PRIMARY_COLOR, CompoundTag.TAG_INT)) return DEFAULT_COLOR;
        return normalizeColor(colors.getInt(PRIMARY_COLOR));
    }

    public static int getSecondaryColor(ItemStack stack) {
        CompoundTag colors = getColorData(stack);
        if (colors == null || !colors.contains(SECONDARY_COLOR, CompoundTag.TAG_INT)) return DEFAULT_COLOR;
        return normalizeColor(colors.getInt(SECONDARY_COLOR));
    }

    public static int getPrimaryColor(ItemStack stack, int defaultColor) {
        CompoundTag colors = getColorData(stack);
        if (colors == null || !colors.contains(PRIMARY_COLOR, CompoundTag.TAG_INT)) return normalizeColor(defaultColor);
        return normalizeColor(colors.getInt(PRIMARY_COLOR));
    }

    public static int getSecondaryColor(ItemStack stack, int defaultColor) {
        CompoundTag colors = getColorData(stack);
        if (colors == null || !colors.contains(SECONDARY_COLOR, CompoundTag.TAG_INT)) return normalizeColor(defaultColor);
        return normalizeColor(colors.getInt(SECONDARY_COLOR));
    }

    public static void applyPrimaryDye(ItemStack stack, ItemStack dyeStack) {
        if (stack == null || stack.isEmpty()) return;
        if (dyeStack == null || dyeStack.isEmpty()) return;
        if (!(dyeStack.getItem() instanceof DyeItem dyeItem)) return;

        setPrimaryColor(stack, dyeItem.getDyeColor().getTextureDiffuseColor());
    }

    public static void applySecondaryDye(ItemStack stack, ItemStack dyeStack) {
        if (stack == null || stack.isEmpty()) return;
        if (dyeStack == null || dyeStack.isEmpty()) return;
        if (!(dyeStack.getItem() instanceof DyeItem dyeItem)) return;

        setSecondaryColor(stack, dyeItem.getDyeColor().getTextureDiffuseColor());
    }

    public static void setPrimaryColor(ItemStack stack, int color) {
        setColor(stack, PRIMARY_COLOR, color);
    }

    public static void setSecondaryColor(ItemStack stack, int color) {
        setColor(stack, SECONDARY_COLOR, color);
    }

    public static void setColors(ItemStack stack, int primaryColor, int secondaryColor) {
        if (stack == null || stack.isEmpty()) return;

        CompoundTag root = getRootDataCopy(stack);
        CompoundTag colors = root.contains(COLOR_DATA, CompoundTag.TAG_COMPOUND) ? root.getCompound(COLOR_DATA) : new CompoundTag();

        colors.putInt(PRIMARY_COLOR, normalizeColor(primaryColor));
        colors.putInt(SECONDARY_COLOR, normalizeColor(secondaryColor));
        root.put(COLOR_DATA, colors);

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    public static void removePrimaryColor(ItemStack stack) {
        removeColor(stack, PRIMARY_COLOR);
    }

    public static void removeSecondaryColor(ItemStack stack) {
        removeColor(stack, SECONDARY_COLOR);
    }

    public static void clearColors(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        CompoundTag root = getRootDataCopy(stack);
        root.remove(COLOR_DATA);

        writeRootData(stack, root);
    }

    public static void copyColors(ItemStack source, ItemStack target) {
        if (source == null || source.isEmpty()) return;
        if (target == null || target.isEmpty()) return;

        CompoundTag sourceColors = getColorData(source);

        if (sourceColors == null || sourceColors.isEmpty()) {
            clearColors(target);
            return;
        }

        CompoundTag root = getRootDataCopy(target);
        root.put(COLOR_DATA, sourceColors.copy());

        stackSetCustomData(target, root);
    }

    private static void setColor(ItemStack stack, String key, int color) {
        if (stack == null || stack.isEmpty()) return;

        CompoundTag root = getRootDataCopy(stack);
        CompoundTag colors = root.contains(COLOR_DATA, CompoundTag.TAG_COMPOUND) ? root.getCompound(COLOR_DATA) : new CompoundTag();

        colors.putInt(key, normalizeColor(color));
        root.put(COLOR_DATA, colors);

        stackSetCustomData(stack, root);
    }

    private static void removeColor(ItemStack stack, String key) {
        if (stack == null || stack.isEmpty()) return;

        CompoundTag root = getRootDataCopy(stack);
        if (!root.contains(COLOR_DATA, CompoundTag.TAG_COMPOUND)) return;

        CompoundTag colors = root.getCompound(COLOR_DATA);
        colors.remove(key);

        if (colors.isEmpty()) {
            root.remove(COLOR_DATA);
        } else {
            root.put(COLOR_DATA, colors);
        }

        writeRootData(stack, root);
    }

    private static CompoundTag getColorData(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;

        CompoundTag root = customData.copyTag();
        if (!root.contains(COLOR_DATA, CompoundTag.TAG_COMPOUND)) return null;

        return root.getCompound(COLOR_DATA);
    }

    private static CompoundTag getRootDataCopy(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return new CompoundTag();

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData == null ? new CompoundTag() : customData.copyTag();
    }

    private static void writeRootData(ItemStack stack, CompoundTag root) {
        if (stack == null || stack.isEmpty()) return;

        if (root.isEmpty()) {
            stack.remove(DataComponents.CUSTOM_DATA);
        } else {
            stackSetCustomData(stack, root);
        }
    }

    private static void stackSetCustomData(ItemStack stack, CompoundTag root) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    private static int normalizeColor(int color) {
        if (FastColor.ARGB32.alpha(color) == 0) {
            return FastColor.ARGB32.color(255, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color));
        }

        return color;
    }
}