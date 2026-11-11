package com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table;

import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.api.StyleClothingColorHelper;
import com.perigrine3.styleoversubstance.styleoversubstance.block.entity.GarmentTableBlockEntity;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.ModRecipes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class GarmentTableCrafting {

    private GarmentTableCrafting() {
    }

    public static void updateResult(GarmentTableBlockEntity blockEntity) {
        if (!(blockEntity.getLevel() instanceof ServerLevel level)) return;

        ItemStack result = createResult(blockEntity, level);

        blockEntity.setUpdatingResult(true);

        try {
            blockEntity.getInventory().setStackInSlot(GarmentTableBlockEntity.OUTPUT_SLOT, result);
        } finally {
            blockEntity.setUpdatingResult(false);
        }
    }

    private static ItemStack createResult(GarmentTableBlockEntity blockEntity, ServerLevel level) {
        ItemStack existingGarment = findExistingGarment(blockEntity);

        if (!existingGarment.isEmpty()) {
            return createExistingGarmentResult(blockEntity, existingGarment);
        }

        return createConstructionResult(blockEntity, level);
    }

    private static ItemStack createConstructionResult(GarmentTableBlockEntity blockEntity, ServerLevel level) {
        ItemStackHandler inventory = blockEntity.getInventory();

        if (!inventory.getStackInSlot(GarmentTableBlockEntity.STRING_SLOT).is(Items.STRING)) return ItemStack.EMPTY;

        GarmentTableRecipeInput input = createRecipeInput(blockEntity);
        Optional<RecipeHolder<GarmentTableRecipe>> recipe = level.getRecipeManager().getRecipeFor(ModRecipes.GARMENT_TABLE_TYPE.get(), input, level);

        if (recipe.isEmpty()) return ItemStack.EMPTY;

        ItemStack result = recipe.get().value().assemble(input, level.registryAccess());

        if (!GarmentData.isGarment(result)) return ItemStack.EMPTY;

        GarmentData.applyPrimaryDye(result, inventory.getStackInSlot(GarmentTableBlockEntity.PRIMARY_DYE_SLOT));
        GarmentData.applySecondaryDye(result, inventory.getStackInSlot(GarmentTableBlockEntity.SECONDARY_DYE_SLOT));
        StyleClothingColorHelper.applyPrimaryDye(result, inventory.getStackInSlot(GarmentTableBlockEntity.PRIMARY_DYE_SLOT));
        StyleClothingColorHelper.applySecondaryDye(result, inventory.getStackInSlot(GarmentTableBlockEntity.SECONDARY_DYE_SLOT));

        if (hasAnyModifiers(blockEntity)) {
            GarmentData.setModifiers(result, getModifierStacks(blockEntity));
        }

        return result;
    }

    private static ItemStack createExistingGarmentResult(GarmentTableBlockEntity blockEntity, ItemStack existingGarment) {
        if (!blockEntity.hasDisplayedModifiers(existingGarment)) return ItemStack.EMPTY;
        ItemStackHandler inventory = blockEntity.getInventory();
        ItemStack primaryDye = inventory.getStackInSlot(GarmentTableBlockEntity.PRIMARY_DYE_SLOT);
        ItemStack secondaryDye = inventory.getStackInSlot(GarmentTableBlockEntity.SECONDARY_DYE_SLOT);
        boolean hasPrimaryDye = primaryDye.getItem() instanceof DyeItem;
        boolean hasSecondaryDye = secondaryDye.getItem() instanceof DyeItem;
        boolean modifiersChanged = modifiersDiffer(existingGarment, getModifierStacks(blockEntity));

        if (!hasPrimaryDye && !hasSecondaryDye && !modifiersChanged) return ItemStack.EMPTY;
        if (modifiersChanged && !inventory.getStackInSlot(GarmentTableBlockEntity.STRING_SLOT).is(Items.STRING)) return ItemStack.EMPTY;

        ItemStack result = existingGarment.copyWithCount(1);

        GarmentData.applyPrimaryDye(result, primaryDye);
        GarmentData.applySecondaryDye(result, secondaryDye);
        StyleClothingColorHelper.applyPrimaryDye(result, primaryDye);
        StyleClothingColorHelper.applySecondaryDye(result, secondaryDye);

        if (modifiersChanged) {
            GarmentData.setModifiers(result, getModifierStacks(blockEntity));
        }

        return result;
    }

    public static void consumeInputs(GarmentTableBlockEntity blockEntity) {
        if (!(blockEntity.getLevel() instanceof ServerLevel)) return;

        ItemStack existingGarment = findExistingGarment(blockEntity);

        if (existingGarment.isEmpty()) {
            consumeConstructionInputs(blockEntity);
        } else {
            consumeExistingGarmentInputs(blockEntity, existingGarment);
        }

        updateResult(blockEntity);
    }

    private static void consumeConstructionInputs(GarmentTableBlockEntity blockEntity) {
        ItemStackHandler inventory = blockEntity.getInventory();

        for (int slot = GarmentTableBlockEntity.CRAFTING_START; slot < GarmentTableBlockEntity.CRAFTING_START + GarmentTableBlockEntity.CRAFTING_COUNT; slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                inventory.extractItem(slot, 1, false);
            }
        }

        inventory.extractItem(GarmentTableBlockEntity.STRING_SLOT, 1, false);
        consumeDyes(inventory);
        consumeModifiers(inventory);
    }

    private static void consumeExistingGarmentInputs(GarmentTableBlockEntity blockEntity, ItemStack existingGarment) {
        ItemStackHandler inventory = blockEntity.getInventory();
        boolean modifiersChanged = modifiersDiffer(existingGarment, getModifierStacks(blockEntity));

        for (int slot = GarmentTableBlockEntity.CRAFTING_START; slot < GarmentTableBlockEntity.CRAFTING_START + GarmentTableBlockEntity.CRAFTING_COUNT; slot++) {
            if (GarmentData.isGarment(inventory.getStackInSlot(slot))) {
                inventory.extractItem(slot, 1, false);
                break;
            }
        }

        consumeDyes(inventory);

        if (modifiersChanged) {
            inventory.extractItem(GarmentTableBlockEntity.STRING_SLOT, 1, false);
            consumeModifiers(inventory);
        }
    }

    private static void consumeDyes(ItemStackHandler inventory) {
        if (inventory.getStackInSlot(GarmentTableBlockEntity.PRIMARY_DYE_SLOT).getItem() instanceof DyeItem) {
            inventory.extractItem(GarmentTableBlockEntity.PRIMARY_DYE_SLOT, 1, false);
        }

        if (inventory.getStackInSlot(GarmentTableBlockEntity.SECONDARY_DYE_SLOT).getItem() instanceof DyeItem) {
            inventory.extractItem(GarmentTableBlockEntity.SECONDARY_DYE_SLOT, 1, false);
        }
    }

    private static void consumeModifiers(ItemStackHandler inventory) {
        for (int slot = GarmentTableBlockEntity.MODIFIER_START; slot < GarmentTableBlockEntity.MODIFIER_START + GarmentTableBlockEntity.MODIFIER_COUNT; slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) {
                inventory.extractItem(slot, 1, false);
            }
        }
    }

    private static boolean hasAnyModifiers(GarmentTableBlockEntity blockEntity) {
        for (int slot = GarmentTableBlockEntity.MODIFIER_START; slot < GarmentTableBlockEntity.MODIFIER_START + GarmentTableBlockEntity.MODIFIER_COUNT; slot++) {
            if (!blockEntity.getInventory().getStackInSlot(slot).isEmpty()) return true;
        }

        return false;
    }

    private static boolean modifiersDiffer(ItemStack garment, List<ItemStack> tableModifiers) {
        List<ItemStack> installedModifiers = GarmentData.getModifiers(garment);

        for (int slot = 0; slot < GarmentTableBlockEntity.MODIFIER_COUNT; slot++) {
            ItemStack installed = slot < installedModifiers.size() ? installedModifiers.get(slot) : ItemStack.EMPTY;
            ItemStack table = slot < tableModifiers.size() ? tableModifiers.get(slot) : ItemStack.EMPTY;

            if (!ItemStack.isSameItemSameComponents(installed, table)) return true;
        }

        return false;
    }

    private static ItemStack findExistingGarment(GarmentTableBlockEntity blockEntity) {
        ItemStack foundGarment = ItemStack.EMPTY;

        for (int slot = GarmentTableBlockEntity.CRAFTING_START; slot < GarmentTableBlockEntity.CRAFTING_START + GarmentTableBlockEntity.CRAFTING_COUNT; slot++) {
            ItemStack stack = blockEntity.getInventory().getStackInSlot(slot);

            if (stack.isEmpty()) continue;
            if (!GarmentData.isGarment(stack)) return ItemStack.EMPTY;
            if (!foundGarment.isEmpty()) return ItemStack.EMPTY;

            foundGarment = stack;
        }

        return foundGarment;
    }

    private static GarmentTableRecipeInput createRecipeInput(GarmentTableBlockEntity blockEntity) {
        List<ItemStack> craftingGrid = new ArrayList<>(GarmentTableRecipeInput.GRID_SIZE);

        for (int slot = GarmentTableBlockEntity.CRAFTING_START; slot < GarmentTableBlockEntity.CRAFTING_START + GarmentTableBlockEntity.CRAFTING_COUNT; slot++) {
            craftingGrid.add(blockEntity.getInventory().getStackInSlot(slot).copy());
        }

        return new GarmentTableRecipeInput(craftingGrid);
    }

    private static List<ItemStack> getModifierStacks(GarmentTableBlockEntity blockEntity) {
        List<ItemStack> modifiers = new ArrayList<>(GarmentTableBlockEntity.MODIFIER_COUNT);

        for (int slot = GarmentTableBlockEntity.MODIFIER_START; slot < GarmentTableBlockEntity.MODIFIER_START + GarmentTableBlockEntity.MODIFIER_COUNT; slot++) {
            ItemStack stack = blockEntity.getInventory().getStackInSlot(slot);
            modifiers.add(stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1));
        }

        return modifiers;
    }
}
