package numenology.block;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import numenology.energy.NumenChunkData;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NumenWorldState extends PersistentState {

    // Карта строго использует Long в качестве ключа
    private final Map<Long, NumenChunkData> chunkData = new HashMap<>();

    public static NumenWorldState get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();
        return manager.getOrCreate(
                NumenWorldState::fromNbt,
                NumenWorldState::new,
                "numenology_state"
        );
    }

    public NumenChunkData getOrCreateChunkData(int chunkX, int chunkZ) {
        return getChunk(new ChunkPos(chunkX, chunkZ));
    }

    // Исправленный метод: теперь он проверяет наличие long-ключа!
    public NumenChunkData getChunk(ChunkPos pos) {
        long key = pos.toLong();

        if (!chunkData.containsKey(key)) {
            NumenChunkData data = new NumenChunkData();

            long seed = pos.toLong();
            Random localRandom = new Random(seed);

            int generatedEnergy = 300 + localRandom.nextInt(400);
            int generatedMiasma = 10 + localRandom.nextInt(40);

            data.setEnergy(generatedEnergy);
            data.setMiasma(generatedMiasma);
            data.setMaxEnergy(1000);

            chunkData.put(key, data);
            markDirty(); // Помечаем для сохранения, так как добавился новый чанк
        }
        return chunkData.get(key);
    }

    public Map<Long, NumenChunkData> getAllChunks() {
        return this.chunkData;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        int i = 0;
        for (Map.Entry<Long, NumenChunkData> entry : chunkData.entrySet()) {
            NbtCompound chunk = new NbtCompound();
            ChunkPos pos = new ChunkPos(entry.getKey());

            chunk.putInt("x", pos.x);
            chunk.putInt("z", pos.z);
            chunk.putInt("energy", entry.getValue().getEnergy());
            chunk.putInt("miasma", entry.getValue().getMiasma());
            chunk.putInt("stability", entry.getValue().getStability());
            chunk.putInt("maxEnergy", entry.getValue().getMaxEnergy());

            nbt.put("chunk_" + i, chunk);
            i++;
        }
        nbt.putInt("count", i);
        return nbt;
    }

    public static NumenWorldState fromNbt(NbtCompound nbt) {
        NumenWorldState state = new NumenWorldState();
        int count = nbt.getInt("count");

        for (int i = 0; i < count; i++) {
            NbtCompound chunk = nbt.getCompound("chunk_" + i);
            ChunkPos pos = new ChunkPos(chunk.getInt("x"), chunk.getInt("z"));

            NumenChunkData data = new NumenChunkData();
            data.setEnergy(chunk.getInt("energy"));
            data.setMiasma(chunk.getInt("miasma"));
            data.setMaxEnergy(chunk.getInt("maxEnergy"));

            state.chunkData.put(pos.toLong(), data);
        }
        return state;
    }
}