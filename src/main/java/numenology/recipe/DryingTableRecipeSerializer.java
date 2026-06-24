package numenology.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class DryingTableRecipeSerializer implements RecipeSerializer<DryingTableRecipe> {

    @Override
    public DryingTableRecipe read(Identifier id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        int count = JsonHelper.getInt(json.getAsJsonObject("input"), "count");
        ItemStack output = ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));
        int dryingTime = JsonHelper.getInt(json, "drying_time");

        return new DryingTableRecipe(id, input, count, output, dryingTime);
    }

    @Override
    public DryingTableRecipe read(Identifier id, PacketByteBuf buf) {
        Ingredient input = Ingredient.fromPacket(buf);
        int count = buf.readInt();
        ItemStack output = buf.readItemStack();
        int dryingTime = buf.readInt();

        return new DryingTableRecipe(id, input, count, output, dryingTime);
    }

    @Override
    public void write(PacketByteBuf buf, DryingTableRecipe recipe) {
        recipe.input.write(buf);
        buf.writeInt(recipe.getCount());
        buf.writeItemStack(recipe.getOutput(null));
        buf.writeInt(recipe.getDryingTime());
    }
}