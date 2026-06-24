package numenology.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import numenology.network.ModPackets;
import numenology.network.packet.S2CKnowledgeSyncPacket;
import numenology.research.KnowledgeManager;

public class C2SCompleteResearchPacket {

    public static void send(String id) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(id);

        ClientPlayNetworking.send(
                ModPackets.COMPLETE_RESEARCH,
                buf
        );
    }

    public static void receive(
            net.minecraft.server.MinecraftServer server,
            ServerPlayerEntity player,
            net.minecraft.server.network.ServerPlayNetworkHandler handler,
            PacketByteBuf buf,
            net.fabricmc.fabric.api.networking.v1.PacketSender responseSender
    ) {
        String id = buf.readString();

        server.execute(() -> {

            // =========================
            // 🔍 1. Проверка: открыт ли стол
            // =========================
            if (!(player.currentScreenHandler instanceof numenology.screen.ResearchTableScreenHandler screenHandler)) {
                return;
            }

            var inventory = screenHandler.getSlot(0).inventory;

            var codex = inventory.getStack(0);
            var paper = inventory.getStack(1);
            var tools = inventory.getStack(2);

            // =========================
            // 🔍 2. Проверка ресурсов
            // =========================
            if (codex.isEmpty() || paper.isEmpty() || tools.isEmpty()) {
                return;
            }

            // =========================
            // 🔍 3. Завершение исследования
            // =========================
            KnowledgeManager.complete(player, id);
            paper.decrement(1);

            // =========================
            // 🔄 4. Синхронизация
            // =========================
            S2CKnowledgeSyncPacket.send(player);
        });
    }
}