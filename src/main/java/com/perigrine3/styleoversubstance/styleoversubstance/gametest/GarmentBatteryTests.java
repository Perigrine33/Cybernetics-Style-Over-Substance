package com.perigrine3.styleoversubstance.styleoversubstance.gametest;

import com.perigrine3.createcybernetics.common.capabilities.ModAttachments;
import com.perigrine3.createcybernetics.common.capabilities.PlayerCyberwareData;
import com.perigrine3.createcybernetics.common.energy.EnergyController;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import top.theillusivec4.curios.api.CuriosApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

@GameTestHolder(Styleoversubstance.MODID)
@PrefixGameTestTemplate(false)
@SuppressWarnings("unused") // GameTest discovers this class and its test methods through annotations.
public final class GarmentBatteryTests {
    private static Player wearer(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack garment = new ItemStack(ModItems.CAP.get());
        GarmentData.setModifiers(garment, List.of(new ItemStack(ModItems.MODULE_BATTERY.get())));
        CuriosApi.getCuriosInventory(player).orElseThrow().getStacksHandler("head").orElseThrow()
                .getStacks().setStackInSlot(0, garment);
        return player;
    }

    private static void depositSurplus(Player player, PlayerCyberwareData data, int amount) throws ReflectiveOperationException {
        // Exercise the real controller's deposit path without depending on a generator's environment.
        Class<?> poolType = Class.forName(EnergyController.class.getName() + "$MutableInt");
        Constructor<?> constructor = poolType.getDeclaredConstructor(int.class);
        constructor.setAccessible(true);
        Object pool = constructor.newInstance(amount);
        Method deposit = EnergyController.class.getDeclaredMethod("depositGeneratedSurplus", Player.class, PlayerCyberwareData.class, poolType);
        deposit.setAccessible(true);
        deposit.invoke(null, player, data, pool);
    }

    @GameTest(template = "garment_table_test")
    public static void chargesWithoutImplantedBattery(GameTestHelper helper) throws ReflectiveOperationException {
        Player player = wearer(helper);
        PlayerCyberwareData data = player.getData(ModAttachments.CYBERWARE);
        data.setEnergyStored(player, 0);
        helper.assertTrue(data.getTotalEnergyCapacity(player) == 2500, "Garment battery must supply 2500 capacity without implants");
        depositSurplus(player, data, 100);
        helper.assertTrue(data.getEnergyStored() == 100, "Garment battery must accept generated surplus without an implanted battery");
        depositSurplus(player, data, 5000);
        helper.assertTrue(data.getEnergyStored() == 2500, "Charging must respect garment battery capacity");
        CuriosApi.getCuriosInventory(player).orElseThrow().getStacksHandler("head").orElseThrow()
                .getStacks().setStackInSlot(0, ItemStack.EMPTY);
        data.clampEnergyToCapacity(player);
        depositSurplus(player, data, 100);
        helper.assertTrue(data.getEnergyStored() == 0, "An unequipped battery must not supply storage");
        helper.succeed();
    }
}
