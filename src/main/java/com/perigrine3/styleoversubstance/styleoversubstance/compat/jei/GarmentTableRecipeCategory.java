package com.perigrine3.styleoversubstance.styleoversubstance.compat.jei;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.block.ModBlocks;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table.GarmentTableRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GarmentTableRecipeCategory implements IRecipeCategory<GarmentTableRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "garment_table");
    public static final RecipeType<GarmentTableRecipe> RECIPE_TYPE = RecipeType.create(Styleoversubstance.MODID, "garment_table", GarmentTableRecipe.class);
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Styleoversubstance.MODID, "textures/gui/garment_table/garment_gui.png");

    private final IDrawable background;
    private final IDrawable icon;

    public GarmentTableRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 43, 64, 174, 92);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(ModBlocks.GARMENT_TABLE.get()));
    }

    @Override
    public RecipeType<GarmentTableRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.styleoversubstance.garment_table");
    }

    @Override
    public int getWidth() {
        return 174;
    }

    @Override
    public int getHeight() {
        return 108;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GarmentTableRecipe recipe, IFocusGroup focuses) {
        int recipeOffsetX = (5 - recipe.getWidth()) / 2;
        int recipeOffsetY = (5 - recipe.getHeight()) / 2;

        for (int row = 0; row < recipe.getHeight(); row++) {
            for (int column = 0; column < recipe.getWidth(); column++) {
                if (recipe.getIngredient(column, row).isEmpty()) continue;

                builder.addSlot(RecipeIngredientRole.INPUT, 5 + (column + recipeOffsetX) * 18, 1 + (row + recipeOffsetY) * 18).addIngredients(recipe.getIngredient(column, row));
            }
        }

        builder.addSlot(RecipeIngredientRole.INPUT, 104, 1).addItemStack(new ItemStack(Items.STRING));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 146, 37).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(GarmentTableRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}