package numenology.client.codex;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.text.OrderedText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import numenology.research.KnowledgeManager;
import numenology.research.KnowledgeRegistry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.Resource;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class CodexPanel {

    private final int x, y, width, height;

    private int drawX, drawY;
    private final int bookWidth = 256;
    private final int bookHeight = 256;


    private List<BookPage> cachedPages = new ArrayList<>();
    private String cachedResearch = null;

    private static final Identifier PAGE_BUTTONS =
            new Identifier("numenology", "textures/gui/page_buttons.png");

    private Map<String, Node> nodeMap;


    private enum State {
        CATEGORY,
        ENTRY
    }

    private enum NodeState {
        COMPLETED,
        SUSPECTED,
        LOCKED
    }

    private enum Category {
        BASICS,        // Основы
        ALCHEMY,       // Алхимия
        METALLURGY     // Металлургия
    }

    private Category currentCategory = Category.BASICS;

    private State state = State.CATEGORY;

    private static class Node {
        String id;
        int x;
        int y;

        Node(String id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }
    }

    private final List<String> suspectedList = new ArrayList<>();
    private final List<String> completedList = new ArrayList<>();

    private final List<Node> nodeLayout = List.of(
            new Node("numenology:first_contact_numen", 180, 45),
            new Node("numenology:hematite_processing", 180, 75),
            new Node("numenology:crucible_basics", 150, 75),
            new Node("numenology:numen_transmutation_tier1", 150, 105),

            new Node("numenology:magic_flora", 210, 50),
            new Node("numenology:nodes_and_aspects", 210, 80),
            new Node("numenology:force_unleashed", 210, 110),

            new Node("numenology:heated_forge", 180, 105),
            new Node("numenology:molten_steel", 180, 135),
            new Node("numenology:research_tools", 180, 165)

    );


    private String selectedResearch = null;
    private int pageIndex = 0;

    public CodexPanel(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        nodeMap = nodeLayout.stream()
                .collect(java.util.stream.Collectors.toMap(n -> n.id, n -> n));
    }

    private void drawPageButtons(DrawContext ctx, int mouseX, int mouseY) {

        final float scale = 0.65f;
        int buttonSize = (int)(64 * scale);

        int leftX = drawX + 20;
        int rightX = drawX + bookWidth - 60;

        int y = drawY + 162;

        // ===== ЛЕВАЯ КНОПКА =====
        boolean leftEnabled = pageIndex > 0;

        boolean leftHovered =
                mouseX >= leftX && mouseX <= leftX + buttonSize &&
                        mouseY >= y && mouseY <= y + buttonSize;

        int uLeft = 0;
        int vLeft;

        if (!leftEnabled) vLeft = 128;
        else if (leftHovered) vLeft = 64;
        else vLeft = 0;


        ctx.getMatrices().push();
        ctx.getMatrices().translate(leftX, y, 0);
        ctx.getMatrices().scale(scale, scale, 1f);

        ctx.drawTexture(PAGE_BUTTONS, 0, 0, uLeft, vLeft, 64, 64, 128, 192);

        ctx.getMatrices().pop();

        // ===== ПРАВАЯ КНОПКА =====
        boolean rightEnabled = pageIndex + 2 < cachedPages.size();

        boolean rightHovered =
                mouseX >= rightX && mouseX <= rightX + buttonSize &&
                        mouseY >= y && mouseY <= y + buttonSize;

        int uRight = 64;
        int vRight;

        if (!rightEnabled) vRight = 128;
        else if (rightHovered) vRight = 64;
        else vRight = 0;


        ctx.getMatrices().push();
        ctx.getMatrices().translate(rightX, y, 0);
        ctx.getMatrices().scale(scale, scale, 1f);

        ctx.drawTexture(PAGE_BUTTONS, 0, 0, uRight, vRight, 64, 64, 128, 192);

        ctx.getMatrices().pop();
    }

    public void update() {
        suspectedList.clear();
        completedList.clear();

        var client = MinecraftClient.getInstance();
        if (client.player == null) return;

        var data = KnowledgeManager.getUnsafeClient(client.player.getUuid());

        suspectedList.addAll(data.getAllSuspected().keySet());
        completedList.addAll(data.getCompleted());
    }

    public void render(DrawContext ctx, int mouseX, int mouseY) {

        drawX = x + (width - bookWidth) / 2;
        drawY = y + (height - bookHeight) / 2;

        // 🔥 ИЗМЕНЕНО: новая текстура 512x512
        Identifier texture = new Identifier("numenology", "textures/gui/codex_book.png");

        // Рисуем кусок 256x256 из позиции (0, 0) текстуры 512x512
        ctx.drawTexture(texture,
                drawX, drawY,           // позиция на экране
                0, 0,                   // U, V — начало в текстуре
                bookWidth, bookHeight,  // 256, 256
                512, 512);              // размер всей текстуры

        int mid = drawX + bookWidth / 2 - 6;

        drawCategoryTabs(ctx, mouseX, mouseY);

        if (state == State.CATEGORY) {
            renderCategory(ctx, mouseX, mouseY, mid);
        } else {
            renderEntry(ctx, mouseX, mouseY, mid);
        }

        if (state == State.ENTRY) {
            drawPageButtons(ctx, mouseX, mouseY);
        }
    }



    private void setCategory(Category category) {
        this.currentCategory = category;
        this.selectedResearch = null;
        this.pageIndex = 0;
        this.state = State.CATEGORY;
    }

    private void renderCategory(DrawContext ctx, int mouseX, int mouseY, int mid) {
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;

        int leftX = drawX + 35;
        int rightX = mid + 40;
        int topY = drawY + 45;
        int maxWidth = (bookWidth / 2) - 40;

        // Переменная динамического ключа (Берем из твоего нового метода)
        Text titleText = Text.translatable(getCategoryJsonKey(currentCategory, "title_key"));

        // ВОТ ЭТА СТРОКА ДОЛЖНА БЫТЬ ТУТ:
        float titleScale = 0.9f;

        // Отрисовываем заголовок
        drawWrappedScaledText(ctx, tr,
                titleText,
                leftX,
                topY,
                maxWidth,
                0x004040,
                titleScale
        );

        // Расчет высоты заголовка для динамического отступа
        int scaledTitleWidth = (int)(maxWidth / titleScale);
        var titleLines = tr.wrapLines(titleText, scaledTitleWidth);

        // Высота = (кол-во строк * высота шрифта) * масштаб + небольшой запас
        int titleHeight = (int)(titleLines.size() * (tr.fontHeight + 2) * titleScale);

        // Отрисовка описания с учетом высоты заголовка
        int descY = topY + titleHeight + 8;

        drawWrappedScaledText(ctx, tr,
                Text.translatable(getCategoryJsonKey(currentCategory, "home_page_key")),
                leftX,
                descY,
                maxWidth,
                0x004040,
                0.7f
        );

        // ... Дальше идет твой старый неизмененный код рендера линий и нод ...

        // РЕНДЕР ЛИНИЙ (ДО НОД)
        for (Node node : nodeLayout) {
            if (!belongsToCategory(node.id)) continue;

            // ПРАВКА: Если нода еще LOCKED, линию к ней не ведем
            if (getNodeState(node.id) == NodeState.LOCKED) continue;

            var entry = KnowledgeRegistry.get(node.id);
            if (entry == null) continue;

            for (String depId : entry.requirements) {
                Node parent = nodeMap.get(depId);
                if (parent == null) continue;
                if (!belongsToCategory(parent.id)) continue;

                // ПРАВКА: Рисуем связь, только если родительское исследование тоже открыто
                if (getNodeState(parent.id) != NodeState.LOCKED) {
                    drawConnection(ctx, node, parent);
                }
            }
        }

        // РЕНДЕР НОД
        for (Node node : nodeLayout) {
            if (!belongsToCategory(node.id)) continue;

            // ПРАВКА: Скрываем ноду полностью, если она еще не "заподозрена" (LOCKED)
            if (getNodeState(node.id) == NodeState.LOCKED) continue;

            int nodeX = drawX + node.x;
            int nodeY = drawY + node.y;

            drawNode(ctx, tr,
                    node.id,
                    nodeX,
                    nodeY,
                    mouseX,
                    mouseY
            );
        }
    }

    private void renderEntry(DrawContext ctx, int mouseX, int mouseY, int mid) {
        if (selectedResearch == null) return;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;

        // Загрузка через JSON
        if (!selectedResearch.equals(cachedResearch)) {
            cachedResearch = selectedResearch;
            cachedPages = loadResearchPages(selectedResearch);
            pageIndex = 0;
        }

        int leftX = drawX + 40;
        int topY = drawY + 45;

        // Заголовок рисуем ТОЛЬКО на первой странице (pageIndex == 0)
        if (pageIndex == 0) {
            drawWrappedScaledTextCentered(ctx, tr,
                    Text.translatable(KnowledgeRegistry.getTitleKey(selectedResearch)),
                    leftX + ((bookWidth / 2 - 60) / 2) + 5,
                    topY,
                    (bookWidth / 2) - 60,
                    0x2E2E2E,
                    0.9f
            );
        }

        int firstPageOffset = drawY + 70; // Там, где заголовок
        int standardPageOffset = drawY + 50; // Где заголовка нет

        // 1. Левая страница
        if (pageIndex < cachedPages.size()) {
            int y = (pageIndex == 0) ? firstPageOffset : standardPageOffset;
            cachedPages.get(pageIndex).render(ctx, tr, drawX, y, mouseX, mouseY, false);
        }

        // 2. Правая страница
        if (pageIndex + 1 < cachedPages.size()) {
            // Если это страница 1 (разворот 0-1), то правая тоже под заголовком?
            // Или только 0-я страница имеет заголовок?
            // Если заголовок только на 0-й:
            int y = standardPageOffset;
            cachedPages.get(pageIndex + 1).render(ctx, tr, drawX, y, mouseX, mouseY, true);
        }
    }

    private NodeState getNodeState(String id) {
        if (completedList.contains(id)) return NodeState.COMPLETED;
        if (suspectedList.contains(id)) return NodeState.SUSPECTED;
        return NodeState.LOCKED;
    }

    private boolean belongsToCategory(String id) {

        // 🔥 Пока у нас только одна полноценная категория — Основы
        if (currentCategory == Category.BASICS) {
            return true;
        }

        // 🔥 Остальные категории пока не реализованы — ничего не показываем
        return false;
    }

    private void drawNode(DrawContext ctx, TextRenderer tr, String id,
                          int nodeX, int nodeY,
                          int mouseX, int mouseY) {

        NodeState state = getNodeState(id);

        boolean hovered =
                mouseX >= nodeX && mouseX <= nodeX + 16 &&
                        mouseY >= nodeY && mouseY <= nodeY + 16;

        ItemStack stack = KnowledgeRegistry.getIcon(id);

        switch (state) {

            case COMPLETED -> ctx.drawItem(stack, nodeX, nodeY);

            case SUSPECTED -> {
                ctx.drawItem(stack, nodeX, nodeY);

                float time = (System.currentTimeMillis() % 2000) / 2000f;
                float pulse = (float)((Math.sin(time * Math.PI * 2) + 1f) / 2f);

                int alpha = (int)(pulse * 80);
                ctx.fill(nodeX, nodeY, nodeX + 16, nodeY + 16, (alpha << 24));
            }

            case LOCKED -> {
                ctx.fill(nodeX, nodeY, nodeX + 16, nodeY + 16, 0xFFAAAAAA);
                ctx.drawText(tr, "?", nodeX + 5, nodeY + 4, 0x333333, false);
            }
        }

        if (hovered) {
            drawScaledText(ctx, tr,
                    Text.translatable(KnowledgeRegistry.getTitleKey(id)),
                    nodeX + 20,
                    nodeY,
                    0x000000,
                    0.7f
            );
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY) {

        if (state == State.ENTRY) {

            final float scale = 0.65f;
            int buttonSize = (int)(64 * scale);

            int leftX = drawX + 20;
            int rightX = drawX + bookWidth - 60;
            int y = drawY + 165;

            // ←
            if (mouseX >= leftX && mouseX <= leftX + buttonSize &&
                    mouseY >= y && mouseY <= y + buttonSize) {
                if (pageIndex > 0) {
                    pageIndex -= 2;
                }
                return true;
            }

            // →
            // →
            if (mouseX >= rightX && mouseX <= rightX + buttonSize &&
                    mouseY >= y && mouseY <= y + buttonSize) {
                if (pageIndex + 2 < cachedPages.size()) {
                    pageIndex += 2;
                }
                return true;
            }
        }

        int tabX = drawX - 5;
        int tabY = drawY + 50;
        int tabWidth = 24;
        int tabHeight = 20;

        Category[] categories = Category.values();

        for (int i = 0; i < categories.length; i++) {

            int y = tabY + i * (tabHeight + 4);

            if (mouseX >= tabX && mouseX <= tabX + tabWidth &&
                    mouseY >= y && mouseY <= y + tabHeight) {

                setCategory(categories[i]);
                return true;
            }
        }

        int mid = drawX + bookWidth / 2 - 6;

        // 🔥 ТЕ ЖЕ КООРДИНАТЫ ЧТО И В renderCategory
        int rightX = mid + 40;
        int startX = rightX + 10;
        int topY = drawY + 55;
        int startY = topY + 10;
        int spacing = 28;

        if (state == State.CATEGORY) {
            for (Node node : nodeLayout) {
                int nodeX = drawX + node.x;
                int nodeY = drawY + node.y;

                if (inside(mouseX, mouseY, nodeX, nodeY)) {
                    NodeState nodeState = getNodeState(node.id);

                    // === ДОБАВЬ ЭТУ СТРОКУ ЗДЕСЬ (Примерно строка 529) ===
                    if (nodeState == NodeState.LOCKED) continue;
                    // ===================================================

                    if (nodeState == NodeState.COMPLETED) {
                        selectedResearch = node.id;
                        state = State.ENTRY;
                    } else if (nodeState == NodeState.SUSPECTED) {
                        selectedResearch = node.id;
                    }

                    return true;
                }
            }
        }

        if (state == State.ENTRY) {
            state = State.CATEGORY;
            return true;
        }

        return false;
    }

    private List<BookPage> loadResearchPages(String researchId) {
        List<BookPage> pages = new ArrayList<>();
        if (researchId == null) return pages;

        String pathId = researchId.contains(":") ? researchId.split(":")[1] : researchId;
        Identifier jsonPath = new Identifier("numenology", "research/" + pathId + ".json");
        MinecraftClient client = MinecraftClient.getInstance();

        try {
            Optional<Resource> resourceOpt = client.getResourceManager().getResource(jsonPath);
            if (resourceOpt.isPresent()) {
                try (InputStreamReader reader = new InputStreamReader(resourceOpt.get().getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    if (jsonObject.has("pages")) {
                        JsonArray pagesArray = jsonObject.getAsJsonArray("pages");
                        for (JsonElement element : pagesArray) {
                            JsonObject pageObj = element.getAsJsonObject();
                            String type = pageObj.get("type").getAsString();
                            if ("text".equals(type)) {
                                String textKey = pageObj.get("text").getAsString();
                                pages.add(new TextPage(textKey));
                            }
                            else if ("crafting".equals(type)) {
                                String recipeId = pageObj.get("recipe").getAsString();
                                String textKey = pageObj.has("text") ? pageObj.get("text").getAsString() : "";


                                // Читаем смещения из JSON. Если их нет в файле исследования — по дефолту будет 0
                                int xOffset = pageObj.has("x_offset") ? pageObj.get("x_offset").getAsInt() : 0;
                                int yOffset = pageObj.has("y_offset") ? pageObj.get("y_offset").getAsInt() : 0;

                                pages.add(new CraftingPage(recipeId, xOffset, yOffset, textKey));
                            }
                            else if ("image".equals(type)) {
                                String textKey = pageObj.has("text") ? pageObj.get("text").getAsString() : "";
                                String imagePath = pageObj.get("image").getAsString();

                                int imgWidth = pageObj.has("width") ? pageObj.get("width").getAsInt() : 128;
                                int imgHeight = pageObj.has("height") ? pageObj.get("height").getAsInt() : 128;

                                int xOffset = pageObj.has("x_offset") ? pageObj.get("x_offset").getAsInt() : 0;
                                int yOffset = pageObj.has("y_offset") ? pageObj.get("y_offset").getAsInt() : 0;

                                // Читаем масштаб, если его нет — ставим по умолчанию 1.0f (100% размер)
                                float scale = pageObj.has("scale") ? pageObj.get("scale").getAsFloat() : 1.0f;

                                pages.add(new ImagePage(textKey, imagePath, imgWidth, imgHeight, xOffset, yOffset, scale));
                            }
                        }
                    }
                }
            } else {
                // Если файла нет, используем старый ключ по умолчанию
                pages.add(new TextPage(KnowledgeRegistry.getDescKey(researchId)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            pages.add(new TextPage("Ошибка чтения JSON: " + researchId));
        }

        if (pages.isEmpty()) {
            pages.add(new TextPage(KnowledgeRegistry.getDescKey(researchId)));
        }

        return pages;
    }

    private boolean inside(double mx, double my, int x, int y) {
        return mx >= x && mx <= x + 16 && my >= y && my <= y + 16;
    }

    public String getSelectedResearch() {
        return selectedResearch;
    }

    public boolean isSelectedCompleted() {
        return completedList.contains(selectedResearch);
    }

    private void drawScaledText(DrawContext ctx, TextRenderer tr, Text text,
                                int x, int y, int color, float scale) {

        ctx.getMatrices().push();
        ctx.getMatrices().scale(scale, scale, 1f);

        ctx.drawText(tr, text,
                (int)(x / scale),
                (int)(y / scale),
                color,
                false
        );

        ctx.getMatrices().pop();
    }

    private void drawConnection(DrawContext ctx, Node a, Node b) {

        int x1 = drawX + a.x + 8;
        int y1 = drawY + a.y + 8;

        int x2 = drawX + b.x + 8;
        int y2 = drawY + b.y + 8;

        drawLine(ctx, x1, y1, x2, y2);
    }

    private void drawLine(DrawContext ctx, int x1, int y1, int x2, int y2) {

        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        for (int i = 0; i <= steps; i++) {
            int x = x1 + dx * i / steps;
            int y = y1 + dy * i / steps;

            ctx.fill(x, y, x + 1, y + 1, 0xFF444444);
        }
    }

    private void drawWrappedScaledText(DrawContext ctx, TextRenderer tr, Text text,
                                       int x, int y, int maxWidth,
                                       int color, float scale) {

        int scaledWidth = (int)(maxWidth / scale);
        var lines = tr.wrapLines(text, scaledWidth);

        int offsetY = 0;

        ctx.getMatrices().push();
        ctx.getMatrices().scale(scale, scale, 1f);

        for (var line : lines) {
            ctx.drawText(tr, line,
                    (int)(x / scale),
                    (int)((y + offsetY) / scale),
                    color,
                    false
            );
            offsetY += (int)((tr.fontHeight + 2));
        }


        ctx.getMatrices().pop();
    }

    private void drawWrappedScaledTextCentered(DrawContext ctx, TextRenderer tr, Text text,
                                               int x, int y, int maxWidth,
                                               int color, float scale) {

        int scaledWidth = (int)(maxWidth / scale);
        var lines = tr.wrapLines(text, scaledWidth);

        int offsetY = 0;

        ctx.getMatrices().push();
        ctx.getMatrices().scale(scale, scale, 1f);

        for (var line : lines) {

            int lineWidth = tr.getWidth(line);

            ctx.drawText(tr, line,
                    (int)((x - lineWidth / 2) / scale),
                    (int)((y + offsetY) / scale),
                    color,
                    false
            );

            offsetY += (tr.fontHeight + 2);
        }

        ctx.getMatrices().pop();
    }

    private void drawCategoryTabs(DrawContext ctx, int mouseX, int mouseY) {

        TextRenderer tr = MinecraftClient.getInstance().textRenderer;

        int tabX = drawX - 7;
        int tabY = drawY + 50;
        int tabWidth = 24;
        int tabHeight = 20;

        Category[] categories = Category.values();

        for (int i = 0; i < categories.length; i++) {

            Category cat = categories[i];
            int y = tabY + i * (tabHeight + 2);

            boolean hovered =
                    mouseX >= tabX && mouseX <= tabX + tabWidth &&
                            mouseY >= y && mouseY <= y + tabHeight;

            boolean active = currentCategory == cat;

            int color = active
                    ? 0xFFBFA76A
                    : hovered ? 0xFF8C7A4F : 0xFF5A5035;

            Identifier tabs = new Identifier("numenology", "textures/gui/codex_tabs.png");

            int u = 0;
            int v = 0;

            if (active) {
                v = 40;
            } else if (hovered) {
                v = 20;
            }

            ctx.drawTexture(
                    tabs,
                    tabX, y,
                    u, v,
                    tabWidth, 20,
                    32, 60
            );

            ItemStack icon = getCategoryIcon(cat);

            if (!icon.isEmpty()) {
                ctx.getMatrices().push();

                float scale = 0.75f; // уменьшаем (0.75 = 75%)
                ctx.getMatrices().translate(tabX + 10, y + 4, 0);
                ctx.getMatrices().scale(scale, scale, 1f);

                ctx.drawItem(icon, 0, 0);

                ctx.getMatrices().pop();
            }
        }
    }
    private String getCategoryShortName(Category cat) {
        return switch (cat) {
            case BASICS -> "О";
            case ALCHEMY -> "А";
            case METALLURGY -> "М";
        };
    }

    private ItemStack getCategoryIcon(Category cat) {
        String iconItemId = getCategoryJsonKey(cat, "icon_item");
        try {
            var item = net.minecraft.registry.Registries.ITEM.get(new Identifier(iconItemId));
            return new ItemStack(item);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }

    private String getCategoryJsonKey(Category cat, String fieldName) {
        String catId = switch (cat) {
            case BASICS -> "circle_1";
            case ALCHEMY -> "circle_2";
            case METALLURGY -> "circle_3";
        };

        MinecraftClient client = MinecraftClient.getInstance();
        Identifier jsonPath = new Identifier("numenology", "numenology_codex/categories.json");

        try {
            Optional<Resource> resource = client.getResourceManager().getResource(jsonPath);
            if (resource.isPresent()) {
                try (InputStreamReader reader = new InputStreamReader(resource.get().getInputStream(), StandardCharsets.UTF_8)) {
                    JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray categoriesArray = jsonObject.getAsJsonArray("categories");

                    for (JsonElement element : categoriesArray) {
                        JsonObject catObj = element.getAsJsonObject();
                        if (catId.equals(catObj.get("id").getAsString())) {
                            return catObj.get(fieldName).getAsString();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Запасной вариант, если JSON не прочитался
        return "numenology.category.basics.title";
    }

}