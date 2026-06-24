package numenology.research;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import numenology.network.packet.S2CKnowledgeSyncPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KnowledgeManager {

    private static final Map<UUID, PlayerKnowledge> CLIENT_CACHE = new HashMap<>();
    private static final Map<UUID, PlayerKnowledge> SERVER_CACHE = new HashMap<>();

    public static PlayerKnowledge get(ServerPlayerEntity player) {
        return SERVER_CACHE.computeIfAbsent(
                player.getUuid(),
                uuid -> new PlayerKnowledge()
        );
    }

    public static PlayerKnowledge getUnsafeClient(UUID uuid) {
        return CLIENT_CACHE.computeIfAbsent(uuid, u -> new PlayerKnowledge());
    }

    public static void trigger(ServerPlayerEntity player, String knowledgeId, long currentTime, long duration) {
        PlayerKnowledge data = get(player);

        if (data.hasCompleted(knowledgeId)) return;
        if (data.hasSuspected(knowledgeId)) return;
        if (data.isOnCooldown(knowledgeId, currentTime)) return;
        if (data.getAllSuspected().size() >= 3) return;

        data.addSuspected(knowledgeId, currentTime + duration);

        if (player.networkHandler != null) {
            syncToClient(player);
        }

        player.sendMessage(net.minecraft.text.Text.literal("Ты замечаешь странные свойства..."), true);
    }

    public static void complete(ServerPlayerEntity player, String id) {
        var data = get(player);
        if (!data.hasSuspected(id)) return;

        if (!data.canComplete(id)) {
            player.sendMessage(net.minecraft.text.Text.literal("Хмм... Чего-то не хватает"), true);
            return;
        }

        data.removeSuspected(id);
        data.addCompleted(id);
        syncToClient(player);

        player.sendMessage(net.minecraft.text.Text.literal("Знание закреплено."), true);
        handleReward(player, id);
    }

    public static void syncToClient(ServerPlayerEntity player) {
        S2CKnowledgeSyncPacket.send(player);
    }

    // 🔥 ИСПРАВЛЕНО: Сохранение данных игрока в PersistentState мира[cite: 16, 21, 22]
    public static void save(ServerPlayerEntity player) {
        PlayerKnowledge data = get(player);
        var state = KnowledgeState.get(player.getServer().getOverworld());
        state.setPlayerData(player.getUuid(), data.toNbt());
    }

    // 🔥 ИСПРАВЛЕНО: Загрузка данных из PersistentState в SERVER_CACHE[cite: 16, 21, 22]
    public static void load(ServerPlayerEntity player) {
        var state = KnowledgeState.get(player.getServer().getOverworld());
        var nbt = state.getPlayerData(player.getUuid());

        // Загружаем существующие данные из NBT[cite: 16, 18, 21]
        PlayerKnowledge data = PlayerKnowledge.fromNbt(nbt);
        SERVER_CACHE.put(player.getUuid(), data);
    }

    private static void handleReward(ServerPlayerEntity player, String id) {
        switch (id) {
            case "numenology:hematite_processing" -> player.sendMessage(net.minecraft.text.Text.literal("Теперь я понимаю, как работать с гематитом..."), false);
            case "numenology:crucible_basics" -> player.sendMessage(net.minecraft.text.Text.literal("Теперь я могу создать тигель..."), false);
            case "numenology:numen_transmutation_tier1" -> player.sendMessage(net.minecraft.text.Text.literal("Я начинаю понимать трансмутацию..."), false);
        }
    }

    public static void tick(ServerPlayerEntity player, long currentTime) {
        PlayerKnowledge data = get(player);
        for (var entry : data.getAllSuspected().entrySet().toArray(new Map.Entry[0])) {
            String id = (String) entry.getKey();
            long expireTime = (long) entry.getValue();

            if (currentTime >= expireTime) {
                data.removeSuspected(id);
                data.setCooldown(id, currentTime + 24000);
                player.sendMessage(net.minecraft.text.Text.literal("Кажется, я забыл что-то важное..."), true);
                syncToClient(player); // Синхронизируем удаление подозрения[cite: 16]
            }
        }
    }
}