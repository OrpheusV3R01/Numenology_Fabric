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

public class UmbraTreeFeature extends Feature<DefaultFeatureConfig> {

    public UmbraTreeFeature(Codec<DefaultFeatureConfig> codec) {
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
        pos = mutablePos.down(); // Встаем на блок земли

        // Проверка: есть ли под нами земля
        if (!world.getBlockState(pos.down()).isIn(BlockTags.DIRT)) {
            return false;
        }

        // 🛠 ИЗМЕНЕНИЕ 1: Фиксированная базовая высота ~16 блоков (например, 15-18 для естественности)
        int height = 15 + random.nextInt(3);

        // Строим ствол толщиной 2x2 и получаем его финальную верхнюю точку
        BlockPos trunkTop = generateTwistedTrunk(world, pos, height, random);

        // 🛠 ИЗМЕНЕНИЕ 2: Несколько веток, расходящихся из верхней части ствола
        generateUmbraBranches(world, trunkTop, random);

        // 🛠 ИЗМЕНЕНИЕ 3: Покрывающая куполообразная крона, центрированная на вершине дерева
        generateDomeCrown(world, trunkTop.up(1), random);

        // Корни
        generateRoots(world, pos, random);

        return true;
    }

    private BlockPos generateTwistedTrunk(WorldAccess world, BlockPos base, int height, Random random) {
        BlockPos current = base;

        for (int y = 0; y < height; y++) {
            // Устанавливаем блоки 2x2
            for (int x = 0; x <= 1; x++) {
                for (int z = 0; z <= 1; z++) {
                    placeLog(world, current.add(x, 0, z), random);
                }
            }

            // Корректировка изгиба под высоту в 16 блоков, чтобы дерево не ломалось слишком сильно
            if (y > 4 && y < height - 4 && random.nextFloat() < 0.35f) {
                Direction twist = Direction.Type.HORIZONTAL.random(random);
                current = current.offset(twist);
            }

            current = current.up();
        }
        return current.down();
    }

    // Старые методы генерации веток (generateLargeBranches, generateLowerBranch) удалены.
    // Вместо них добавлен один компактный метод для создания нескольких верхних ветвей.
    private void generateUmbraBranches(WorldAccess world, BlockPos top, Random random) {
        int branchCount = 3 + random.nextInt(2); // Ровно "несколько" веток (3-4 штуки)

        for (int i = 0; i < branchCount; i++) {
            Direction dir = Direction.Type.HORIZONTAL.random(random);
            int length = 4 + random.nextInt(3); // Короткие ветви для поддержки купола

            // Начинаем ветку из структуры 2x2 на 1-2 блока ниже макушки
            BlockPos branchPos = top.down(random.nextInt(2)).offset(dir, 1);

            for (int j = 0; j < length; j++) {
                placeLog(world, branchPos, random);

                // Ветки идут вбок и слегка вверх
                branchPos = branchPos.offset(dir).up(random.nextFloat() < 0.4f ? 1 : 0);
            }
        }
    }

    // Новый метод генерации единой куполообразной (полусферической) кроны
    private void generateDomeCrown(WorldAccess world, BlockPos center, Random random) {
        int radius = 7 + random.nextInt(2); // Радиус покрытия купола

        for (int x = -radius; x <= radius; x++) {
            // y от 0 и выше создает плоский низ купола, уходящий вверх
            for (int y = 0; y <= radius - 2; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Формула уплощенного эллипсоида/купола (сжатие по вертикали y * 1.3)
                    if (x * x + (y * 1.3) * (y * 1.3) + z * z <= radius * radius) {
                        BlockPos leafPos = center.add(x, y, z);

                        BlockState currentState = world.getBlockState(leafPos);
                        if (currentState.isAir() || currentState.isIn(BlockTags.LEAVES) || currentState.isReplaceable()) {
                            world.setBlockState(leafPos,
                                    ModBlocks.UMBRA_LEAVES.getDefaultState()
                                            .with(LeavesBlock.PERSISTENT, true), 3);
                        }
                    }
                }
            }
        }
    }

    private void generateRoots(WorldAccess world, BlockPos base, Random random) {
        // Так как ствол 2x2, корни теперь распределяются по периметру толстого ствола
        for (int x = -1; x <= 2; x++) {
            for (int z = -1; z <= 2; z++) {
                // Исключаем сам ствол (координаты 0 и 1)
                if ((x == 0 || x == 1) && (z == 0 || z == 1)) continue;

                if (random.nextFloat() < 0.45f) {
                    BlockPos root = base.add(x, 0, z);
                    placeLog(world, root, random);
                    if (random.nextBoolean()) {
                        placeLog(world, root.down(), random);
                    }
                }
            }
        }
    }

    private void placeLog(WorldAccess world, BlockPos pos, Random random) {
        BlockState state = random.nextFloat() < 0.35f
                ? ModBlocks.UMBRA_RESIN_LOG.getDefaultState()
                : ModBlocks.UMBRA_LOG.getDefaultState();
        world.setBlockState(pos, state, 3);
    }
}