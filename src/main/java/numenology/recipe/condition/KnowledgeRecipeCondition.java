package numenology.recipe.condition;

import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.resource.conditions.v1.ConditionJsonProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import numenology.research.KnowledgeManager;

public class KnowledgeRecipeCondition {
    public static final Identifier ID = new Identifier("numenology", "has_knowledge");

    public static void register() {
        ResourceConditions.register(ID, (jsonObject) -> {
            String knowledgeId = jsonObject.get("knowledge").getAsString();

            // Это условие проверяется при загрузке ресурсов.
            // Чтобы оно работало для конкретного игрока, мы будем использовать
            // это ID в кастомном предикате или фильтре.
            return true;
        });
    }
}