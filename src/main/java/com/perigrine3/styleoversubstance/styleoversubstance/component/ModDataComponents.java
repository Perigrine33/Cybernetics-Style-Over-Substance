package com.perigrine3.styleoversubstance.styleoversubstance.component;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Styleoversubstance.MODID);

    public static final Supplier<DataComponentType<DyedItemColor>> SECONDARY_DYE_COLOR = DATA_COMPONENTS.registerComponentType("secondary_dye_color",
            builder -> builder.persistent(DyedItemColor.CODEC).networkSynchronized(DyedItemColor.STREAM_CODEC));

    public static final Supplier<DataComponentType<ItemContainerContents>> GARMENT_MODIFIERS = DATA_COMPONENTS.registerComponentType("garment_modifiers",
            builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC));


    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}