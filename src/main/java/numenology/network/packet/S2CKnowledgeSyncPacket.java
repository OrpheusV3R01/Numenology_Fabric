package numenology.network.packet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import numenology.network.ModPackets;
import numenology.research.KnowledgeManager;
import numenology.research.PlayerKnowledge;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class S2CKnowledgeSyncPacket {

    public static void send(ServerPlayerEntity player) {

        PlayerKnowledge data = KnowledgeManager.get(player);

        PacketByteBuf buf = PacketByteBufs.create();

        // COMPLETED
        Set<String> completed = data.getCompleted();
        buf.writeInt(completed.size());
        for (String id : completed) {
            buf.writeString(id);
        }

        // SUSPECTED
        Map<String, Long> suspected = data.getAllSuspected();
        buf.writeInt(suspected.size());
        for (var entry : suspected.entrySet()) {
            buf.writeString(entry.getKey());
            buf.writeLong(entry.getValue());
        }

        ServerPlayNetworking.send(player, ModPackets.SYNC_KNOWLEDGE, buf);
    }

    public static void receive(
            net.minecraft.client.MinecraftClient client,
            net.minecraft.client.network.ClientPlayNetworkHandler handler,
            PacketByteBuf buf,
            net.fabricmc.fabric.api.networking.v1.PacketSender responseSender
    ) {

        int completedSize = buf.readInt();
        Set<String> completed = new java.util.HashSet<>();

        for (int i = 0; i < completedSize; i++) {
            completed.add(buf.readString());
        }

        int suspectedSize = buf.readInt();
        Map<String, Long> suspected = new java.util.HashMap<>();

        for (int i = 0; i < suspectedSize; i++) {
            String id = buf.readString();
            long time = buf.readLong();
            suspected.put(id, time);
        }

        client.execute(() -> {
            if (client.player == null) return;

            PlayerKnowledge data =
                    KnowledgeManager.getUnsafeClient(client.player.getUuid());

            data.getCompleted().clear();
            data.getCompleted().addAll(completed);

            data.getAllSuspected().clear();
            data.getAllSuspected().putAll(suspected);
        });
    }
}