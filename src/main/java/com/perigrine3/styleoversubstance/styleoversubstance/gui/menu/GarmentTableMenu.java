package com.perigrine3.styleoversubstance.styleoversubstance.gui.menu;

import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.block.entity.GarmentTableBlockEntity;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.ModMenuTypes;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table.GarmentTableCrafting;
import com.perigrine3.styleoversubstance.styleoversubstance.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.SlotItemHandler;

public class GarmentTableMenu extends AbstractContainerMenu {
    public static final int CRAFTING_START = 0;
    public static final int CRAFTING_END = 25;

    public static final int STRING_SLOT = 25;
    public static final int PRIMARY_DYE_SLOT = 26;
    public static final int SECONDARY_DYE_SLOT = 27;

    public static final int MODIFIER_START = 28;
    public static final int MODIFIER_END = 32;

    public static final int OUTPUT_SLOT = 32;

    public static final int BLOCK_SLOT_COUNT = 33;
    public static final int PLAYER_INVENTORY_START = 33;
    public static final int PLAYER_INVENTORY_END = 60;
    public static final int HOTBAR_START = 60;
    public static final int HOTBAR_END = 69;

    private final GarmentTableBlockEntity blockEntity;
    private final BlockPos blockPos;

    public GarmentTableMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, getBlockEntity(playerInventory, buf.readBlockPos()));
    }

    public GarmentTableMenu(int containerId, Inventory playerInventory, GarmentTableBlockEntity blockEntity) {
        super(ModMenuTypes.GARMENT_TABLE.get(), containerId);
        this.blockEntity = blockEntity;
        this.blockPos = blockEntity.getBlockPos();

        addGarmentTableSlots();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private static GarmentTableBlockEntity getBlockEntity(Inventory playerInventory, BlockPos pos) {
        if (playerInventory.player.level().getBlockEntity(pos) instanceof GarmentTableBlockEntity blockEntity) {
            return blockEntity;
        }

        throw new IllegalStateException("Garment Table block entity was not present at " + pos);
    }

    private void addGarmentTableSlots() {
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                int slot = column + row * 5;
                addSlot(new SlotItemHandler(blockEntity.getInventory(), slot, 48 + column * 18, 29 + row * 18) {
                    @Override
                    public int getMaxStackSize(ItemStack stack) {
                        return GarmentData.isGarment(stack) ? 1 : super.getMaxStackSize(stack);
                    }
                });
            }
        }

        addSlot(new SlotItemHandler(blockEntity.getInventory(), STRING_SLOT, 147, 29) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.STRING);
            }
        });

        addSlot(new SlotItemHandler(blockEntity.getInventory(), PRIMARY_DYE_SLOT, 147, 56) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });

        addSlot(new SlotItemHandler(blockEntity.getInventory(), SECONDARY_DYE_SLOT, 165, 56) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof DyeItem;
            }
        });

        addSlot(new SlotItemHandler(blockEntity.getInventory(), 28, 147, 83));
        addSlot(new SlotItemHandler(blockEntity.getInventory(), 29, 165, 83));
        addSlot(new SlotItemHandler(blockEntity.getInventory(), 30, 147, 101));
        addSlot(new SlotItemHandler(blockEntity.getInventory(), 31, 165, 101));

        addSlot(new SlotItemHandler(blockEntity.getInventory(), OUTPUT_SLOT, 189, 65) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                GarmentTableCrafting.consumeInputs(blockEntity);
                super.onTake(player, stack);
            }
        });
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInventory, column + row * 9 + 9, 48 + column * 18, 126 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 48 + column * 18, 184));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);

        if (!slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index == OUTPUT_SLOT) {
            if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, HOTBAR_END, true)) return ItemStack.EMPTY;
        } else if (index < BLOCK_SLOT_COUNT) {
            if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, HOTBAR_END, false)) return ItemStack.EMPTY;
        } else if (stack.is(ModTags.Items.CLOTHING_MODS)) {
            // Vanilla stops after filling one empty slot, so repeat for our single-item slots.
            while (!stack.isEmpty() && moveItemStackTo(stack, MODIFIER_START, MODIFIER_END, false)) {
            }
        } else if (stack.is(Items.STRING)) {
            if (!moveItemStackTo(stack, STRING_SLOT, STRING_SLOT + 1, false)) {
                if (!moveItemStackTo(stack, CRAFTING_START, CRAFTING_END, false)) return ItemStack.EMPTY;
            }
        } else if (stack.getItem() instanceof DyeItem) {
            if (!moveItemStackTo(stack, PRIMARY_DYE_SLOT, SECONDARY_DYE_SLOT + 1, false)) {
                if (!moveItemStackTo(stack, CRAFTING_START, CRAFTING_END, false)) return ItemStack.EMPTY;
            }
        } else {
            if (!moveItemStackTo(stack, CRAFTING_START, CRAFTING_END, false)) return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) return ItemStack.EMPTY;

        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), blockPos), player, ModBlocks.GARMENT_TABLE.get());
    }

    public GarmentTableBlockEntity getBlockEntity() {
        return blockEntity;
    }
}
