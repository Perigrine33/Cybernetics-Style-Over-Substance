package com.perigrine3.styleoversubstance.styleoversubstance.compat.jei;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.ModMenuTypes;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.menu.GarmentTableMenu;
import com.perigrine3.styleoversubstance.styleoversubstance.gui.screen.GarmentTableScreen;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.ModRecipes;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table.GarmentTableRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@JeiPlugin
public class JEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GarmentTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        if (Minecraft.getInstance().level == null) return;

        List<GarmentTableRecipe> recipes = Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ModRecipes.GARMENT_TABLE_TYPE.get()).stream().map(RecipeHolder::value).toList();
        registration.addRecipes(GarmentTableRecipeCategory.RECIPE_TYPE, recipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GARMENT_TABLE.get()), GarmentTableRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GarmentTableScreen.class, 188, 83, 18, 18, GarmentTableRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(GarmentTableMenu.class, ModMenuTypes.GARMENT_TABLE.get(), GarmentTableRecipeCategory.RECIPE_TYPE, GarmentTableMenu.CRAFTING_START, 25, GarmentTableMenu.PLAYER_INVENTORY_START, 36);
    }
}