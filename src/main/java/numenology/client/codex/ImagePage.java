package numenology.client.codex;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ImagePage extends BookPage {
    private final String textKey;
    private final Identifier imageTexture;
    private final int textureWidth;
    private final int textureHeight;
    private final int xOffset;
    private final int yOffset;
    private final float imgScale; // Новый параметр масштаба картинки

    /**
     * Конструктор страницы со схемой/картинкой
     */
    public ImagePage(String textKey, String imagePath, int width, int height, int xOffset, int yOffset, float imgScale) {
        this.textKey = textKey;
        this.imageTexture = new Identifier("numenology", imagePath);
        this.textureWidth = width;
        this.textureHeight = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.imgScale = imgScale;
    }

    @Override
    public void render(DrawContext ctx, TextRenderer tr, int startX, int startY, int mouseX, int mouseY, boolean isRightPage) {
        int pageX = startX + (isRightPage ? 140 : 35);
        int maxWidth = 95; // Рабочая ширина страницы книги
        int currentY = startY;

        // 1. ОТРИСОВКА ТЕКСТА (Масштаб 0.7f, Цвет 0x333333)
        if (textKey != null && !textKey.isEmpty()) {
            Text translatableText = Text.translatable(textKey);
            float textScale = 0.7f;
            int scaledWidth = (int)(maxWidth / textScale);
            var lines = tr.wrapLines(translatableText, scaledWidth);

            ctx.getMatrices().push();
            ctx.getMatrices().scale(textScale, textScale, 1f);

            int localOffsetY = 0;
            for (var line : lines) {
                ctx.drawText(tr, line,
                        (int)(pageX / textScale),
                        (int)((currentY + localOffsetY) / textScale),
                        0x333333,
                        false
                );
                localOffsetY += (tr.fontHeight + 2);
            }
            ctx.getMatrices().pop();

            // Сдвигаем Y ниже текста + зазор
            currentY += localOffsetY + 6;
        }

        // 2. ОТРИСОВКА КАРТИНКИ С УЧЕТОМ СKЕЙЛА
        // Считаем реальный размер на экране после масштабирования
        int displayedWidth = (int) (textureWidth * imgScale);

        // Центрируем отмасштабированную картинку по ширине страницы
        int imgX = pageX + (maxWidth - displayedWidth) / 2 + xOffset;
        int imgY = currentY + yOffset;

        ctx.getMatrices().push();
        // Сдвигаем матрицу в точку рисования картинки
        ctx.getMatrices().translate(imgX, imgY, 0);
        // Применяем масштаб из JSON
        ctx.getMatrices().scale(imgScale, imgScale, 1f);

        // Рисуем текстуру (в локальных координатах 0, 0 измененной матрицы)
        ctx.drawTexture(imageTexture, 0, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);

        ctx.getMatrices().pop();
    }
}