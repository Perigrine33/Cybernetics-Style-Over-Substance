package com.perigrine3.styleoversubstance.styleoversubstance.item;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.clothing.StyleClothingItem;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Styleoversubstance.MODID);


//BASIC ITEMS & COMPONENTS

//HEAD
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.register("example_item",
            () -> new StyleClothingItem(new Item.Properties()));

//NECKLACE
//BACK
//BANGLE
//BODY
//BUNDLE
//QUIVER
//HANDS
//RING
//BELT
//CHARM
//FEET


    public static <T extends Item> DeferredItem<T> registerIfLoaded(String modid, String name, Supplier<T> supplier) {
        if (ModList.get().isLoaded(modid)) {
            return ModItems.ITEMS.register(name, supplier);
        }
        return null;
    }

    public static <T extends Item> DeferredItem<T> registerIfNotLoaded(String modid, String name, Supplier<T> supplier) {
        if (!ModList.get().isLoaded(modid)) {
            return ModItems.ITEMS.register(name, supplier);
        }
        return null;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
