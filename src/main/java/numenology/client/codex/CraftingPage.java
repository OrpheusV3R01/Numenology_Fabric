package numenology.client.codex;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.Optional;

public class CraftingPage extends BookPage {
    private final Identifier recipeId;
    private CraftingRecipe cachedRecipe;
    private final int xOffset;
    private final int yOffset;
    private final String textKey;

    private static final Identifier CRAFT_GRID_TEX =
            new Identifier("numenology", "textures/gui/crafting_grid.png");

    /**
     * Основной constructor для комбинированных страниц (Текст + Рецепт)
     */
    public CraftingPage(String recipeId, int xOffset, int yOffset, String textKey) {
        this.recipeId = new Identifier(recipeId);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.textKey = textKey;
    }

    /**
     * Конструктор с поддержкой смещений (без текста)
     */
    public CraftingPage(String recipeId, int xOffset, int yOffset) {
        this(recipeId, xOffset, yOffset, "");
    }

    /**
     * Конструктор по умолчанию для обратной совместимости с CodexPanel
     */
    public CraftingPage(String recipeId) {
        this(recipeId, 0, 0, "");
    }

    private CraftingRecipe getRecipe() {
        if (cachedRecipe == null) {
            var world = MinecraftClient.getInstance().world;
            if (world != null) {
                Optional<?> recipe = world.getRecipeManager().get(recipeId);
                if (recipe.isPresent() && recipe.get() instanceof CraftingRecipe) {
                    this.cachedRecipe = (CraftingRecipe) recipe.get();
                }
            }
        }
        return cachedRecipe;
    }

    @Override
    public void render(DrawContext ctx, TextRenderer tr, int startX, int startY, int mouseX, int mouseY, boolean isRightPage) {
        // Базовая X координата страницы книги
        int pageX = startX + (isRightPage ? 140 : 35);
        int maxWidth = 95; // Доступная ширина для текста
        int currentY = startY;

        // 1. ОТРИСОВКА ТЕКСТА ОПИСАНИЯ РЕЦЕПТА (Масштаб 0.75f)
        if (textKey != null && !textKey.isEmpty()) {
            Text translatableText = Text.translatable(textKey);

            float textScale = 0.75f; // Стандартный масштаб для страниц описания
            int scaledWidth = (int)(maxWidth / textScale);
            var lines = tr.wrapLines(translatableText, scaledWidth);

            ctx.getMatrices().push();
            ctx.getMatrices().scale(textScale, textScale, 1f);

            int localOffsetY = 0;
            for (var line : lines) {
                ctx.drawText(tr, line,
                        (int)(pageX / textScale),
                        (int)((currentY + localOffsetY) / textScale),
                        0x333333, // Чернильный цвет
                        false
                );
                localOffsetY += (tr.fontHeight + 2);
            }
            ctx.getMatrices().pop();

            // Сдвигаем текущую Y-координату для сетки крафта ниже текста
            currentY += localOffsetY + 6;
        }

        // 2. НАСТРОЙКА РАЗМЕРОВ И КООРДИНАТ СЕТКИ КРАФТА (Новый холст 128x128)
        int textureWidth = 128;
        int textureHeight = 128;

        // Компактный масштаб для сетки крафта, ужимает 128x128 до аккуратных 64x64 на экране
        float craftScale = 0.5f;
        int displayedWidth = (int) (textureWidth * craftScale);

        // Автоматическое центрирование по ширине страницы + кастомные JSON смещения
        int gridX = pageX + (maxWidth - displayedWidth) / 2 + xOffset;
        int gridY = currentY + yOffset;

        CraftingRecipe recipe = getRecipe();
        if (recipe == null) {
            ctx.drawText(tr, "Рецепт не найден...", pageX, gridY + 10, 0xFF0000, false);
            return;
        }

        // ОТКРЫВАЕМ МАТРИЦУ ДЛЯ СИНХРОННОГО МАСШТАБИРОВАНИЯ
        ctx.getMatrices().push();
        ctx.getMatrices().translate(gridX, gridY, 0);
        ctx.getMatrices().scale(craftScale, craftScale, 1f);

        // Рисуем подложку вклейки (в локальных координатах 0, 0 измененной матрицы)
        ctx.drawTexture(CRAFT_GRID_TEX, 0, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        // 3. КООРДИНАТЫ СЛОТОВ ДЛЯ ТВОЕГО ШАБЛОНА 128x128
        // Рассчитано под сетку 3х3 из ячеек 24x24 (внутреннее поле 18x18 для предметов),
        // которая идеально отцентрирована посередине холста 128x128 (отступы по краям ~28 пикселей).
        // Матрица автоматически уменьшит эти значения в два раза, и предметы встанут ровно в ячейки!
        int[][] slotCoordinates = new int[][] {
                {32, 28}, {57, 28}, {82, 28},  // Верхний ряд (слоты 0, 1, 2)
                {32, 52}, {57, 52}, {82, 52},  // Средний ряд  (слоты 3, 4, 5)
                {32, 77}, {57, 77}, {82, 77}   // Нижний ряд   (слоты 6, 7, 8)
        };

        var ingredients = recipe.getIngredients();

        // 4. ОТРИСОВКА МАТЕРИАЛОВ В СЛОТАХ ВНУТРИ МАТРИЦЫ
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (!ingredient.isEmpty()) {
                int localX = slotCoordinates[i][0];
                int localY = slotCoordinates[i][1];

                ItemStack[] matchingStacks = ingredient.getMatchingStacks();
                if (matchingStacks.length > 0) {
                    int index = (int) ((System.currentTimeMillis() / 1000) % matchingStacks.length);

                    // Рендерим иконку материала в локальных координатах
                    ctx.drawItem(matchingStacks[index], localX, localY);
                }
            }
        }

        // Закрываем матрицу крафта
        ctx.getMatrices().pop();
    }
}