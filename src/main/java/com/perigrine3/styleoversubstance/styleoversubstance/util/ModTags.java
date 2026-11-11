package com.perigrine3.styleoversubstance.styleoversubstance.util;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static class Items {
        public static final TagKey<Item> FABRICS = createModTag("fabrics");
        public static final TagKey<Item> CLOTHING_MODS = createModTag("clothing_mods");

        public static final TagKey<Item> HEAD = createCuriosTag("head");
        public static final TagKey<Item> NECKLACE = createCuriosTag("necklace");
        public static final TagKey<Item> BACK = createCuriosTag("back");
        public static final TagKey<Item> BANGLE = createCuriosTag("bracelet");
        public static final TagKey<Item> BODY = createCuriosTag("body");
        public static final TagKey<Item> BUNDLE = createCuriosTag("bundle");
        public static final TagKey<Item> QUIVER = createCuriosTag("quiver");
        public static final TagKey<Item> HANDS = createCuriosTag("hands");
        public static final TagKey<Item> RING = createCuriosTag("ring");
        public static final TagKey<Item> BELT = createCuriosTag("belt");
        public static final TagKey<Item> CHARM = createCuriosTag("charm");
        public static final TagKey<Item> PANTS = createCuriosTag("pants");
        public static final TagKey<Item> FEET = createCuriosTag("feet");

        private static TagKey<Item> createModTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, name));
        }

        private static TagKey<Item> createCuriosTag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", name));
        }
    }
}