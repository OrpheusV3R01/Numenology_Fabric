package numenology.command;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

import net.minecraft.server.network.ServerPlayerEntity;

import net.minecraft.text.Text;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;

import net.minecraft.server.world.ServerWorld;

import numenology.energy.NumenChunkData;
import numenology.energy.NumenEnergyManager;

import numenology.block.ModBlocks;

public class NumenCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

        dispatcher.register(CommandManager.literal("numen")

                // 📊 ТВОЯ СТАРАЯ КОМАНДА (оставляем как есть)
                .executes(context -> {

                    ServerCommandSource source = context.getSource();

                    ServerWorld world = source.getWorld();

                    BlockPos pos = BlockPos.ofFloored(source.getPosition());

                    ChunkPos chunk = new ChunkPos(pos);

                    NumenChunkData data =
                            NumenEnergyManager.getChunk(world, chunk);

                    source.sendFeedback(() -> Text.literal(
                            "Numen Field\n" +
                                    "Energy: " + data.getEnergy() + "\n" +
                                    "Miasma: " + data.getMiasma() + "\n" +
                                    "Stability: " + data.getStability()
                    ), false);

                    return 1;
                })

                // 🔥 НОВАЯ ПОДКОМАНДА
                .then(CommandManager.literal("node")
                        .executes(context -> {

                            ServerPlayerEntity player = context.getSource().getPlayer();

                            ServerWorld world = player.getServerWorld();

                            // позиция перед игроком
                            Direction dir = player.getHorizontalFacing();
                            BlockPos pos = player.getBlockPos().offset(dir);

                            world.setBlockState(
                                    pos,
                                    ModBlocks.NUMEN_NODE.getDefaultState()
                            );

                            player.sendMessage(Text.literal("Numen Node spawned"), false);

                            return 1;
                        })
                )

        );
    }
}