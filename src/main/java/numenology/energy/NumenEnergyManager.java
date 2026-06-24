package numenology.energy;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import numenology.block.NumenWorldState;

public class NumenEnergyManager {

    public static NumenChunkData getChunk(ServerWorld world, ChunkPos pos) {

        NumenWorldState state = NumenWorldState.get(world);

        return state.getChunk(pos);
    }

}