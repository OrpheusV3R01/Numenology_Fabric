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

public class NumenTreeFeature extends Feature<DefaultFeatureConfig> {

    public NumenTreeFeature(Codec<DefaultFeatureConfig> codec) {
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

        int height = 11 + random.nextInt(5); // 11–15 блоков

        BlockPos trunkTop = generateTiltedTrunk(world, pos, height, random);

        generateBranches(world, trunkTop, random);

        generateRoots(world, pos, random);

        return true;
    }

    private BlockPos generateTiltedTrunk(WorldAccess world, BlockPos base, int height, Random random) {
        BlockPos current = base;
        Direction tilt = Direction.Type.HORIZONTAL.random(random);

        for (int y = 0; y < height; y++) {
            placeLog(world, current, random);

            if (y > 4 && y < height - 4 && random.nextFloat() < 0.4f) {
                current = current.offset(tilt);
            }

            current = current.up();
        }
        return current.down();
    }

    private void generateBranches(WorldAccess world, BlockPos top, Random random) {
        int count = 5 + random.nextInt(3);

        for (int i = 0; i < count; i++) {
            Direction dir = Direction.Type.HORIZONTAL.random(random);
            int length = 4 + random.nextInt(3);

            // 🛠 ИЗМЕНЕНИЕ 1: Поднимаем ветки выше.
            // Было: top.down(3 + random.nextInt(5)) -> уходило вниз на 3-7 блоков от макушки.
            // Стало: уходим вниз всего на 1-3 блока (1 + random.nextInt(3)). Ветки теперь растут у самого верха.
            BlockPos current = top.down(1 + random.nextInt(3)).offset(dir, 1);

            for (int j = 0; j < length; j++) {
                placeLog(world, current, random);
                current = current.offset(dir).up(random.nextFloat() < 0.5f ? 1 : 0);
            }

            generateHemisphericalCrown(world, current, random);
        }
    }

    // 🛠 ИЗМЕНЕНИЕ 2: Переименовали в HemisphericalCrown и изменили логику y
    private void generateHemisphericalCrown(WorldAccess world, BlockPos center, Random random) {
        int radius = 4 + random.nextInt(2);

        for (int x = -radius; x <= radius; x++) {
            // у = 0 — это уровень конца ветки. Начинаем цикл от 0, чтобы листва не росла вниз.
            for (int y = 0; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= radius * radius + 3) {
                        BlockPos leafPos = center.add(x, y, z);

                        BlockState currentState = world.getBlockState(leafPos);
                        if (currentState.isAir() || currentState.isIn(BlockTags.LEAVES) || currentState.isReplaceable()) {
                            world.setBlockState(leafPos,
                                    ModBlocks.NUMEN_LEAVES.getDefaultState()
                                            .with(LeavesBlock.PERSISTENT, true), 3);
                        }
                    }
                }
            }
        }
    }

    private void generateRoots(WorldAccess world, BlockPos base, Random random) {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            if (random.nextFloat() < 0.7f) {
                BlockPos root = base.offset(dir);
                placeLog(world, root, random);
                if (random.nextBoolean()) placeLog(world, root.down(), random);
            }
        }
    }

    private void placeLog(WorldAccess world, BlockPos pos, Random random) {
        BlockState state = random.nextFloat() < 0.4f
                ? ModBlocks.NUMEN_RESIN_LOG.getDefaultState()
                : ModBlocks.NUMEN_LOG.getDefaultState();
        world.setBlockState(pos, state, 3);
    }
}