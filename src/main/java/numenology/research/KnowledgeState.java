package numenology.research;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class KnowledgeState extends PersistentState {

    private final Map<UUID, NbtCompound> data = new HashMap<>();

    public static KnowledgeState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                KnowledgeState::fromNbt,
                KnowledgeState::new,
                "numenology_knowledge"
        );
    }

    public NbtCompound getPlayerData(UUID uuid) {
        return data.computeIfAbsent(uuid, u -> new NbtCompound());
    }

    public void setPlayerData(UUID uuid, NbtCompound nbt) {
        data.put(uuid, nbt);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        for (var entry : data.entrySet()) {
            nbt.put(entry.getKey().toString(), entry.getValue());
        }
        return nbt;
    }

    public static KnowledgeState fromNbt(NbtCompound nbt) {
        KnowledgeState state = new KnowledgeState();

        for (String key : nbt.getKeys()) {
            state.data.put(UUID.fromString(key), nbt.getCompound(key));
        }

        return state;
    }
}