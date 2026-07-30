package com.perigrine3.styleoversubstance.styleoversubstance.util;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        //public static final TagKey<Block> METAL_DETECTABLE = createTag("metal_detectable");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, name));
        }
    }


    public static class Items {
        public static final TagKey<Item> HEAD = createTag("head");
        public static final TagKey<Item> NECKLACE = createTag("necklace");
        public static final TagKey<Item> BACK = createTag("back");
        public static final TagKey<Item> BANGLE = createTag("bangle");
        public static final TagKey<Item> BODY = createTag("body");
        public static final TagKey<Item> BUNDLE = createTag("bundle");
        public static final TagKey<Item> QUIVER = createTag("quiver");
        public static final TagKey<Item> HANDS = createTag("hands");
        public static final TagKey<Item> RING = createTag("ring");
        public static final TagKey<Item> BELT = createTag("belt");
        public static final TagKey<Item> CHARM = createTag("charm");
        public static final TagKey<Item> FEET = createTag("feet");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, name));
        }
    }


    public static class Enchantments {
        //public static final TagKey<Enchantment> HARVESTER_EXCLUSIVE = createTag("harvester_exclusive");

        private static TagKey<Enchantment> createTag(String name) {
            return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, name));
        }
    }


    public static final class Biomes {
        //public static final TagKey<Biome> CYBERMONSTER_SPAWNS = create("cybermonster_spawns");

        private static TagKey<Biome> create(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, name));
        }
    }


    public static final class Entities {

        public static final TagKey<EntityType<?>> CYBERENTITIES = createTag("cyberentities");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, name));
        }
    }
}
