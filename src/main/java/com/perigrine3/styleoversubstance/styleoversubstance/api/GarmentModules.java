package com.perigrine3.styleoversubstance.styleoversubstance.api;

import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class GarmentModules {
    public static final int BATTERY_CAPACITY = 2500;
    public static final double ARMOR_PER_PLATE = 2.0;
    public static final double TOUGHNESS_PER_PLATE = 2.0;
    public static final int FIRE_PROTECTION_PERCENT = 7;
    public static final int FARADAY_PERCENT = 10;

    private GarmentModules() {}

    public static boolean isModule(ItemStack stack) {
        return stack.is(ModItems.MODULE_LEDS.get()) || stack.is(ModItems.MODULE_BATTERY.get())
                || stack.is(ModItems.MODULE_ARMOREDPLATING.get()) || stack.is(ModItems.MODULE_FIREPROOFGEL.get())
                || stack.is(ModItems.MODULE_FARADAYTHREADING.get());
    }

    public static int countInstalled(ItemStack garment, Item module) {
        if (!GarmentData.isGarment(garment)) return 0;
        return (int) GarmentData.getModifiers(garment).stream().filter(stack -> stack.is(module)).count();
    }

    public static int countWorn(LivingEntity entity, Item module) {
        return countWorn(entity, module, null);
    }

    public static int countWorn(LivingEntity entity, Item module, String slotIdentifier) {
        // Query the actual wearer's functional inventory, never a visual proxy or cosmetic slots.
        return CuriosApi.getCuriosInventory(entity).map(handler -> handler.findCurios(GarmentData::isGarment)
                .stream().filter(result -> !result.slotContext().cosmetic())
                .filter(result -> slotIdentifier == null || slotIdentifier.equals(result.slotContext().identifier()))
                .mapToInt(result -> countInstalled(result.stack(), module)).sum()).orElse(0);
    }

    public static int extraEnergyCapacity(LivingEntity entity) {
        return countWorn(entity, ModItems.MODULE_BATTERY.get()) * BATTERY_CAPACITY;
    }

    public static boolean roll(LivingEntity entity, int modules, int percentPerModule) {
        return modules > 0 && entity.getRandom().nextInt(100) < Math.min(100L, (long) modules * percentPerModule);
    }

    public static boolean blocksOutgoingQuickhack(LivingEntity entity) {
        return roll(entity, countWorn(entity, ModItems.MODULE_FARADAYTHREADING.get(), "head"), FARADAY_PERCENT);
    }
}
