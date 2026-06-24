package numenology.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import numenology.block.ModBlocks;
import numenology.block.custom.NumenClusterBlock;

public class NumenClusterFeature extends Feature<DefaultFeatureConfig> {

    public NumenClusterFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();

        // Делаем небольшое скопление (кластер) из нескольких кристаллов рядом
        int count = 4 + random.nextInt(5); // от 4 до 8 попыток спавна в одной точке
        boolean generatedAny = false;

        for (int i = 0; i < count; i++) {
            // Немного смещаем каждый кристалл в кубе 4х4х4 для естественности
            BlockPos spawnPos = origin.add(
                    random.nextInt(5) - 2,
                    random.nextInt(5) - 2,
                    random.nextInt(5) - 2
            );

            // Проверяем, что мы ставим блок в пещерный воздух
            BlockState currentState = world.getBlockState(spawnPos);
            if (!currentState.isAir() && !currentState.isOf(Blocks.CAVE_AIR)) {
                continue;
            }

            // Перебираем случайные направления, чтобы цепляться за пол, потолок или стены
            Direction[] directions = Direction.values();
            // Перемешаем направления для случайности
            for (int j = 0; j < directions.length; j++) {
                int randomIndex = random.nextInt(directions.length);
                Direction temp = directions[j];
                directions[j] = directions[randomIndex];
                directions[randomIndex] = temp;
            }

            for (Direction direction : directions) {
                // Ищем опорный блок в противоположном направлении
                BlockPos neighborPos = spawnPos.offset(direction.getOpposite());
                BlockState neighborState = world.getBlockState(neighborPos);

                // Если опорный блок — это камень или глубинная руда, то место идеальное
                if (neighborState.isOf(Blocks.STONE) || neighborState.isOf(Blocks.DEEPSLATE) ||
                        neighborState.isOf(Blocks.ANDESITE) || neighborState.isOf(Blocks.DIORITE) ||
                        neighborState.isOf(Blocks.GRANITE)) {

                    // Формируем состояние блока с ПРАВИЛЬНЫМ направлением facing
                    BlockState clusterState = ModBlocks.NUMEN_CLUSTER.getDefaultState()
                            .with(NumenClusterBlock.FACING, direction);

                    // Устанавливаем блок в мир
                    world.setBlockState(spawnPos, clusterState, 2);
                    generatedAny = true;
                    break; // Направление нашли, переходим к следующему кристаллу в цикле
                }
            }
        }

        return generatedAny;
    }
}