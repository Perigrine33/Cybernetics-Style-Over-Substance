package com.perigrine3.styleoversubstance.styleoversubstance.datagen;

import com.perigrine3.styleoversubstance.styleoversubstance.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.List;
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


//SHAPED RECIPE
        //ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TITANIUM_BLOCK.get())
        //        .pattern("TTT")
        //        .pattern("TTT")
        //        .pattern("TTT")
        //        .define('T', ModItems.TITANIUMINGOT.get())
        //        .unlockedBy("has_titaniumingot", has(ModItems.TITANIUMINGOT))
        //        .save(recipeOutput);


//SHAPELESS RECIPE
        //ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TITANIUMINGOT.get(), 9)
        //        .requires(ModBlocks.TITANIUM_BLOCK.get())
        //        .unlockedBy("has_titanium_block", has(ModBlocks.TITANIUM_BLOCK))
        //        .save(recipeOutput, "createcybernetics:titaniumingot_from_titanium_block");


//SMELTING
        //oreSmelting(recipeOutput, TITANIUM_SMELTABLES, RecipeCategory.MISC,
        //        ModItems.TITANIUMINGOT.get(), 0.25f, 200, "titanium");


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
