package com.perigrine3.styleoversubstance.styleoversubstance.item;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Styleoversubstance.MODID);

    public static final Supplier<CreativeModeTab> STYLE_OVER_SUBSTANCE_TAB = CREATIVE_MODE_TAB.register("style_over_substance_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.EXAMPLE_ITEM.get()))
                    .title(Component.translatable("creativetab.styleoversubstance.style_over_substance_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        //output.accept(ModItems.BODYPART_RIGHTARM);

                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
