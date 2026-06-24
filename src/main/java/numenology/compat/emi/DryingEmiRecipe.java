package numenology.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import numenology.recipe.DryingTableRecipe;

import java.util.List;

public class DryingEmiRecipe implements EmiRecipe {
    private final Identifier id;
    private final EmiIngredient input;
    private final EmiStack output;

    public DryingEmiRecipe(DryingTableRecipe recipe, DynamicRegistryManager registryManager) {
        this.id = recipe.getId();

        // Защита от пустого списка ингредиентов
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            // Превращаем ванильный Ingredient в EmiIngredient
            this.input = EmiIngredient.of(recipe.getIngredients().get(0));
        } else {
            // ИСПРАВЛЕНИЕ: Используем встроенный пустой стек EMI вместо ItemStack.EMPTY
            this.input = EmiStack.EMPTY;
        }

        this.output = EmiStack.of(recipe.getOutput(registryManager));
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return NumenologyEmiPlugin.DRYING_CATEGORY;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(input);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(output);
    }

    @Override
    public int getDisplayWidth() {
        return 100;
    }

    @Override
    public int getDisplayHeight() {
        return 40;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(input, 10, 11);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 40, 12);
        widgets.addAnimatedTexture(EmiTexture.FULL_ARROW, 40, 12, 2000, true, false, false);
        widgets.addSlot(output, 72, 7).large(true);
    }
}