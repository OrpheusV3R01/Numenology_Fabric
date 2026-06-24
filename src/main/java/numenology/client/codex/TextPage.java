package numenology.client.codex;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import java.util.List;

public class TextPage extends BookPage {
    private final String translationKey;

    public TextPage(String translationKey) {
        this.translationKey = translationKey;
    }

    @Override
    public void render(DrawContext ctx, TextRenderer tr, int leftX, int topY, int mouseX, int mouseY, boolean isRightPage) {
        float scale = 0.75f;
        int xOffset = isRightPage ? 145 : 30;
        int availableWidth = 85;
        int maxHeight = (int) (180 / scale); // Максимальная высота текстового блока

        ctx.getMatrices().push();
        ctx.getMatrices().translate(leftX + xOffset, topY, 0);
        ctx.getMatrices().scale(scale, scale, 1.0f);

        Text text = Text.translatable(translationKey);
        int maxWidth = (int) (availableWidth / scale);
        List<OrderedText> lines = tr.wrapLines(text, maxWidth);

        int currentY = 0;
        int lineHeight = tr.fontHeight + 3;

        for (OrderedText line : lines) {
            // Проверка: если следующая строка выйдет за пределы высоты — прекращаем отрисовку
            if (currentY + lineHeight > maxHeight) break;

            ctx.drawText(tr, line, 0, currentY, 0x3f3f3f, false);
            currentY += lineHeight;
        }

        ctx.getMatrices().pop();
    }
}