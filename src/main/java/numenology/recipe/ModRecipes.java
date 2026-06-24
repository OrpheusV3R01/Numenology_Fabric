package numenology.recipe;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;

public class ModRecipes {

    // ===== TYPE =====
    public static final RecipeType<NumenSmelteryRecipe> NUMEN_SMELTERY_TYPE =
            Registry.register(
                    Registries.RECIPE_TYPE,
                    new Identifier("numenology", "numen_smeltery"),
                    new RecipeType<>() {
                        public String toString() {
                            return "numen_smeltery";
                        }
                    }
            );

    // ===== SERIALIZER =====
    public static final RecipeSerializer<NumenSmelteryRecipe> NUMEN_SMELTERY_SERIALIZER =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    new Identifier("numenology", "numen_smeltery"),
                    new NumenSmelteryRecipeSerializer()
            );

    public static final RecipeType<DryingTableRecipe> DRYING_TABLE_TYPE =
            Registry.register(
                    Registries.RECIPE_TYPE,
                    new Identifier("numenology", "drying_table"),
                    new RecipeType<>() {
                        public String toString() { return "drying_table"; }
                    }
            );

    public static final RecipeSerializer<DryingTableRecipe> DRYING_TABLE_SERIALIZER =
            Registry.register(
                    Registries.RECIPE_SERIALIZER,
                    new Identifier("numenology", "drying_table"),
                    new DryingTableRecipeSerializer()
            );

    public static void register() {
        // вызывается в main классе мода
    }
}