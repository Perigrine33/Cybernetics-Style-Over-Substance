package com.perigrine3.styleoversubstance.styleoversubstance.api;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public final class StyleClothingApi {

    private static Function<LivingEntity, LivingEntity> visualEntityResolver = entity -> entity;

    private StyleClothingApi() {
    }

    public static void setVisualEntityResolver(Function<LivingEntity, LivingEntity> resolver) {
        visualEntityResolver = resolver != null ? resolver : entity -> entity;
    }

    public static LivingEntity resolveVisualEntity(LivingEntity entity) {
        if (entity == null) return null;

        try {
            LivingEntity resolved = visualEntityResolver.apply(entity);
            return resolved != null ? resolved : entity;
        } catch (Throwable ignored) {
            return entity;
        }
    }

    public static boolean hasSpecificItem(LivingEntity entity, Item item) {
        return hasEquipped(entity, item);
    }

    public static boolean hasSpecificItem(LivingEntity entity, Item item, String slotIdentifier) {
        return hasEquippedInSlot(entity, slotIdentifier, item);
    }

    public static boolean hasSpecificItem(LivingEntity entity, TagKey<Item> tag) {
        return hasEquipped(entity, tag);
    }

    public static boolean hasSpecificItem(LivingEntity entity, TagKey<Item> tag, String slotIdentifier) {
        return hasEquippedInSlot(entity, slotIdentifier, tag);
    }

    public static boolean hasSpecificItem(LivingEntity entity, Predicate<ItemStack> predicate) {
        return hasEquipped(entity, predicate);
    }

    public static boolean hasSpecificItem(LivingEntity entity, Predicate<ItemStack> predicate, String slotIdentifier) {
        return hasEquippedInSlot(entity, slotIdentifier, predicate);
    }

    public static ItemStack getSpecificItem(LivingEntity entity, Item item) {
        return getEquippedStack(entity, item);
    }

    public static ItemStack getSpecificItem(LivingEntity entity, Item item, String slotIdentifier) {
        return getEquippedStackInSlot(entity, slotIdentifier, item);
    }

    public static ItemStack getSpecificItem(LivingEntity entity, TagKey<Item> tag) {
        return findFirst(entity, tag).map(EquippedStyleItem::stack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack getSpecificItem(LivingEntity entity, TagKey<Item> tag, String slotIdentifier) {
        return findFirstInSlot(entity, slotIdentifier, tag).map(EquippedStyleItem::stack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack getSpecificItem(LivingEntity entity, Predicate<ItemStack> predicate) {
        return findFirst(entity, predicate).map(EquippedStyleItem::stack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack getSpecificItem(LivingEntity entity, Predicate<ItemStack> predicate, String slotIdentifier) {
        return findFirstInSlot(entity, slotIdentifier, predicate).map(EquippedStyleItem::stack).orElse(ItemStack.EMPTY);
    }

    public static boolean hasEquipped(LivingEntity entity, Item item) {
        return findFirst(entity, stack -> stack.is(item)).isPresent();
    }

    public static boolean hasEquipped(LivingEntity entity, TagKey<Item> tag) {
        return findFirst(entity, stack -> stack.is(tag)).isPresent();
    }

    public static boolean hasEquipped(LivingEntity entity, Predicate<ItemStack> predicate) {
        return findFirst(entity, predicate).isPresent();
    }

    public static boolean hasStyleClothingEquipped(LivingEntity entity) {
        return findFirst(entity, stack -> stack.getItem() instanceof IStyleClothingItem).isPresent();
    }

    public static boolean hasEquippedInSlot(LivingEntity entity, String slotIdentifier, Item item) {
        return findFirstInSlot(entity, slotIdentifier, stack -> stack.is(item)).isPresent();
    }

    public static boolean hasEquippedInSlot(LivingEntity entity, String slotIdentifier, TagKey<Item> tag) {
        return findFirstInSlot(entity, slotIdentifier, stack -> stack.is(tag)).isPresent();
    }

    public static boolean hasEquippedInSlot(LivingEntity entity, String slotIdentifier, Predicate<ItemStack> predicate) {
        return findFirstInSlot(entity, slotIdentifier, predicate).isPresent();
    }

    public static Optional<EquippedStyleItem> findFirst(LivingEntity entity, Item item) {
        return findFirst(entity, stack -> stack.is(item));
    }

    public static Optional<EquippedStyleItem> findFirst(LivingEntity entity, TagKey<Item> tag) {
        return findFirst(entity, stack -> stack.is(tag));
    }

    public static Optional<EquippedStyleItem> findFirst(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity == null || predicate == null) return Optional.empty();

        LivingEntity resolvedEntity = resolveVisualEntity(entity);
        if (resolvedEntity == null) return Optional.empty();

        return CuriosApi.getCuriosInventory(resolvedEntity).flatMap(handler -> handler.findFirstCurio(predicate)).map(StyleClothingApi::createResult);
    }

    public static Optional<EquippedStyleItem> findFirstInSlot(LivingEntity entity, String slotIdentifier, Item item) {
        return findFirstInSlot(entity, slotIdentifier, stack -> stack.is(item));
    }

    public static Optional<EquippedStyleItem> findFirstInSlot(LivingEntity entity, String slotIdentifier, TagKey<Item> tag) {
        return findFirstInSlot(entity, slotIdentifier, stack -> stack.is(tag));
    }

    public static Optional<EquippedStyleItem> findFirstInSlot(LivingEntity entity, String slotIdentifier, Predicate<ItemStack> predicate) {
        if (entity == null || slotIdentifier == null || slotIdentifier.isBlank() || predicate == null) return Optional.empty();

        return findAll(entity, predicate).stream().filter(result -> result.slotIdentifier().equals(slotIdentifier)).findFirst();
    }

    public static List<EquippedStyleItem> findAll(LivingEntity entity, Item item) {
        return findAll(entity, stack -> stack.is(item));
    }

    public static List<EquippedStyleItem> findAll(LivingEntity entity, TagKey<Item> tag) {
        return findAll(entity, stack -> stack.is(tag));
    }

    public static List<EquippedStyleItem> findAll(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (entity == null || predicate == null) return Collections.emptyList();

        LivingEntity resolvedEntity = resolveVisualEntity(entity);
        if (resolvedEntity == null) return Collections.emptyList();

        return CuriosApi.getCuriosInventory(resolvedEntity).map(handler -> handler.findCurios(predicate).stream().map(StyleClothingApi::createResult).toList()).orElse(Collections.emptyList());
    }

    public static List<EquippedStyleItem> findAllStyleClothing(LivingEntity entity) {
        return findAll(entity, stack -> stack.getItem() instanceof IStyleClothingItem);
    }

    public static int countEquipped(LivingEntity entity, Item item) {
        return findAll(entity, item).size();
    }

    public static int countEquipped(LivingEntity entity, TagKey<Item> tag) {
        return findAll(entity, tag).size();
    }

    public static int countEquipped(LivingEntity entity, Predicate<ItemStack> predicate) {
        return findAll(entity, predicate).size();
    }

    public static ItemStack getEquippedStack(LivingEntity entity, Item item) {
        return findFirst(entity, item).map(EquippedStyleItem::stack).orElse(ItemStack.EMPTY);
    }

    public static ItemStack getEquippedStackInSlot(LivingEntity entity, String slotIdentifier, Item item) {
        return findFirstInSlot(entity, slotIdentifier, item).map(EquippedStyleItem::stack).orElse(ItemStack.EMPTY);
    }

    private static EquippedStyleItem createResult(SlotResult result) {
        return new EquippedStyleItem(result.stack(), result.slotContext().identifier(), result.slotContext().index());
    }
}