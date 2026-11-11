package com.perigrine3.styleoversubstance.styleoversubstance.recipe.garment_table;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.perigrine3.styleoversubstance.styleoversubstance.recipe.ModRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GarmentTableRecipe implements Recipe<GarmentTableRecipeInput> {
    public static final int MAX_WIDTH = 5;
    public static final int MAX_HEIGHT = 5;

    private final boolean acceptMirrored;
    private final Map<String, Ingredient> key;
    private final List<String> pattern;
    private final ItemStack result;
    private final int width;
    private final int height;
    private final NonNullList<Ingredient> ingredients;

    public GarmentTableRecipe(boolean acceptMirrored, Map<String, Ingredient> key, List<String> pattern, ItemStack result) {
        validatePattern(key, pattern, result);

        this.acceptMirrored = acceptMirrored;
        this.key = Map.copyOf(key);
        this.pattern = List.copyOf(pattern);
        this.result = result.copy();
        this.width = pattern.getFirst().length();
        this.height = pattern.size();
        this.ingredients = compileIngredients(this.key, this.pattern, width, height);
    }

    private GarmentTableRecipe(boolean acceptMirrored, int width, int height, NonNullList<Ingredient> ingredients, ItemStack result) {
        this.acceptMirrored = acceptMirrored;
        this.key = Map.of();
        this.pattern = List.of();
        this.result = result.copy();
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
    }

    private static void validatePattern(Map<String, Ingredient> key, List<String> pattern, ItemStack result) {
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("Garment Table recipe pattern cannot be empty");
        }

        if (pattern.size() > MAX_HEIGHT) {
            throw new IllegalArgumentException("Garment Table recipe cannot contain more than 5 rows");
        }

        int width = pattern.getFirst().length();

        if (width < 1 || width > MAX_WIDTH) {
            throw new IllegalArgumentException("Garment Table recipe rows must contain between 1 and 5 columns");
        }

        if (result.isEmpty()) {
            throw new IllegalArgumentException("Garment Table recipe result cannot be empty");
        }

        for (String row : pattern) {
            if (row.length() != width) {
                throw new IllegalArgumentException("All Garment Table recipe rows must have the same width");
            }
        }

        Set<String> usedSymbols = new HashSet<>();

        for (String row : pattern) {
            for (int column = 0; column < row.length(); column++) {
                char symbol = row.charAt(column);

                if (symbol == ' ') continue;

                String symbolKey = String.valueOf(symbol);

                if (!key.containsKey(symbolKey)) {
                    throw new IllegalArgumentException("Garment Table recipe uses undefined symbol '" + symbolKey + "'");
                }

                usedSymbols.add(symbolKey);
            }
        }

        for (String symbolKey : key.keySet()) {
            if (symbolKey.length() != 1) {
                throw new IllegalArgumentException("Garment Table recipe keys must contain exactly one character");
            }

            if (" ".equals(symbolKey)) {
                throw new IllegalArgumentException("Garment Table recipe key cannot define the space character");
            }

            if (!usedSymbols.contains(symbolKey)) {
                throw new IllegalArgumentException("Garment Table recipe defines unused symbol '" + symbolKey + "'");
            }
        }
    }

    private static NonNullList<Ingredient> compileIngredients(Map<String, Ingredient> key, List<String> pattern, int width, int height) {
        NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

        for (int row = 0; row < height; row++) {
            String patternRow = pattern.get(row);

            for (int column = 0; column < width; column++) {
                char symbol = patternRow.charAt(column);

                if (symbol == ' ') continue;

                ingredients.set(column + row * width, key.get(String.valueOf(symbol)));
            }
        }

        return ingredients;
    }

    @Override
    public boolean matches(GarmentTableRecipeInput input, Level level) {
        for (int offsetY = 0; offsetY <= MAX_HEIGHT - height; offsetY++) {
            for (int offsetX = 0; offsetX <= MAX_WIDTH - width; offsetX++) {
                if (matchesAt(input, offsetX, offsetY, false)) return true;
                if (acceptMirrored && matchesAt(input, offsetX, offsetY, true)) return true;
            }
        }

        return false;
    }

    private boolean matchesAt(GarmentTableRecipeInput input, int offsetX, int offsetY, boolean mirrored) {
        for (int gridY = 0; gridY < MAX_HEIGHT; gridY++) {
            for (int gridX = 0; gridX < MAX_WIDTH; gridX++) {
                int recipeX = gridX - offsetX;
                int recipeY = gridY - offsetY;
                Ingredient expected = Ingredient.EMPTY;

                if (recipeX >= 0 && recipeY >= 0 && recipeX < width && recipeY < height) {
                    int ingredientX = mirrored ? width - recipeX - 1 : recipeX;
                    expected = ingredients.get(ingredientX + recipeY * width);
                }

                ItemStack actual = input.getItem(gridX, gridY);

                if (expected.isEmpty()) {
                    if (!actual.isEmpty()) return false;
                } else if (!expected.test(actual)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack assemble(GarmentTableRecipeInput input, HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result.copy();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GARMENT_TABLE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.GARMENT_TABLE_TYPE.get();
    }

    public boolean acceptsMirrored() {
        return acceptMirrored;
    }

    public Map<String, Ingredient> getKey() {
        return key;
    }

    public List<String> getPattern() {
        return pattern;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    public Ingredient getIngredient(int column, int row) {
        if (column < 0 || column >= width) return Ingredient.EMPTY;
        if (row < 0 || row >= height) return Ingredient.EMPTY;

        return ingredients.get(column + row * width);
    }

    public ItemStack getResult() {
        return result.copy();
    }

    private record RecipeData(boolean acceptMirrored, Map<String, Ingredient> key, List<String> pattern, ItemStack result) {
        private static final Codec<Map<String, Ingredient>> KEY_CODEC = Codec.unboundedMap(Codec.STRING, Ingredient.CODEC);

        private static final MapCodec<RecipeData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.BOOL.optionalFieldOf("accept_mirrored", true).forGetter(RecipeData::acceptMirrored),
                KEY_CODEC.fieldOf("key").forGetter(RecipeData::key),
                Codec.STRING.listOf().fieldOf("pattern").forGetter(RecipeData::pattern),
                ItemStack.CODEC.fieldOf("result").forGetter(RecipeData::result)
        ).apply(instance, RecipeData::new));

        private GarmentTableRecipe createRecipe() {
            return new GarmentTableRecipe(acceptMirrored, key, pattern, result);
        }

        private static RecipeData fromRecipe(GarmentTableRecipe recipe) {
            return new RecipeData(recipe.acceptMirrored, recipe.key, recipe.pattern, recipe.result);
        }
    }

    public static class Serializer implements RecipeSerializer<GarmentTableRecipe> {
        private static final MapCodec<GarmentTableRecipe> CODEC = RecipeData.CODEC.xmap(RecipeData::createRecipe, RecipeData::fromRecipe);
        private static final StreamCodec<RegistryFriendlyByteBuf, GarmentTableRecipe> STREAM_CODEC = StreamCodec.of(Serializer::encode, Serializer::decode);

        private static void encode(RegistryFriendlyByteBuf buffer, GarmentTableRecipe recipe) {
            buffer.writeBoolean(recipe.acceptMirrored);
            buffer.writeVarInt(recipe.width);
            buffer.writeVarInt(recipe.height);

            for (Ingredient ingredient : recipe.ingredients) {
                Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient);
            }

            ItemStack.STREAM_CODEC.encode(buffer, recipe.result);
        }

        private static GarmentTableRecipe decode(RegistryFriendlyByteBuf buffer) {
            boolean acceptMirrored = buffer.readBoolean();
            int width = buffer.readVarInt();
            int height = buffer.readVarInt();

            if (width < 1 || width > MAX_WIDTH) {
                throw new IllegalArgumentException("Invalid Garment Table recipe width: " + width);
            }

            if (height < 1 || height > MAX_HEIGHT) {
                throw new IllegalArgumentException("Invalid Garment Table recipe height: " + height);
            }

            NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

            for (int slot = 0; slot < ingredients.size(); slot++) {
                ingredients.set(slot, Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));
            }

            ItemStack result = ItemStack.STREAM_CODEC.decode(buffer);

            return new GarmentTableRecipe(acceptMirrored, width, height, ingredients, result);
        }

        @Override
        public MapCodec<GarmentTableRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, GarmentTableRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}