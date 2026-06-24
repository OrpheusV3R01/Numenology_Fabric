package numenology.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.screen.NumenSmelteryScreenHandler;

public class NumenSmelteryScreen extends HandledScreen<NumenSmelteryScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier(Numenology.MOD_ID, "textures/gui/numen_smeltery.png");

    public NumenSmelteryScreen(NumenSmelteryScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 186;

        this.titleX = 6; // Отступ заголовка слева
        this.titleY = 6; // Отступ заголовка сверху

        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.backgroundHeight - 92; // Позиция надписи "Инвентарь"
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // ИСПРАВЛЕНИЕ 1: В новых версиях метод принимает только DrawContext
        this.renderBackground(context);

        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    // NumenSmelteryScreen.java

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // Фон
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 512, 512);

        int progress = handler.getScaledProgress();
        // ВНИМАНИЕ: Если в игре он заполняется только до середины, значит
        // в Handler стоит ограничение (например, 24).
        // Давай здесь вручную укажем, сколько МАКСИМУМ выдает твой Handler.
        int maxProgressFromHandler = 24; // Поменяй на 40, если исправишь в Handler (см. ниже)
        int visualHeight = 42;          // А это реальная высота иконки в Photoshop

        if (progress > 0) {
            // Рассчитываем реальный визуальный прогресс для 40 пикселей
            int scaledVisualProgress = (progress * visualHeight) / maxProgressFromHandler;

            context.drawTexture(TEXTURE,
                    x + 82, y + 17 + (visualHeight - scaledVisualProgress), // Позиция
                    179, visualHeight - scaledVisualProgress,               // Текстура
                    13, scaledVisualProgress,                               // Ширина 13 (чтобы не было щели)
                    512, 512);
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Используем созданные нами ключи перевода
        Text line1 = Text.translatable("gui.numenology.smeltery.line1");
        Text line2 = Text.translatable("gui.numenology.smeltery.line2");

        // Отрисовка первой строки (titleY обычно равен 6)
        context.drawText(this.textRenderer, line1, this.titleX, this.titleY, 4210752, false);

        // Отрисовка второй строки (titleY + 10 пикселей вниз)
        context.drawText(this.textRenderer, line2, this.titleX, this.titleY + 10, 4210752, false);

        // Надпись "Инвентарь" (playerInventoryTitle) отрисовывается автоматически ниже
        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);
    }
}