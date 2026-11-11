package com.perigrine3.styleoversubstance.styleoversubstance.block.entity;

import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.util.ModTags;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.menu.GarmentTableMenu;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table.GarmentTableCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GarmentTableBlockEntity extends BlockEntity implements MenuProvider {
    public static final int CRAFTING_START = 0;
    public static final int CRAFTING_COUNT = 25;
    public static final int STRING_SLOT = 25;
    public static final int PRIMARY_DYE_SLOT = 26;
    public static final int SECONDARY_DYE_SLOT = 27;
    public static final int MODIFIER_START = 28;
    public static final int MODIFIER_COUNT = 4;
    public static final int OUTPUT_SLOT = 32;
    public static final int SLOT_COUNT = 33;

    private boolean updatingResult;
    private ItemStack displayedGarment = ItemStack.EMPTY;
    // Each displayed slot points to its original slot inside the garment, or -1 for a loose mod.
    private final int[] displayedModifiers = {-1, -1, -1, -1};
    private final ItemStack[] displayedStacks = new ItemStack[MODIFIER_COUNT];

    private final ItemStackHandler inventory = new ItemStackHandler(SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();

            if (!updatingResult && slot != OUTPUT_SLOT) {
                refreshDisplayedModifiers();
                GarmentTableCrafting.updateResult(GarmentTableBlockEntity.this);
            }
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == OUTPUT_SLOT) return false;
            if (slot == STRING_SLOT) return stack.is(Items.STRING);
            if (slot == PRIMARY_DYE_SLOT || slot == SECONDARY_DYE_SLOT) return stack.getItem() instanceof DyeItem;
            if (slot >= MODIFIER_START && slot < MODIFIER_START + MODIFIER_COUNT) return stack.is(ModTags.Items.CLOTHING_MODS);
            if (slot < CRAFTING_COUNT && GarmentData.isGarment(stack) && GarmentData.hasModifiers(stack)) {
                // Never accept a garment whose installed modules cannot all be displayed safely.
                if (!displayedGarment.isEmpty()) return false;
                long installed = GarmentData.getModifiers(stack).stream().filter(mod -> !mod.isEmpty()).count();
                int free = 0;
                for (int i = 0; i < MODIFIER_COUNT; i++) {
                    if (getStackInSlot(MODIFIER_START + i).isEmpty()) free++;
                }
                return installed <= free;
            }
            return true;
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot >= MODIFIER_START && slot < MODIFIER_START + MODIFIER_COUNT ? 1 : super.getSlotLimit(slot);
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return slot < CRAFTING_COUNT && GarmentData.isGarment(stack) ? 1 : super.getStackLimit(slot, stack);
        }
    };

    public GarmentTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GARMENT_TABLE.get(), pos, state);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public void setUpdatingResult(boolean updatingResult) {
        this.updatingResult = updatingResult;
    }

    public boolean hasDisplayedModifiers(ItemStack garment) {
        return !GarmentData.hasModifiers(garment) || displayedGarment == garment;
    }

    private void refreshDisplayedModifiers() {
        if (level == null || level.isClientSide || updatingResult) return;
        updatingResult = true;
        try {
            boolean stillPresent = false;
            for (int slot = 0; slot < CRAFTING_COUNT; slot++) {
                if (!displayedGarment.isEmpty() && inventory.getStackInSlot(slot) == displayedGarment) stillPresent = true;
            }
            if (stillPresent) {
                List<ItemStack> installed = GarmentData.getModifiers(displayedGarment);
                boolean changed = false;
                for (int i = 0; i < MODIFIER_COUNT; i++) {
                    int originalSlot = displayedModifiers[i];
                    if (originalSlot < 0) continue;
                    ItemStack current = inventory.getStackInSlot(MODIFIER_START + i);
                    if (current != displayedStacks[i] || !ItemStack.isSameItemSameComponents(installed.get(originalSlot), current)) {
                        // Taking or replacing a displayed module detaches it from the source garment.
                        installed.set(originalSlot, ItemStack.EMPTY);
                        displayedModifiers[i] = -1;
                        displayedStacks[i] = null;
                        changed = true;
                    }
                }
                if (changed) GarmentData.setModifiers(displayedGarment, installed);
                return;
            }

            // The source left the table; its attached modules leave with it.
            for (int i = 0; i < MODIFIER_COUNT; i++) {
                if (displayedModifiers[i] >= 0) inventory.setStackInSlot(MODIFIER_START + i, ItemStack.EMPTY);
            }
            Arrays.fill(displayedModifiers, -1);
            Arrays.fill(displayedStacks, null);
            displayedGarment = ItemStack.EMPTY;
            ItemStack candidate = ItemStack.EMPTY;
            for (int slot = 0; slot < CRAFTING_COUNT; slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (!GarmentData.isGarment(stack)) continue;
                if (!candidate.isEmpty()) return;
                candidate = stack;
            }
            if (candidate.isEmpty() || !GarmentData.hasModifiers(candidate)) return;
            List<ItemStack> installed = GarmentData.getModifiers(candidate);
            int free = 0;
            for (int i = 0; i < MODIFIER_COUNT; i++) {
                if (inventory.getStackInSlot(MODIFIER_START + i).isEmpty()) free++;
            }
            if (installed.stream().filter(mod -> !mod.isEmpty()).count() > free) return;
            displayedGarment = candidate;
            for (int originalSlot = 0; originalSlot < installed.size(); originalSlot++) {
                ItemStack module = installed.get(originalSlot);
                if (module.isEmpty()) continue;
                // Prefer the original position, then any free modifier slot.
                int target = originalSlot;
                if (!inventory.getStackInSlot(MODIFIER_START + target).isEmpty()) {
                    for (target = 0; target < MODIFIER_COUNT; target++) {
                        if (inventory.getStackInSlot(MODIFIER_START + target).isEmpty()) break;
                    }
                }
                displayedModifiers[target] = originalSlot;
                displayedStacks[target] = module.copyWithCount(1);
                inventory.setStackInSlot(MODIFIER_START + target, displayedStacks[target]);
            }
        } finally {
            updatingResult = false;
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.styleoversubstance.garment_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new GarmentTableMenu(containerId, playerInventory, this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        // Displayed modules are already saved on the garment; only loose modules are separate items.
        ItemStackHandler savedInventory = new ItemStackHandler(SLOT_COUNT);
        for (int slot = 0; slot < SLOT_COUNT; slot++) {
            if (slot >= MODIFIER_START && slot < MODIFIER_START + MODIFIER_COUNT
                    && displayedModifiers[slot - MODIFIER_START] >= 0) continue;
            savedInventory.setStackInSlot(slot, inventory.getStackInSlot(slot).copy());
        }
        tag.put("Inventory", savedInventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        displayedGarment = ItemStack.EMPTY;
        Arrays.fill(displayedModifiers, -1);
        Arrays.fill(displayedStacks, null);

        if (tag.contains("Inventory")) {
            inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        }
        if (level != null && !level.isClientSide) {
            refreshDisplayedModifiers();
            GarmentTableCrafting.updateResult(this);
        }
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);

        if (!level.isClientSide) {
            refreshDisplayedModifiers();
            GarmentTableCrafting.updateResult(this);
        }
    }

    public void drops() {
        if (level == null) return;

        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            if (slot == OUTPUT_SLOT) continue;
            if (slot >= MODIFIER_START && slot < MODIFIER_START + MODIFIER_COUNT
                    && displayedModifiers[slot - MODIFIER_START] >= 0) continue;

            ItemStack stack = inventory.getStackInSlot(slot);

            if (!stack.isEmpty()) {
                net.minecraft.world.Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack);
            }
        }
    }
}
