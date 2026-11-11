package com.perigrine3.styleoversubstance.styleoversubstance.item;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Styleoversubstance.MODID);

    public static final Supplier<CreativeModeTab> STYLE_OVER_SUBSTANCE_ITEMS_TAB = CREATIVE_MODE_TAB.register("style_over_substance_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.BASIC_FABRIC.get()))
                    .title(Component.translatable("creativetab.styleoversubstance.style_over_substance_items_tab"))
                    .displayItems((itemDisplayParameters, output) -> {

                        output.accept(ModBlocks.GARMENT_TABLE);

                        output.accept(ModItems.BASIC_FABRIC);
                        output.accept(ModItems.LEATHER_FABRIC);
                        output.accept(ModItems.SYNTHWEAVE_FABRIC);

                        output.accept(ModItems.MODULE_LEDS);
                        output.accept(ModItems.MODULE_BATTERY);
                        output.accept(ModItems.MODULE_ARMOREDPLATING);
                        output.accept(ModItems.MODULE_FIREPROOFGEL);
                        output.accept(ModItems.MODULE_FARADAYTHREADING);

                    }).build());



    public static final Supplier<CreativeModeTab> STYLE_OVER_SUBSTANCE_TAB = CREATIVE_MODE_TAB.register("style_over_substance_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.SYNTHWEAVE_FABRIC.get()))
                    .title(Component.translatable("creativetab.styleoversubstance.style_over_substance_tab"))
                    .displayItems((itemDisplayParameters, output) -> {
                //HEAD
                        output.accept(ModItems.BALACLAVA);
                        output.accept(ModItems.CAP);
                        output.accept(ModItems.VISOR1);
                        output.accept(ModItems.GLASSES1);
                        output.accept(ModItems.GLASSES2);
                        output.accept(ModItems.MONOVISOR);
                        output.accept(ModItems.GM08);
                        output.accept(ModItems.TB3);
                        output.accept(ModItems.MAXTAC_HELMET);
                        output.accept(ModItems.TRAUMA_TEAM_HELMET);
                        output.accept(ModItems.ONI_MASK);
                //BACK
                        output.accept(ModItems.LEATHER_JACKET1);
                        output.accept(ModItems.EMT_JACKET);
                        output.accept(ModItems.HIGH_CROPPED_LEATHER_JACKET);
                        output.accept(ModItems.LABCOAT1);
                        output.accept(ModItems.LABCOAT2);
                        output.accept(ModItems.BAGGY_SWEATSHIRT);
                //BODY
                        output.accept(ModItems.NETRUNNING_BODYSUIT);
                        output.accept(ModItems.NETRUNNING_UNITARD);
                        output.accept(ModItems.MAXTAC_BODYSUIT);
                        output.accept(ModItems.TRAUMA_TEAM_PADDED_ARMOR);
                //NECKLACE
                        output.accept(ModItems.MESSY_TIE);
                        output.accept(ModItems.TIE);
                //PANTS
                        output.accept(ModItems.CAMO_PANTS);


                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
