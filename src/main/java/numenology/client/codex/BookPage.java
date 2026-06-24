package numenology.client.codex;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public abstract class BookPage {
    // Каждая страница сама знает, как себя нарисовать.
    // isRightPage нужна, чтобы понимать, левая это страница или правая (для смещения X)
    public abstract void render(DrawContext ctx, TextRenderer tr, int leftX, int topY, int mouseX, int mouseY, boolean isRightPage);
}