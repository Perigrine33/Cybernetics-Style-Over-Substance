package com.perigrine3.styleoversubstance.styleoversubstance.item;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.clothing.StyleClothingItem;
import com.perigrine3.styleoversubstance.styleoversubstance.item.cyberware.skin.PlasticCasingItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Styleoversubstance.MODID);


//BASIC ITEMS & COMPONENTS
    //FABRICS
    public static final DeferredItem<Item> BASIC_FABRIC = ITEMS.register("basic_fabric",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LEATHER_FABRIC = ITEMS.register("leather_fabric",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SYNTHWEAVE_FABRIC = ITEMS.register("synthweave_fabric",
            () -> new Item(new Item.Properties()));
    //CLOTHING MODULES
    public static final DeferredItem<Item> MODULE_LEDS = ITEMS.register("module_leds",
            () -> new Item(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                        tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.clothing_module"));
                }});
    public static final DeferredItem<Item> MODULE_BATTERY = ITEMS.register("module_battery",
            () -> new Item(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.clothing_module"));
                }});
    public static final DeferredItem<Item> MODULE_ARMOREDPLATING = ITEMS.register("module_armoredplating",
            () -> new Item(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.clothing_module"));
                }});
    public static final DeferredItem<Item> MODULE_FIREPROOFGEL = ITEMS.register("module_fireproofgel",
            () -> new Item(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.clothing_module"));
                }});
    public static final DeferredItem<Item> MODULE_FARADAYTHREADING = ITEMS.register("module_faradaythreading",
            () -> new Item(new Item.Properties()) {
                @Override
                public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.clothing_module"));
                }});
    //COMPONENTS



//HEAD
    public static final DeferredItem<Item> BALACLAVA = ITEMS.register("balaclava",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> CAP = ITEMS.register("cap",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> VISOR1 = ITEMS.register("visor1",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> GLASSES1 = ITEMS.register("glasses1",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> GLASSES2 = ITEMS.register("glasses2",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> MONOVISOR = ITEMS.register("monovisor",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> GM08 = ITEMS.register("gm08",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> TB3 = ITEMS.register("tb3",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> MAXTAC_HELMET = ITEMS.register("maxtac_helmet",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> TRAUMA_TEAM_HELMET = ITEMS.register("trauma_team_helmet",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> ONI_MASK = ITEMS.register("oni_mask",
            () -> new StyleClothingItem(new Item.Properties()));

//NECKLACE
    public static final DeferredItem<Item> MESSY_TIE = ITEMS.register("messy_tie",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> TIE = ITEMS.register("tie",
            () -> new StyleClothingItem(new Item.Properties()));

//BACK
    public static final DeferredItem<Item> LEATHER_JACKET1 = ITEMS.register("leather_jacket1",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> EMT_JACKET = ITEMS.register("emt_jacket",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> HIGH_CROPPED_LEATHER_JACKET = ITEMS.register("high_cropped_leather_jacket",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> LABCOAT1 = ITEMS.register("labcoat1",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> LABCOAT2 = ITEMS.register("labcoat2",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> BAGGY_SWEATSHIRT = ITEMS.register("baggy_sweatshirt",
            () -> new StyleClothingItem(new Item.Properties()));

//BANGLE
//BODY
    public static final DeferredItem<Item> NETRUNNING_BODYSUIT = ITEMS.register("netrunning_bodysuit",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> NETRUNNING_UNITARD = ITEMS.register("netrunning_unitard",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> MAXTAC_BODYSUIT = ITEMS.register("maxtac_bodysuit",
            () -> new StyleClothingItem(new Item.Properties()));
    public static final DeferredItem<Item> TRAUMA_TEAM_PADDED_ARMOR = ITEMS.register("trauma_team_padded_armor",
            () -> new StyleClothingItem(new Item.Properties()));

//BUNDLE
//QUIVER
//HANDS
//RING
//BELT
//CHARM
//PANTS
    public static final DeferredItem<Item> CAMO_PANTS = ITEMS.register("camo_pants",
            () -> new StyleClothingItem(new Item.Properties()));

//FEET






//FASHIONWARE
    public static final DeferredItem<Item> SKINUPGRADES_PLASTICCASING = ITEMS.register("skinupgrades_plasticcasing",
            () -> new PlasticCasingItem(new Item.Properties().stacksTo(16), 3) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                if (Screen.hasShiftDown()) {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.skinupgrades_plasticcasing.tooltip1"));
                } else {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.hold_shift_down"));
                }
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });
    public static final DeferredItem<Item> SKINUPGRADES_TECHHAIR = ITEMS.register("skinupgrades_techhair",
            () -> new PlasticCasingItem(new Item.Properties().stacksTo(16), 3) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                if (Screen.hasShiftDown()) {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.skinupgrades_techhair.tooltip1"));
                } else {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.hold_shift_down"));
                }
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });
    public static final DeferredItem<Item> SKINUPGRADES_SKINWATCH = ITEMS.register("skinupgrades_skinwatch",
            () -> new PlasticCasingItem(new Item.Properties().stacksTo(16), 3) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                if (Screen.hasShiftDown()) {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.skinupgrades_skinwatch.tooltip1"));
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.skinupgrades_skinwatch.tooltip2"));
                } else {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.hold_shift_down"));
                }
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });
    public static final DeferredItem<Item> SKINUPGRADES_CHEMSKIN = ITEMS.register("skinupgrades_chemskin",
            () -> new PlasticCasingItem(new Item.Properties().stacksTo(16), 3) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                if (Screen.hasShiftDown()) {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.skinupgrades_chemskin.tooltip1"));
                    tooltipComponents.add(Component.translatable("tooltip.styleoversubstance.skinupgrades_chemskin.tooltip2"));
                } else {
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.skinupgrades_tooltip"));
                    tooltipComponents.add(Component.translatable("tooltip.createcybernetics.hold_shift_down"));
                }
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });

//SCAVENGED FASHIONWARE
    public static final DeferredItem<Item> SCAVENGED_PLASTICCASING = ITEMS.register("scavenged_plasticcasing",
            () -> new Item(new Item.Properties().stacksTo(64)) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                tooltipComponents.add(Component.translatable("tooltip.createcybernetics.scavenged_tooltip"));
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });
    public static final DeferredItem<Item> SCAVENGED_TECHHAIR = ITEMS.register("scavenged_techhair",
            () -> new Item(new Item.Properties().stacksTo(64)) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                tooltipComponents.add(Component.translatable("tooltip.createcybernetics.scavenged_tooltip"));
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });
    public static final DeferredItem<Item> SCAVENGED_SKINWATCH = ITEMS.register("scavenged_skinwatch",
            () -> new Item(new Item.Properties().stacksTo(64)) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                tooltipComponents.add(Component.translatable("tooltip.createcybernetics.scavenged_tooltip"));
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });
    public static final DeferredItem<Item> SCAVENGED_CHEMSKIN = ITEMS.register("scavenged_chemskin",
            () -> new Item(new Item.Properties().stacksTo(64)) {
            @Override
            public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                tooltipComponents.add(Component.translatable("tooltip.createcybernetics.scavenged_tooltip"));
                super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
            }
        });







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
