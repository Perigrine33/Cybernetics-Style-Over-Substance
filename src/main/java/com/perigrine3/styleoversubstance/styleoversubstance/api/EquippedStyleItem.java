package com.perigrine3.styleoversubstance.styleoversubstance.api;

import net.minecraft.world.item.ItemStack;

public record EquippedStyleItem(ItemStack stack, String slotIdentifier, int slotIndex) {
}