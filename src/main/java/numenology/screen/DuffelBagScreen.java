package numenology.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import numenology.Numenology;

@Environment(EnvType.CLIENT)
public class DuffelBagScreen extends HandledScreen<DuffelBagScreenHandler> {
    // Путь к твоей кастомной текстуре 512х512
    private static final Identifier TEXTURE = new Identifier(Numenology.MOD_ID, "textures/gui/duffel_bag_gui.png");

    public DuffelBagScreen(DuffelBagScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);

        // Физические размеры самого окна интерфейса на экране (область, которую занимает HUD)
        // По дефолту оставляем ванильные 176х166. Если твой HUD шире или выше, измени эти числа здесь.
        this.backgroundWidth = 176;
        this.backgroundHeight = 166;

        // Смещение надписи "Инвентарь" игрока относительно низа окна
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Вычисляем центр экрана, чтобы интерфейс не съезжал на разных мониторах
        int x = (this.width - this.backgroundWidth) / 2;
        int y = (this.height - this.backgroundHeight) / 2;

        // Координаты текстуры (u, v) в начале координат — это 0, 0.
        int u = 0;
        int v = 0;

        // Размеры твоего графического файла (Texture Width и Texture Height)
        int textureWidth = 512;
        int textureHeight = 512;

        // Используем метод drawTexture, который учитывает нестандартный размер файла (512x512)
        context.drawTexture(TEXTURE, x, y, u, v, this.backgroundWidth, this.backgroundHeight, textureWidth, textureHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Отрисовка стандартного полупрозрачного заднего фона игры
        this.renderBackground(context);

        super.render(context, mouseX, mouseY, delta);

        // Отрисовка всплывающих подсказок над предметами
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }
}