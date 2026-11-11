package com.perigrine3.styleoversubstance.styleoversubstance.event;

import com.perigrine3.createcybernetics.effect.ModEffects;
import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentModules;
import com.perigrine3.styleoversubstance.styleoversubstance.api.GarmentData;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import java.util.LinkedHashMap;
import java.util.Map;

@EventBusSubscriber(modid = Styleoversubstance.MODID)
public final class GarmentModuleEvents {
    private static final ResourceLocation PLATING = ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "garment_plating");

    private GarmentModuleEvents() {}

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        String description = null;
        if (stack.is(ModItems.MODULE_LEDS.get())) description = "leds";
        else if (stack.is(ModItems.MODULE_BATTERY.get())) description = "battery";
        else if (stack.is(ModItems.MODULE_ARMOREDPLATING.get())) description = "plating";
        else if (stack.is(ModItems.MODULE_FIREPROOFGEL.get())) description = "gel";
        else if (stack.is(ModItems.MODULE_FARADAYTHREADING.get())) description = "faraday";
        if (description != null) {
            event.getToolTip().add(Component.translatable("tooltip.styleoversubstance.module." + description).withStyle(ChatFormatting.GRAY));
            if (description.equals("faraday")) {
                event.getToolTip().add(Component.translatable("tooltip.styleoversubstance.module.faraday_head").withStyle(ChatFormatting.GRAY));
            }
            event.getToolTip().add(Component.translatable("tooltip.styleoversubstance.module.worn_only").withStyle(ChatFormatting.DARK_GRAY));
        }
        if (!GarmentData.isGarment(stack)) return;
        Map<Item, Integer> installed = new LinkedHashMap<>();
        for (ItemStack module : GarmentData.getModifiers(stack)) {
            if (GarmentModules.isModule(module)) installed.merge(module.getItem(), 1, Integer::sum);
        }
        if (installed.isEmpty()) return;
        event.getToolTip().add(Component.translatable("tooltip.styleoversubstance.module.installed").withStyle(ChatFormatting.AQUA));
        installed.forEach((module, count) -> event.getToolTip().add(Component.translatable(
                "tooltip.styleoversubstance.module.entry", module.getDescription(), count).withStyle(ChatFormatting.GRAY)));
        event.getToolTip().add(Component.translatable("tooltip.styleoversubstance.module.worn_only").withStyle(ChatFormatting.DARK_GRAY));
    }

    @SubscribeEvent
    public static void onTick(EntityTickEvent.Pre event) {
        if (!(event.getEntity() instanceof LivingEntity entity) || entity.level().isClientSide()) return;
        int plates = GarmentModules.countWorn(entity, ModItems.MODULE_ARMOREDPLATING.get());
        updateAttribute(entity, Attributes.ARMOR, plates * GarmentModules.ARMOR_PER_PLATE);
        updateAttribute(entity, Attributes.ARMOR_TOUGHNESS, plates * GarmentModules.TOUGHNESS_PER_PLATE);
    }

    private static void updateAttribute(LivingEntity entity, Holder<Attribute> attribute, double amount) {
        AttributeInstance instance = entity.getAttribute(attribute);
        if (instance == null) return;
        AttributeModifier current = instance.getModifier(PLATING);
        if (current != null && current.amount() == amount) return;
        if (current != null) instance.removeModifier(PLATING);
        if (amount > 0) instance.addTransientModifier(new AttributeModifier(PLATING, amount, AttributeModifier.Operation.ADD_VALUE));
    }

    @SubscribeEvent
    public static void onFireDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide() || !event.getSource().is(DamageTypeTags.IS_FIRE)
                || event.getSource().is(DamageTypes.LAVA)) return;
        if (GarmentModules.roll(entity, GarmentModules.countWorn(entity, ModItems.MODULE_FIREPROOFGEL.get()),
                GarmentModules.FIRE_PROTECTION_PERCENT)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEmp(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide() || !event.getEffectInstance().is(ModEffects.EMP)) return;
        if (GarmentModules.roll(entity, GarmentModules.countWorn(entity, ModItems.MODULE_FARADAYTHREADING.get()),
                GarmentModules.FARADAY_PERCENT)) event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }
}
