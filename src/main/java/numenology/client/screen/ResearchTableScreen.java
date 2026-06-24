package numenology.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import numenology.Numenology;
import numenology.network.packet.C2SCompleteResearchPacket;
import numenology.screen.ResearchTableScreenHandler;

public class ResearchTableScreen extends HandledScreen<ResearchTableScreenHandler> {



    private static final Identifier TEXTURE =
            new Identifier(Numenology.MOD_ID, "textures/gui/research_table.png");

    private ButtonWidget researchButton;
    private numenology.client.codex.CodexPanel codexPanel;

    // 🔥 единые параметры (чтобы больше не ломалось)
    private static final float SCALE = 0.6f;
    private static final int PANEL_X = 70;
    private static final int PANEL_Y = 27;

    public ResearchTableScreen(ResearchTableScreenHandler handler,
                               PlayerInventory inventory,
                               Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 300;
        this.backgroundHeight = 240;

        // Указываем свой X и Y для надписи "Инвентарь"
        this.playerInventoryTitleX = 70;
        this.playerInventoryTitleY = this.backgroundHeight - 100;

    }

    @Override
    protected void init() {
        super.init();

        int tableX = 20;
        int tableY = 15;

        int buttonWidth = 60;
        int slotsCenterX = tableX + 28;

        researchButton = ButtonWidget.builder(
                Text.translatable("gui.numenology.research_table.button.research"),
                button -> onResearchPressed()
        ).dimensions(
                this.x + slotsCenterX - buttonWidth / 2,
                this.y + tableY + 22,
                buttonWidth,
                18
        ).build();

        addDrawableChild(researchButton);

        codexPanel = new numenology.client.codex.CodexPanel(
                120,
                10,
                170,
                120
        );
    }

    private void onResearchPressed() {
        if (codexPanel != null) {
            String selected = codexPanel.getSelectedResearch();
            if (selected != null) {
                C2SCompleteResearchPacket.send(selected);
            }
        }
    }

    // 🔥 ПРОВЕРКА НАЛИЧИЯ CODEX
    private boolean hasCodex() {
        return !handler.getSlot(0).getStack().isEmpty();
    }

    private boolean canResearch() {

        var codex = handler.getSlot(0).getStack();
        var paper = handler.getSlot(1).getStack();
        var tools = handler.getSlot(2).getStack();

        if (codex.isEmpty() || paper.isEmpty() || tools.isEmpty()) {
            return false;
        }

        if (client == null || client.player == null) return false;

        var data = numenology.research.KnowledgeManager
                .getUnsafeClient(client.player.getUuid());

        return !data.getAllSuspected().isEmpty();
    }

    @Override
    protected void handledScreenTick() {
        super.handledScreenTick();

        if (researchButton != null) {
            researchButton.active = canResearch();
        }

        if (codexPanel != null) {
            codexPanel.update();
        }
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Параметры: (Текстура, X, Y, U, V, Ширина_GUI, Высота_GUI, Ширина_Файла, Высота_Файла)
        // Мы берем кусок 300x240 из верхнего левого угла (0,0) файла 512x512
        context.drawTexture(TEXTURE, x, y, 0, 0, 300, 240, 512, 512);

        int tableX = x + 20;
        int tableY = y + 15;

        drawSlot(context, tableX, tableY);
        drawSlot(context, tableX + 20, tableY);
        drawSlot(context, tableX + 40, tableY);
    }

    private void drawSlot(DrawContext context, int x, int y) {
        context.fill(x - 1, y - 1, x + 17, y + 17, 0xFF000000);
        context.fill(x, y, x + 16, y + 16, 0xFF8B8B8B);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {

        int invX = (backgroundWidth - 162) / 2;
        int invY = 150;

        context.drawText(this.textRenderer,
                Text.translatable("container.inventory"),
                this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);

        // 🔥 РЕНДЕР ТОЛЬКО ЕСЛИ ЕСТЬ CODEX
        if (codexPanel != null && hasCodex()) {

            context.getMatrices().push();

            context.getMatrices().translate(PANEL_X, PANEL_Y, 0);
            context.getMatrices().scale(SCALE, SCALE, 1f);

            int adjustedMouseX = (int)((mouseX - this.x - PANEL_X) / SCALE);
            int adjustedMouseY = (int)((mouseY - this.y - PANEL_Y) / SCALE);

            codexPanel.render(context, adjustedMouseX, adjustedMouseY);

            context.getMatrices().pop();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        // 🔥 КЛИКИ ТОЛЬКО ЕСЛИ ЕСТЬ CODEX
        if (codexPanel != null && hasCodex()) {

            double localMouseX = mouseX - this.x;
            double localMouseY = mouseY - this.y;

            double adjustedMouseX = (localMouseX - PANEL_X) / SCALE;
            double adjustedMouseY = (localMouseY - PANEL_Y) / SCALE;

            if (codexPanel.mouseClicked(adjustedMouseX, adjustedMouseY)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}