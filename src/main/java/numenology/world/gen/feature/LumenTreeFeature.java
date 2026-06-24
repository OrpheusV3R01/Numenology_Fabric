package numenology.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import numenology.block.ModBlocks;

public class LumenTreeFeature extends Feature<DefaultFeatureConfig> {

    public LumenTreeFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        WorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();
        Random random = context.getRandom();

        // 🔍 Адаптивный поиск настоящей поверхности земли для сервера
        net.minecraft.util.math.BlockPos.Mutable mutablePos = pos.mutableCopy();
        while (world.isAir(mutablePos) && mutablePos.getY() > world.getBottomY()) {
            mutablePos.move(net.minecraft.util.math.Direction.DOWN);
        }
        while (!world.isAir(mutablePos) && mutablePos.getY() < world.getTopY()) {
            mutablePos.move(net.minecraft.util.math.Direction.UP);
        }
        pos = mutablePos.down(); // Встаем на саму землю

        if (!world.getBlockState(pos.down()).isIn(BlockTags.DIRT)) {
            return false;
        }

        int height = 12 + random.nextInt(4); // Слегка увеличили высоту (12–15 блоков) для лучшего баланса

        // 🌳 Массив для запоминания точных координат блоков ствола,
        // чтобы ветки росли гарантированно ИЗ ствола, а не в воздухе рядом.
        BlockPos[] trunkPositions = new BlockPos[height];

        // Строим тонкий ствол
        for (int i = 0; i < height; i++) {
            BlockPos currentTrunkPos = pos.up(i);
            trunkPositions[i] = currentTrunkPos;
            placeLog(world, currentTrunkPos, random);
        }

        // 🛠 ИЗМЕНЕНИЕ 1: Передаем массив координат ствола, чтобы ветки крепились к нему идеально
        generateLumenBranches(world, trunkPositions, random);

        // Крона на самой верхушке
        BlockPos trunkTop = trunkPositions[height - 1];
        generateHollowSphere(world, trunkTop, random);
        if (random.nextFloat() < 0.5f) {
            generateHollowSphere(world, trunkTop.up(), random);
        }

        return true;
    }

    private void placeLog(WorldAccess world, BlockPos pos, Random random) {
        BlockState state = random.nextFloat() < 0.25f
                ? ModBlocks.LUMEN_RESIN_LOG.getDefaultState()
                : ModBlocks.LUMEN_LOG.getDefaultState();

        world.setBlockState(pos, state, 3);
    }

    // 🛠 ИЗМЕНЕНИЕ 2: Новый алгоритм развесистых ветвей
    private void generateLumenBranches(WorldAccess world, BlockPos[] trunkPositions, Random random) {
        int height = trunkPositions.length;

        // Ветки начинают расти с середины дерева (высота / 2), чтобы нижняя часть ствола была аккуратной,
        // но дерево при этом не выглядело «лысым» снизу.
        int startY = height / 2;
        int branchCount = 4 + random.nextInt(3); // Увеличили количество веток до 4-6 штук

        for (int b = 0; b < branchCount; b++) {
            Direction dir = Direction.Type.HORIZONTAL.random(random);

            // Выбираем случайный индекс блока на стволе в верхнем диапазоне
            int trunkIndex = startY + random.nextInt(height - startY - 2);

            // Берем точную точку прямо со ствола и делаем первый шаг в сторону
            BlockPos current = trunkPositions[trunkIndex].offset(dir);

            // Делаем ветки чуть длиннее (4-5 блоков), чтобы они выходили за пределы купола листвы
            int length = 4 + random.nextInt(2);

            for (int j = 0; j < length; j++) {
                placeLog(world, current, random);

                // Имитация красивого изгиба ветки: идет вбок, но иногда приподнимается (направление к солнцу)
                if (random.nextFloat() < 0.3f) {
                    current = current.up();
                } else {
                    current = current.offset(dir);
                }
            }

            // Генерируем сферы только на концах длинных веток
            generateHollowSphere(world, current, random);
            if (random.nextFloat() < 0.4f) {
                generateHollowSphere(world, current.up(), random);
            }
        }
    }

    private void generateHollowSphere(WorldAccess world, BlockPos center, Random random) {
        int radius = 2 + random.nextInt(2);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double dist = x * x + y * y + z * z;

                    if (dist <= radius * radius && dist >= (radius - 1) * (radius - 1)) {
                        BlockPos leafPos = center.add(x, y, z);

                        BlockState currentState = world.getBlockState(leafPos);
                        if (currentState.isAir() || currentState.isIn(BlockTags.LEAVES) || currentState.isReplaceable()) {
                            world.setBlockState(
                                    leafPos,
                                    ModBlocks.LUMEN_LEAVES.getDefaultState()
                                            .with(LeavesBlock.PERSISTENT, true),
                                    3
                            );
                        }
                    }
                }
            }
        }
    }
}