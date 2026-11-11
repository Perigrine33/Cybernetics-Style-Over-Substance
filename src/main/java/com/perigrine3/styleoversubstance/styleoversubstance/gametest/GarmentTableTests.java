package com.perigrine3.styleoversubstance.styleoversubstance.gametest;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import com.perigrine3.styleoversubstance.styleoversubstance.block.entity.GarmentTableBlockEntity;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.menu.GarmentTableMenu;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table.GarmentTableCrafting;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

@GameTestHolder(Styleoversubstance.MODID)
@PrefixGameTestTemplate(false)
public final class GarmentTableTests {
    private static GarmentTableBlockEntity table(GameTestHelper helper) {
        BlockPos pos = new BlockPos(1, 1, 1);
        helper.setBlock(pos, ModBlocks.GARMENT_TABLE.get());
        return (GarmentTableBlockEntity) helper.getLevel().getBlockEntity(helper.absolutePos(pos));
    }

    private static ItemStack garment() {
        ItemStack garment = new ItemStack(ModItems.CAP.get());
        GarmentData.setModifiers(garment, List.of(new ItemStack(ModItems.MODULE_BATTERY.get())));
        return garment;
    }

    @GameTest(template = "garment_table_test")
    public static void preservesInstalledModulesWhenAdding(GameTestHelper helper) {
        GarmentTableBlockEntity table = table(helper);
        ItemStackHandler inventory = table.getInventory();
        inventory.setStackInSlot(0, garment());
        helper.assertTrue(inventory.getStackInSlot(28).is(ModItems.MODULE_BATTERY.get()), "Installed battery must populate the table");
        inventory.insertItem(29, new ItemStack(ModItems.MODULE_ARMOREDPLATING.get()), false);
        inventory.setStackInSlot(25, new ItemStack(Items.STRING));
        ItemStack result = inventory.getStackInSlot(32).copy();
        helper.assertTrue(GarmentData.getModifiers(result).get(0).is(ModItems.MODULE_BATTERY.get()), "Output must keep the installed battery");
        helper.assertTrue(GarmentData.getModifiers(result).get(1).is(ModItems.MODULE_ARMOREDPLATING.get()), "Output must include the new plate");
        GarmentTableCrafting.consumeInputs(table);
        helper.assertTrue(inventory.getStackInSlot(0).isEmpty() && inventory.getStackInSlot(28).isEmpty()
                && inventory.getStackInSlot(29).isEmpty(), "Taking output must consume input and leave no duplicate modules");
        helper.succeed();
    }

    @GameTest(template = "garment_table_test")
    public static void removingGarmentKeepsOriginalModules(GameTestHelper helper) {
        GarmentTableBlockEntity table = table(helper);
        ItemStackHandler inventory = table.getInventory();
        inventory.setStackInSlot(0, garment());
        inventory.insertItem(29, new ItemStack(ModItems.MODULE_LEDS.get()), false);
        ItemStack removed = inventory.extractItem(0, 1, false);
        helper.assertTrue(GarmentData.getModifiers(removed).get(0).is(ModItems.MODULE_BATTERY.get()), "Removed garment must retain original modules");
        helper.assertTrue(inventory.getStackInSlot(28).isEmpty(), "Displayed module must leave with its garment");
        helper.assertTrue(inventory.getStackInSlot(29).is(ModItems.MODULE_LEDS.get()), "Uninstalled module must stay in the table");
        helper.succeed();
    }

    @GameTest(template = "garment_table_test")
    public static void swappingIdenticalModuleDoesNotDuplicate(GameTestHelper helper) {
        GarmentTableBlockEntity table = table(helper);
        ItemStackHandler inventory = table.getInventory();
        inventory.setStackInSlot(0, garment());
        inventory.setStackInSlot(28, new ItemStack(ModItems.MODULE_BATTERY.get()));
        ItemStack removed = inventory.extractItem(0, 1, false);
        helper.assertTrue(!GarmentData.hasModifiers(removed), "Replacing a displayed module must detach the original even with an identical item");
        helper.assertTrue(inventory.getStackInSlot(28).is(ModItems.MODULE_BATTERY.get()), "Replacement must remain as a loose module");
        helper.succeed();
    }

    @GameTest(template = "garment_table_test")
    public static void savesOnlyOneCopyOfInstalledModules(GameTestHelper helper) {
        GarmentTableBlockEntity table = table(helper);
        table.getInventory().setStackInSlot(0, garment());
        table.getInventory().insertItem(29, new ItemStack(ModItems.MODULE_LEDS.get()), false);
        ItemStackHandler saved = new ItemStackHandler();
        saved.deserializeNBT(helper.getLevel().registryAccess(), table.saveWithFullMetadata(helper.getLevel().registryAccess()).getCompound("Inventory"));
        helper.assertTrue(saved.getStackInSlot(28).isEmpty(), "Displayed modules must not be saved twice");
        helper.assertTrue(GarmentData.hasModifiers(saved.getStackInSlot(0)), "Installed modules must be saved on the garment");
        helper.assertTrue(saved.getStackInSlot(29).is(ModItems.MODULE_LEDS.get()), "Loose modules must be saved separately");
        GarmentTableBlockEntity reloaded = new GarmentTableBlockEntity(table.getBlockPos(), table.getBlockState());
        reloaded.loadWithComponents(table.saveWithFullMetadata(helper.getLevel().registryAccess()), helper.getLevel().registryAccess());
        reloaded.setLevel(helper.getLevel());
        helper.assertTrue(reloaded.getInventory().getStackInSlot(28).is(ModItems.MODULE_BATTERY.get()), "Reload must restore the display");
        helper.succeed();
    }

    @GameTest(template = "garment_table_test")
    public static void shiftClickRoutesTaggedModifiers(GameTestHelper helper) {
        GarmentTableBlockEntity table = table(helper);
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(9, new ItemStack(ModItems.MODULE_BATTERY.get(), 5));
        GarmentTableMenu menu = new GarmentTableMenu(0, player.getInventory(), table);
        menu.quickMoveStack(player, GarmentTableMenu.PLAYER_INVENTORY_START);
        for (int slot = 28; slot < 32; slot++) {
            helper.assertTrue(table.getInventory().getStackInSlot(slot).getCount() == 1, "Shift-click must fill each modifier slot with one module");
        }
        helper.assertTrue(player.getInventory().getItem(9).getCount() == 1, "Excess modules must stay in the player inventory");
        helper.assertTrue(table.getInventory().getStackInSlot(0).isEmpty(), "Modifiers must not spill into the crafting grid");
        helper.succeed();
    }
}
