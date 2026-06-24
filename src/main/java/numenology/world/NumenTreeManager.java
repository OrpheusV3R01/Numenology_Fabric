package numenology.world;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import numenology.block.ModBlocks;
import numenology.block.NumenWorldState;

public class NumenTreeManager {

    private static int tickCounter = 0;

    public static void register() {

        ServerTickEvents.END_WORLD_TICK.register(world -> {

            if (world.isClient()) return;

            tickCounter++;

            if (tickCounter < 100) return;
            tickCounter = 0;

            processWorld((ServerWorld) world);
        });
    }

    private static void processWorld(ServerWorld world) {

        for (ServerPlayerEntity player : world.getPlayers()) {

            BlockPos center = player.getBlockPos();

            int radius = 16;

            for (BlockPos pos : BlockPos.iterate(
                    center.add(-radius, -6, -radius),
                    center.add(radius, 6, radius))) {

                if (world.getBlockState(pos).getBlock() == ModBlocks.NUMEN_LOG) {

                    applyEffect(world, player, pos);
                }
            }
        }
    }

    private static void applyEffect(ServerWorld world, ServerPlayerEntity player, BlockPos treePos) {

        int chunkX = treePos.getX() >> 4;
        int chunkZ = treePos.getZ() >> 4;

        var worldState = NumenWorldState.get(world);
        var chunkData = worldState.getOrCreateChunkData(chunkX, chunkZ);

        int currentMax = chunkData.getMaxEnergy();

        if (currentMax < 1500) {

            int newValue = currentMax + 1;

            chunkData.setMaxEnergy(newValue);

            // 💥 ОБЯЗАТЕЛЬНО ДЛЯ СОХРАНЕНИЯ
            worldState.markDirty();

        }
    }
}