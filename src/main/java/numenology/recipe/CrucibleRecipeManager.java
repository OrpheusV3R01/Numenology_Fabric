package numenology.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient; // Импортируем Ingredient
import net.minecraft.registry.Registries;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CrucibleRecipeManager {

    public static final List<CrucibleRecipe> RECIPES = new ArrayList<>();

    public static void load(ResourceManager resourceManager) {
        RECIPES.clear();
        System.out.println("LOADING CRUCIBLE RECIPES...");

        try {
            var resources = resourceManager.findResources("crucible", id -> id.getPath().endsWith(".json"));

            for (var entry : resources.entrySet()) {
                try (InputStreamReader reader = new InputStreamReader(entry.getValue().getInputStream())) {
                    JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();

                    // 🔄 Ванильный метод сам распарсит и "item", и "tag" без крашей!
                    Ingredient input = Ingredient.fromJson(obj.get("ingredient"));

                    int numen = obj.get("numen_cost").getAsInt();
                    int time = obj.get("time").getAsInt();

                    JsonObject resultObj = obj.getAsJsonObject("result");
                    Identifier resultId = new Identifier(resultObj.get("item").getAsString());
                    ItemStack resultStack = new ItemStack(Registries.ITEM.get(resultId));

                    String knowledge = obj.has("required_knowledge")
                            ? obj.get("required_knowledge").getAsString()
                            : "numenology:first_contact_numen";

                    RECIPES.add(new CrucibleRecipe(input, numen, time, resultStack, knowledge));

                    System.out.println("Loaded crucible recipe | knowledge: " + knowledge);
                }
            }
            System.out.println("TOTAL RECIPES: " + RECIPES.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CrucibleRecipe getRecipe(ItemStack stack) {
        for (CrucibleRecipe recipe : RECIPES) {
            if (recipe.matches(stack)) return recipe;
        }
        return null;
    }
}