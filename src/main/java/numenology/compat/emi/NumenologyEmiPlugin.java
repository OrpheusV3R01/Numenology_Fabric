package numenology.compat.emi;

import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.block.ModBlocks;
import numenology.recipe.DryingTableRecipe;
import numenology.recipe.ModRecipes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NumenologyEmiPlugin implements EmiPlugin {
    // Создаем логгер для отслеживания загрузки в консоли разработки
    private static final Logger LOGGER = LoggerFactory.getLogger("Numenology-EMI");

    public static final EmiRecipeCategory DRYING_CATEGORY = new EmiRecipeCategory(
            new Identifier(Numenology.MOD_ID, "drying"),
            EmiStack.of(ModBlocks.DRYING_TABLE)
    );

    @Override
    public void register(EmiRegistry registry) {
        LOGGER.info("==================================================");
        LOGGER.info("Инициализация EMI плагина для Numenology...");

        // 1. Регистрируем категорию и рабочую станцию
        registry.addCategory(DRYING_CATEGORY);
        registry.addWorkstation(DRYING_CATEGORY, EmiStack.of(ModBlocks.DRYING_TABLE));

        // 2. Получаем менеджеры из игры
        RecipeManager recipeManager = registry.getRecipeManager();
        MinecraftClient client = MinecraftClient.getInstance();

        // Получаем реальный менеджер реестров запущенного клиента
        DynamicRegistryManager registryManager = (client.world != null)
                ? client.world.getRegistryManager()
                : DynamicRegistryManager.EMPTY;

        try {
            // Запрашиваем рецепты сушки
            List<DryingTableRecipe> recipes = recipeManager.listAllOfType(ModRecipes.DRYING_TABLE_TYPE);
            LOGGER.info("Успешно найдено рецептов сушки в игре: {}", recipes.size());

            if (recipes.isEmpty()) {
                LOGGER.warn("Внимание: Список рецептов пуст! Проверь папки data/numenology/recipes/");
            }

            // 3. Загружаем каждый рецепт отдельно с защитой от ошибок
            for (DryingTableRecipe recipe : recipes) {
                try {
                    registry.addRecipe(new DryingEmiRecipe(recipe, registryManager));
                } catch (Exception e) {
                    LOGGER.error("Ошибка при регистрации отдельного рецепта: " + recipe.getId(), e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Критическая ошибка при попытке прочитать типы рецептов Numenology", e);
        }

        LOGGER.info("==================================================");
    }
}