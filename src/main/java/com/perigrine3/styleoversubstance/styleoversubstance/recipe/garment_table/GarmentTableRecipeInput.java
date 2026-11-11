package com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record GarmentTableRecipeInput(List<ItemStack> craftingGrid) implements RecipeInput {
    public static final int GRID_WIDTH = 5;
    public static final int GRID_HEIGHT = 5;
    public static final int GRID_SIZE = GRID_WIDTH * GRID_HEIGHT;

    public GarmentTableRecipeInput {
        if (craftingGrid.size() != GRID_SIZE) {
            throw new IllegalArgumentException("Garment Table input must contain exactly 25 stacks");
        }
    }

    @Override
    public ItemStack getItem(int index) {
        if (index < 0 || index >= GRID_SIZE) return ItemStack.EMPTY;
        return craftingGrid.get(index);
    }

    public ItemStack getItem(int column, int row) {
        if (column < 0 || column >= GRID_WIDTH) return ItemStack.EMPTY;
        if (row < 0 || row >= GRID_HEIGHT) return ItemStack.EMPTY;
        return getItem(column + row * GRID_WIDTH);
    }

    @Override
    public int size() {
        return GRID_SIZE;
    }
}