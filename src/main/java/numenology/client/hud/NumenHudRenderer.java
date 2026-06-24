package numenology.client.hud;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import numenology.Numenology;
import numenology.energy.NumenChunkData;
import numenology.energy.NumenEnergyManager;
import numenology.item.ModItems;
import numenology.nodes.NumenNodeBlockEntity;

public class NumenHudRenderer {

    private static final Identifier HUD_TEXTURE =
            Identifier.of(Numenology.MOD_ID, "textures/gui/hud/numen_hud.png");

    public static void init() {
        HudRenderCallback.EVENT.register(NumenHudRenderer::render);
    }

    private static void drawMultilineText(DrawContext context, String text, int x, int y, int color, boolean shadow) {
        String[] lines = text.split("\n");
        MinecraftClient client = MinecraftClient.getInstance();
        for (int i = 0; i < lines.length; i++) {
            context.drawText(client.textRenderer, Text.literal(lines[i]), x, y + (i * 12), color, shadow);
        }
    }

    private static void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;



        PlayerEntity player = client.player;

        // 1. Проверяем монокуляр в Trinkets
        boolean hasMonocular = TrinketsApi.getTrinketComponent(player)
                .map(comp -> comp.isEquipped(ModItems.MONOCULAR))
                .orElse(false);

        // 2. Проверяем, держит ли игрок Нуменометр в руках
        boolean holdingNumenometer = player.getMainHandStack().isOf(ModItems.NUMENOMETER)
                || player.getOffHandStack().isOf(ModItems.NUMENOMETER);

        // Если у игрока нет ни монокуляра, ни нуменометра — выходим
        if (!hasMonocular && !holdingNumenometer) return;


        // ==========================================
        // 🌍 ЧАСТЬ 1: Получение данных чанка
        // ==========================================
        ChunkPos chunk = new ChunkPos(player.getBlockPos());
        NumenChunkData data = null;

        if (client.getServer() != null) {
            ServerWorld serverWorld = client.getServer().getWorld(client.world.getRegistryKey());
            if (serverWorld != null) {
                data = NumenEnergyManager.getChunk(serverWorld, chunk);
            }
        }

        if (data != null) {
            context.getMatrices().push();
            context.getMatrices().scale(0.5f, 0.5f, 0.5f);

            drawFullFlask(context, 0, 0, 0, 66, data.getEnergy(), data.getMaxEnergy(), 2, 17);
            drawFullFlask(context, 70, 0, 55, 102, data.getMiasma(), 100, 1, 17);
            drawFullFlask(context, 140, 0, 110, 138, data.getStability(), 100, 0, 17);

            context.getMatrices().pop();
        }

