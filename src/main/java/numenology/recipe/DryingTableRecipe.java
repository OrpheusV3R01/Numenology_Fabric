package numenology.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class DryingTableRecipe implements Recipe<Inventory> {

    private final Identifier id;
    public final Ingredient input;
    private final int count;
    private final ItemStack output;
    private final int dryingTime; // в тиках

    public DryingTableRecipe(Identifier id, Ingredient input, int count, ItemStack output, int dryingTime) {
        this.id = id;
        this.input = input;
        this.count = count;
        this.output = output;
        this.dryingTime = dryingTime;
    }

    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack stack = inv.getStack(0);
        return input.test(stack) && stack.getCount() >= count;
    }

    @Override
    public ItemStack craft(Inventory inv, DynamicRegistryManager registryManager) {
        return output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return output;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DRYING_TABLE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.DRYING_TABLE_TYPE;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        DefaultedList<Ingredient> list = DefaultedList.of();
        list.add(this.input); // Передаем твое поле ингредиента в ванильный список
        return list;
    }

    public int getCount() { return count; }
    public int getDryingTime() { return dryingTime; }
}