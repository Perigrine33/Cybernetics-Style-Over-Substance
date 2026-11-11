package com.perigrine3.styleoversubstance.styleoversubstance.recipe;

import com.perigrine3.styleoversubstance.styleoversubstance.Styleoversubstance;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table.GarmentTableRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Styleoversubstance.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, Styleoversubstance.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<GarmentTableRecipe>> GARMENT_TABLE_SERIALIZER = RECIPE_SERIALIZERS.register("garment_table", GarmentTableRecipe.Serializer::new);

    public static final DeferredHolder<RecipeType<?>, RecipeType<GarmentTableRecipe>> GARMENT_TABLE_TYPE = RECIPE_TYPES.register("garment_table",
            () -> new RecipeType<GarmentTableRecipe>() {
                @Override
                public String toString() {
                    return Styleoversubstance.MODID + ":garment_table";
                }
            });

    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
        RECIPE_TYPES.register(eventBus);
    }
}