        // ==========================================
        // 🔮 ЧАСТЬ 2: Отображение Узлов (Скрутка)
        // ==========================================
        if (client.crosshairTarget != null && client.crosshairTarget.getType() == net.minecraft.util.hit.HitResult.Type.BLOCK) {
            net.minecraft.util.hit.BlockHitResult blockHit = (net.minecraft.util.hit.BlockHitResult) client.crosshairTarget;
            BlockPos targetPos = blockHit.getBlockPos();

            int scrollX = 10;
            int scrollY = 40;

            if (client.world.getBlockEntity(targetPos) instanceof NumenNodeBlockEntity) {
                context.getMatrices().push();

                float scrollScale = 0.75f;
                context.getMatrices().scale(scrollScale, scrollScale, scrollScale);

                // Рисуем фон скрутки
                context.drawTexture(
                        HUD_TEXTURE,
                        scrollX, scrollY,
                        67, 43, 115, 134,
                        256, 256
                );

                // 🔮 ОТРИСОВКА ПОЛОСОК АСПЕКТОВ ВНУТРИ СКРУТКИ
                if (client.world.getBlockEntity(targetPos) instanceof NumenNodeBlockEntity nodeEntity) {
                    java.util.Map<numenology.energy.NumenAspect, Integer> nodeAspects = nodeEntity.getAllAspects();

                    int maxNumen = nodeAspects.getOrDefault(numenology.energy.ModAspects.NUMEN, 100);
                    int maxAspectCapacity = 100;

                    int numenAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.NUMEN, 0);
                    if (numenAmount > 0) {
                        drawAspectFlask(context, scrollX + 28, scrollY + 41, 177, 0, numenAmount, maxNumen);
                    }
                    int lumenAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.LUMEN, 0);
                    if (lumenAmount > 0) {
                        drawAspectFlask(context, scrollX + 44, scrollY + 39, 189, 0, lumenAmount, maxAspectCapacity);
                    }
                    int umbraAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.UMBRA, 0);
                    if (umbraAmount > 0) {
                        drawAspectFlask(context, scrollX + 65, scrollY + 39, 185, 0, umbraAmount, maxAspectCapacity);
                    }
                    int infungumAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.INFUNGUM, 0);
                    if (infungumAmount > 0) {
                        drawAspectFlask(context, scrollX + 80, scrollY + 39, 181, 0, infungumAmount, maxAspectCapacity);
                    }
                    int potentiaAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.POTENTIA, 0);
                    if (potentiaAmount > 0) {
                        drawAspectFlask(context, scrollX + 28, scrollY + 73, 193, 0, potentiaAmount, maxAspectCapacity);
                    }
                    int antiquusAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.ANTIQUUS, 0);
                    if (antiquusAmount > 0) {
                        drawAspectFlask(context, scrollX + 80, scrollY + 73, 197, 0, antiquusAmount, maxAspectCapacity);
                    }
                    int vitaAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.VITA, 0);
                    if (vitaAmount > 0) {
                        drawAspectFlask(context, scrollX + 44, scrollY + 73, 201, 0, vitaAmount, maxAspectCapacity);
                    }
                    int mechaAmount = nodeAspects.getOrDefault(numenology.energy.ModAspects.MECHA, 0);
                    if (mechaAmount > 0) {
                        drawAspectFlask(context, scrollX + 65, scrollY + 73, 205, 0, mechaAmount, maxAspectCapacity);
                    }
                }
                context.getMatrices().pop();
            }

            // ==========================================================
            // 📊 ВЫВОД ИНФОРМАЦИОННЫХ СТРОК СТРОГО СПРАВА В HUD (ПКМ)
            // ==========================================================
            if (client.options.useKey.isPressed() && holdingNumenometer) {
                int screenWidth = context.getScaledWindowWidth();
                int screenHeight = context.getScaledWindowHeight();

                // Входим в матрицу масштабирования СРАЗУ, чтобы и фон, и текст сжимались вместе
                context.getMatrices().push();
                context.getMatrices().scale(0.9f, 0.9f, 1.0f);

                // Вычисляем виртуальное разрешение экрана с учетом масштаба 0.9f
                int scaledWidth = (int) (screenWidth / 0.9f);
                int scaledHeight = (int) (screenHeight / 0.9f);

                // Конфигурация размеров внутри масштабированной сетки
                int panelWidth = 145;
                int textX = scaledWidth - panelWidth + 8;
                int textY = scaledHeight / 2 - 35;

                // Считаем строчки для динамической высоты фона
                int totalLines = 0;
                boolean isNode = client.world.getBlockEntity(targetPos) instanceof NumenNodeBlockEntity;

                if (isNode) {
                    NumenNodeBlockEntity node = (NumenNodeBlockEntity) client.world.getBlockEntity(targetPos);
                    totalLines = 3; // Название узла, NUMEN, "Аспекты:"
                    for (int amount : node.getAllAspects().values()) {
                        if (amount > 0) totalLines++;
                    }
                    if (node.getNodeType() == numenology.nodes.NodeType.PURE) {
                        totalLines = 4; // Фиксированная строка для чистого узла
                    }
                } else if (data != null) {
                    totalLines = 4; // Заголовок чанка + 3 строки параметров
                }

                // Рендерим подложку (теперь она идеально адаптирована под размер текста)
                if (totalLines > 0) {
                    // Используем 10 вместо 11, чтобы убрать лишний "воздух" между строками
                    // Padding теперь работает симметрично
                    int verticalPadding = 4;
                    int lineHeight = 10;
                    int backgroundHeight = (totalLines * lineHeight) + verticalPadding;

                    context.fill(
                            scaledWidth - panelWidth, textY - 3, // Сдвигаем верхнюю границу чуть выше
                            scaledWidth - 4, textY + backgroundHeight - 2, // Подрезаем нижнюю границу
                            0x80000000
                    );
                }

                int curX = textX;
                int curY = textY;

                // ВЫВОД ДАННЫХ УЗЛА
                if (isNode && client.world.getBlockEntity(targetPos) instanceof NumenNodeBlockEntity node) {
                    numenology.nodes.NodeType type = node.getNodeType();
                    java.util.Map<numenology.energy.NumenAspect, Integer> nodeAspects = node.getAllAspects();
                    int currentNumenMax = nodeAspects.getOrDefault(numenology.energy.ModAspects.NUMEN, 0);

                    String translationKey = "nodetype.numenology." + type.getId();
                    context.drawText(client.textRenderer,
                            Text.translatable(translationKey).formatted(net.minecraft.util.Formatting.GOLD),
                            curX, curY, 0xFFFFFFFF, true);
                    curY += 12;

                    int numenColor = numenology.energy.ModAspects.NUMEN.getColor() | 0xFF000000;
                    context.drawText(client.textRenderer,
                            Text.literal("NUMEN: " + currentNumenMax),
                            curX, curY, numenColor, true);
                    curY += 12;

                    context.drawText(client.textRenderer,
                            Text.translatable("chat.numenology.numenometer.aspects").formatted(net.minecraft.util.Formatting.DARK_AQUA),
                            curX, curY, 0xFFFFFFFF, true);
                    curY += 11;

                    if (type == numenology.nodes.NodeType.PURE) {
                        context.drawText(client.textRenderer,
                                Text.translatable("chat.numenology.numenometer.nodepure").formatted(net.minecraft.util.Formatting.GREEN),
                                curX, curY, 0xFFFFFFFF, true);
                    } else {
                        boolean hasCustomAspects = false;
                        for (java.util.Map.Entry<numenology.energy.NumenAspect, Integer> entry : nodeAspects.entrySet()) {
                            numenology.energy.NumenAspect aspect = entry.getKey();
                            int amount = entry.getValue();

                            if (amount <= 0 || aspect == numenology.energy.ModAspects.NUMEN) continue;

                            hasCustomAspects = true;
                            int aspectColor = aspect.getColor() | 0xFF000000;

                            context.drawText(client.textRenderer,
                                    Text.literal("  " + aspect.getId().toUpperCase() + ": " + amount),
                                    curX, curY, aspectColor, true);
                            curY += 10;
                        }

                        if (!hasCustomAspects) {
                            // Получаем строку из перевода, которая может содержать \n
                            String translatedText = Text.translatable("chat.numenology.numenometer.nodeempty").getString();

                            // Используем наш новый метод для отрисовки
                            drawMultilineText(context, translatedText, curX, curY, 0xFF0000, true);

                            // Важно: если строк много, нужно обновить curY, чтобы не перекрыть следующий контент
                            int lineCount = translatedText.split("\n").length;
                            curY += (lineCount * 12);
                        }
                    }
                }
                // ВЫВОД ДАННЫХ ЧАНКА С ПОСТРОЧНЫМ ПЕРЕНОСОМ
                else if (data != null) {
                    context.drawText(client.textRenderer,
                            Text.translatable("hud.numenology.chunk.title").formatted(net.minecraft.util.Formatting.GOLD),
                            curX, curY, 0xFFFFFFFF, true);
                    curY += 12;

                    context.drawText(client.textRenderer,
                            Text.translatable("hud.numenology.chunk.energy").append(": " + data.getEnergy() + " / " + data.getMaxEnergy()).formatted(net.minecraft.util.Formatting.AQUA),
                            curX, curY, 0xFFFFFFFF, true);
                    curY += 10;

                    context.drawText(client.textRenderer,
                            Text.translatable("hud.numenology.chunk.miasma")
                                    .append(": " + data.getMiasma())
                                    .styled(style -> style.withColor(0x7FAB90)),
                            curX, curY, 0xFFFFFFFF, true);
                    curY += 10;

                    context.drawText(client.textRenderer,
                            Text.translatable("hud.numenology.chunk.stability").append(": " + data.getStability()).formatted(net.minecraft.util.Formatting.GREEN),
                            curX, curY, 0xFFFFFFFF, true);
                }

                context.getMatrices().pop();
            }
        }
    }

    private static void drawFullFlask(DrawContext context, int x, int y, int bgV, int fluidU, int current, int max, int xOffset, int yOffset) {
        context.drawTexture(HUD_TEXTURE, x, y, 0, bgV, 65, 55, 256, 256);

        if (max <= 0) return;

        float percent = Math.max(0, Math.min(1, (float) current / max));
        int fillAreaHeight = 36;
        int currentFillHeight = (int) (fillAreaHeight * percent);

        if (currentFillHeight > 0) {
            int fillX = x + xOffset;
            int areaTopY = y + yOffset;

            int diff = fillAreaHeight - currentFillHeight;
            int screenY = areaTopY + diff;
            int uvV = diff;

            context.drawTexture(HUD_TEXTURE,
                    fillX, screenY,
                    fluidU, uvV,
                    36, currentFillHeight,
                    256, 256);
        }
    }

    private static void drawAspectFlask(DrawContext context, int x, int y, int fluidU, int fluidV, int current, int max) {
        if (max <= 0) return;

        int maxHeight = 18;
        float percent = Math.max(0, Math.min(1, (float) current / max));
        int currentFillHeight = (int) (maxHeight * percent);

        if (currentFillHeight > 0) {
            int diff = maxHeight - currentFillHeight;
            int screenY = y + diff;
            int uvV = fluidV + diff;

            context.drawTexture(
                    HUD_TEXTURE,
                    x, screenY,
                    fluidU, uvV,
                    4, currentFillHeight,
                    256, 256
            );
        }
    }
}