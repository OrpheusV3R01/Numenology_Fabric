package numenology.recipe;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class NumenSmelteryRecipeSerializer implements RecipeSerializer<NumenSmelteryRecipe> {

    @Override
    public NumenSmelteryRecipe read(Identifier id, JsonObject json) {

        Ingredient inputA = Ingredient.fromJson(json.get("inputA"));
        int countA = JsonHelper.getInt(json.getAsJsonObject("inputA"), "count");

        Ingredient inputB = Ingredient.fromJson(json.get("inputB"));
        int countB = JsonHelper.getInt(json.getAsJsonObject("inputB"), "count");

        ItemStack output = net.minecraft.recipe.ShapedRecipe.outputFromJson(json.getAsJsonObject("result"));

        return new NumenSmelteryRecipe(id, inputA, countA, inputB, countB, output);
    }

    @Override
    public NumenSmelteryRecipe read(Identifier id, PacketByteBuf buf) {

        Ingredient inputA = Ingredient.fromPacket(buf);
        int countA = buf.readInt();

        Ingredient inputB = Ingredient.fromPacket(buf);
        int countB = buf.readInt();

        ItemStack output = buf.readItemStack();

        return new NumenSmelteryRecipe(id, inputA, countA, inputB, countB, output);
    }

    @Override
    public void write(PacketByteBuf buf, NumenSmelteryRecipe recipe) {

        recipe.inputA.write(buf);
        buf.writeInt(recipe.getCountA());

        recipe.inputB.write(buf);
        buf.writeInt(recipe.getCountB());

        buf.writeItemStack(recipe.getOutput(null));
    }
}