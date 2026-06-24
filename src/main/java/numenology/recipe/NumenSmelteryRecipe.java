package numenology.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class NumenSmelteryRecipe implements Recipe<Inventory> {

    private final Identifier id;

    public final Ingredient inputA;
    public final Ingredient inputB;

    private final int countA;
    private final int countB;

    private final ItemStack output;



    public NumenSmelteryRecipe(Identifier id,
                               Ingredient inputA, int countA,
                               Ingredient inputB, int countB,
                               ItemStack output) {

        this.id = id;
        this.inputA = inputA;
        this.countA = countA;
        this.inputB = inputB;
        this.countB = countB;
        this.output = output;
    }

    @Override
    public boolean matches(Inventory inv, World world) {

        ItemStack stackA = inv.getStack(0);
        ItemStack stackB = inv.getStack(1);

        return inputA.test(stackA) && stackA.getCount() >= countA &&
                inputB.test(stackB) && stackB.getCount() >= countB;
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
        return ModRecipes.NUMEN_SMELTERY_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.NUMEN_SMELTERY_TYPE;
    }


    public int getCountA() { return countA; }
    public int getCountB() { return countB; }
}