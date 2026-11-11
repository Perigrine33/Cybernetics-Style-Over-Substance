package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
//SMELTABLES
        //List<ItemLike> TITANIUM_SMELTABLES = List.of(ModItems.RAWTITANIUM, ModItems.CRUSHEDTITANIUM,
        //        ModBlocks.TITANIUMORE_BLOCK, ModBlocks.DEEPSLATE_TITANIUMORE_BLOCK);


//SHAPED RECIPES
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GARMENT_TABLE.get(), 4)
                .pattern(" F ")
                .pattern("FEF")
                .pattern("SF ")
                .define('E', com.perigrine3.createcybernetics.block.ModBlocks.ENGINEERING_TABLE.get())
                .define('F', ModItems.BASIC_FABRIC)
                .define('S', Items.SHEARS)
                .unlockedBy("has_fabric", has(ModItems.BASIC_FABRIC))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASIC_FABRIC.get(), 4)
                .pattern("STT")
                .define('T', Items.WHITE_WOOL)
                .define('S', Items.STICK)
                .unlockedBy("has_whitewool", has(Items.WHITE_WOOL))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LEATHER_FABRIC.get(), 4)
                .pattern("STT")
                .define('T', Items.LEATHER)
                .define('S', Items.STICK)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SYNTHWEAVE_FABRIC.get(), 4)
                .pattern("STT")
                .define('T', com.perigrine3.createcybernetics.item.ModItems.PLASTIC_SHEET)
                .define('S', Items.STICK)
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(recipeOutput);


//SHAPELESS RECIPE
        //ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.UNREFINED_POLYMER.get(), 3)
        //        .requires(ModItems.TAR.get())
        //        .requires(Items.SLIME_BALL)
        //        .requires(Items.BONE_MEAL)
        //        .unlockedBy("has_tar", has(ModItems.TAR.get()))
        //        .save(recipeOutput);


//SMELTING
        //oreSmelting(recipeOutput, List.of(Items.COAL, Items.CHARCOAL), RecipeCategory.MISC,
        //        ModItems.TAR, 0.25f, 200, "tar_from_coal");

//BLASTING
        //oreBlasting(recipeOutput, TITANIUM_SMELTABLES, RecipeCategory.MISC,
        //        ModItems.TITANIUMINGOT.get(), 0.25f, 100, "titanium");


//STONECUTTING
        //SingleItemRecipeBuilder.stonecutting(Ingredient.of(ModBlocks.TITANIUM_BLOCK.get()),
        //                RecipeCategory.MISC,
        //                ModBlocks.SMOOTH_TITANIUM.get(), 4)
        //        .unlockedBy("has_titanium_block", has(ModBlocks.TITANIUM_BLOCK.get()))
        //        .save(recipeOutput, "createcybernetics:smooth_titanium_from_stonecutter");


//SMITHING
        //SmithingTransformRecipeBuilder.smithing(
        //                Ingredient.of(ModItems.TITANIUMSHEET),
        //                Ingredient.of(ModItems.XP_CAPSULE.get()),
        //                Ingredient.of(ModItems.COMPONENT_WIRING.get()),
        //                RecipeCategory.MISC,
        //                ModItems.BRAINUPGRADES_CORTICALSTACK.get())
        //        .unlocks("has_xp_capsule", has(ModItems.XP_CAPSULE.get()))
        //        .save(recipeOutput, "createcybernetics:cortical_stack_from_xp_capsule");


//NON-BLOCK BLOCK RECIPES
        //stairBuilder(ModBlocks.SMOOTH_TITANIUM_STAIRS.get(), Ingredient.of(ModBlocks.SMOOTH_TITANIUM)).group("titanium")
        //        .unlockedBy("has_titaniumingot", has(ModBlocks.SMOOTH_TITANIUM)).save(recipeOutput);

        //slab(recipeOutput, RecipeCategory.BUILDING_BLOCKS, ModBlocks.SMOOTH_TITANIUM_SLAB.get(), ModBlocks.SMOOTH_TITANIUM.get());
    }
}
