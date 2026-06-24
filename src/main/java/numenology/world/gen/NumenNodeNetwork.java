package numenology.world.gen;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.Random;

import numenology.block.ModBlocks;

public class NumenNodeNetwork {

    private static final int REGION_SIZE = 12; // размер региона в чанках
    private static final float NODE_CHANCE = 0.7f;

    public static void tryGenerateNode(ServerWorld world, ChunkPos chunkPos) {

        int regionX = chunkPos.x / REGION_SIZE;
        int regionZ = chunkPos.z / REGION_SIZE;

        Random random = Random.create(regionX * 341873128712L + regionZ * 132897987541L);

        if (random.nextFloat() > NODE_CHANCE) {
            return;
        }

        int chunkOffsetX = random.nextInt(REGION_SIZE);
        int chunkOffsetZ = random.nextInt(REGION_SIZE);

        if (chunkPos.x % REGION_SIZE != chunkOffsetX ||
                chunkPos.z % REGION_SIZE != chunkOffsetZ) {
            return;
        }

        int x = chunkPos.getStartX() + random.nextInt(16);
        int z = chunkPos.getStartZ() + random.nextInt(16);
        int y = 60 + random.nextInt(40);

        BlockPos pos = new BlockPos(x, y, z);

        if (world.isAir(pos)) {
            world.setBlockState(pos, ModBlocks.NUMEN_NODE.getDefaultState());
        }
    }
}