package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Styleoversubstance.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //blockWithItem(ModBlocks.TITANIUMORE_BLOCK);

        //stairsBlock(ModBlocks.SMOOTH_TITANIUM_STAIRS.get(), blockTexture(ModBlocks.SMOOTH_TITANIUM.get()));
        //slabBlock(ModBlocks.SMOOTH_TITANIUM_SLAB.get(), blockTexture(ModBlocks.SMOOTH_TITANIUM.get()), blockTexture(ModBlocks.SMOOTH_TITANIUM.get()));

        //blockItem(ModBlocks.SMOOTH_TITANIUM_STAIRS);

    }





    private void cutoutBlockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), models().cubeAll(name(deferredBlock.get()), blockTexture(deferredBlock.get())).renderType("cutout"));
    }

    private void cutoutMippedBlockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), models().cubeAll(name(deferredBlock.get()), blockTexture(deferredBlock.get())).renderType("cutout_mipped"));
    }

    private static String name(net.minecraft.world.level.block.Block b) {
        return BuiltInRegistries.BLOCK.getKey(b).getPath();
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("styleoversubstance:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("styleoversubstance:block/" + deferredBlock.getId().getPath() + appendix));
    }
}