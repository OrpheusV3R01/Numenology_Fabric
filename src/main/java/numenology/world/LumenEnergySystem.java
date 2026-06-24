package numenology.world;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import numenology.block.NumenWorldState;

public class LumenEnergySystem {

    private static int tickCounter = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            tickCounter++;

            // 🌟 каждые 5 секунд (100 тиков)
            if (tickCounter < 100) return;
            tickCounter = 0;

            for (ServerWorld world : server.getWorlds()) {
                NumenWorldState state = NumenWorldState.get(world);

                // Принимаем chunkKey (Long) вместо объекта ChunkPos
                state.getAllChunks().forEach((chunkKey, data) -> {
                    if (data.hasLumenTree()) {
                        data.addEnergy(3);
                    }
                });

                state.markDirty();
            }
        });
    }
}