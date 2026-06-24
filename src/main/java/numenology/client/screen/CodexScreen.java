package numenology.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import numenology.client.codex.CodexPanel;

public class CodexScreen extends Screen {

    private CodexPanel panel;

    public CodexScreen() {
        super(Text.literal("Codex"));
    }

    @Override
    protected void init() {

        int bookWidth = 260;
        int bookHeight = 180;

        int bookX = this.width / 2 - bookWidth / 2;
        int bookY = this.height / 2 - bookHeight / 2;

        panel = new CodexPanel(bookX, bookY, bookWidth, bookHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);

        if (panel != null) {
            panel.update();
            panel.render(context, mouseX, mouseY);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        if (panel != null && panel.mouseClicked(mouseX, mouseY)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false; // Отключает паузу мира при открытом интерфейсе кодекса
    }
}