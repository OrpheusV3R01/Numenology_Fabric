package numenology.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import numenology.Numenology;
import numenology.screen.DryingTableScreenHandler;

public class DryingTableScreen extends HandledScreen<DryingTableScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(Numenology.MOD_ID, "textures/gui/drying_table.png");

    public DryingTableScreen(DryingTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176;
        this.backgroundHeight = 186;

        this.titleX = 6;
        this.titleY = 6;

        this.playerInventoryTitleX = 8;
        this.playerInventoryTitleY = this.backgroundHeight - 111;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        // Фон GUI
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight, 512, 512);

        // ===== ПОЛОСКА ПРОГРЕССА (Горизонтальная, слева направо) =====
        int progress = handler.getProgress();
        int maxProgress = handler.getMaxProgress();

// Настройки размеров твоей шкалы (подставь свои значения под текстуру)
        int startX = 73;       // Координата X начала шкалы на экране GUI
        int startY = 23;       // Координата Y начала шкалы на экране GUI
        int uCoordinate = 179; // Координата X (U) начала ЗАПОЛНЕННОЙ шкалы на текстуре 512x512
        int vCoordinate = 0;   // Координата Y (V) начала ЗАПОЛНЕННОЙ шкалы на текстуре 512x512
        int visualWidth = 30;  // Полная ШИРИНА готовой стрелочки/шкалы в пикселях
        int visualHeight = 20; // Полная ВЫСОТА стрелочки/шкалы в пикселях

        if (maxProgress > 0 && progress > 0) {
            // Вычисляем, сколько пикселей в ширину должно быть закрашено прямо сейчас
            int scaledWidth = (progress * visualWidth) / maxProgress;

            // Рисуем закрашенную часть слева направо
            context.drawTexture(TEXTURE,
                    x + startX,          // Позиция X на экране (остается на месте)
                    y + startY,          // Позиция Y на экране (остается на месте)
                    uCoordinate,         // U на текстуре
                    vCoordinate,         // V на текстуре
                    scaledWidth,         // ТЕКУЩАЯ ширина отрисовки (растет от 0 до visualWidth)
                    visualHeight,        // Высота шкалы
                    512, 512             // Размер твоей сетки текстуры
            );
        }
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // Заголовок
        context.drawText(this.textRenderer,
                Text.translatable("gui.numenology.drying_table.title"),
                this.titleX, this.titleY, 4210752, false);

        // Надпись инвентаря
        context.drawText(this.textRenderer,
                this.playerInventoryTitle,
                this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);
    }
}