package numenology.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient; // Ипользуем ванильный класс ингредиентов
import net.minecraft.util.Identifier;

public class CrucibleRecipe {

    public final Ingredient input; // 🔄 Меняем Item на Ingredient
    public final int numenCost;
    public final int time;
    public final ItemStack result;
    public final String requiredKnowledge;

    public CrucibleRecipe(Ingredient input, int numenCost, int time, ItemStack result, String requiredKnowledge) {
        this.input = input;
        this.numenCost = numenCost;
        this.time = time;
        this.result = result;
        this.requiredKnowledge = requiredKnowledge;
    }

    public boolean matches(ItemStack stack) {
        // 🔄 Ingredient сам знает, как проверить, подходит ли предмет под условия (и тег, и итем)
        return input.test(stack);
    }
}