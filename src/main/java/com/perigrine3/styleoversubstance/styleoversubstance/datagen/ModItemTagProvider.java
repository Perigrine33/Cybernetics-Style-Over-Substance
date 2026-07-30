package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import com.perigrine3.styleoversubstance.styleoversubstance.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Styleoversubstance.MODID, existingFileHelper);
    }

    private static ResourceLocation resourceLocation(String id) {
        return ResourceLocation.parse(id);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(ModTags.Items.HEAD)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.NECKLACE)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.BACK)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.BANGLE)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.BODY)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.BUNDLE)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.QUIVER)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.HANDS)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.RING)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.BELT)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.CHARM)
                .add(ModItems.EXAMPLE_ITEM.get());

        tag(ModTags.Items.FEET)
                .add(ModItems.EXAMPLE_ITEM.get());
    }
}
