package numenology.research;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.Items;

import numenology.item.ModItems;
import numenology.block.ModBlocks;

public class ResearchEvents {

    public static void register() {

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            KnowledgeManager.load(handler.getPlayer());
            KnowledgeManager.syncToClient(handler.getPlayer());
        });

        // Сохраняем данные при выходе игрока
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            KnowledgeManager.save(handler.getPlayer());
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                if (player.age < 20) continue;

                boolean hasCodex = false;
                boolean hasSlate = false;
                boolean hasIron = false;
                boolean hasHematite = false;
                boolean hasNumenBricks = false;
                boolean hasNumenSteel = false;
                boolean hasModLog = false;

                // 🔍 проверка инвентаря
                for (ItemStack stack : player.getInventory().main) {

                    if (stack.isEmpty()) continue;

                    // Нумен Кодекс
                    if (stack.isOf(ModItems.NUMEN_CODEX)) {
                        hasCodex = true;
                    }

                    // Колотый глубинный сланец (ваниль)
                    if (stack.isOf(Items.COBBLED_DEEPSLATE)) {
                        hasSlate = true;
                    }

                    // Железо
                    if (stack.isOf(Items.IRON_INGOT) || stack.isOf(Items.IRON_NUGGET)) {
                        hasIron = true;
                    }

                    // ГЕМАТИТ (БЛОК → ITEM)
                    if (stack.isOf(ModBlocks.HEMATITE_BLOCK.asItem())) {
                        hasHematite = true;
                    }

                    if (stack.isOf(ModBlocks.NUMEN_BRICKS.asItem())) {
                        hasNumenBricks = true;
                    }

                    if (stack.isOf(ModItems.NUMEN_STEEL_INGOT)) {
                        hasNumenSteel = true;
                    }

                    if (stack.isOf(ModBlocks.NUMEN_LOG.asItem()) ||
                            stack.isOf(ModBlocks.NUMEN_RESIN_LOG.asItem()) ||
                            stack.isOf(ModBlocks.UMBRA_LOG.asItem()) ||
                            stack.isOf(ModBlocks.UMBRA_RESIN_LOG.asItem()) ||
                            stack.isOf(ModBlocks.LUMEN_LOG.asItem()) ||
                            stack.isOf(ModBlocks.LUMEN_RESIN_LOG.asItem())) {
                        hasModLog = true;
                    }

                }

                long time = player.getWorld().getTime();

                // =========================
                // 🔹 FIRST CONTACT
                //=========================
                if (hasCodex) {
                     KnowledgeManager.trigger(
                            player,
                            "numenology:first_contact_numen",
                            time,
                            72000
                    );
                }

                // =========================
                // 🔹 HEMATITE
                // =========================
                if (hasSlate && hasIron) {
                 KnowledgeManager.trigger(
                          player,
                         "numenology:hematite_processing",
                          time,
                          72000
                  );
                }

                // =========================
                // 🔹 CRUCIBLE BASICS
                // =========================
                if (hasHematite && hasCodex) {
                   KnowledgeManager.trigger(
                          player,
                          "numenology:crucible_basics",
                         time,
                          72000
                  );
                }

                // 3. ТРИГГЕР: ИНСТРУМЕНТЫ ИССЛЕДОВАНИЯ
                if (hasNumenSteel) {
                    KnowledgeManager.trigger(
                            player,
                            "numenology:research_tools",
                            time,
                            72000
                    );
                }

                // =========================
                // 🔹 HEATED FORGE
                // =========================
               if (hasNumenBricks &&
                        KnowledgeManager.get(player).hasCompleted("numenology:numen_transmutation_tier1")) {

                                  KnowledgeManager.trigger(
                          player,
                         "numenology:heated_forge",
                          time,
                          72000
                  );
                }

                // =========================
                // 🌿 MAGIC FLORA
                // =========================
                if (hasModLog) {
                    KnowledgeManager.trigger(
                            player,
                            "numenology:magic_flora",
                            time,
                            72000
                    );
                }

            }
        });
    }
}