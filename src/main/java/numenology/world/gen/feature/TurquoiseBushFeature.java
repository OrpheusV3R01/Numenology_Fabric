package numenology.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import numenology.block.ModBlocks;

public class TurquoiseBushFeature extends Feature<DefaultFeatureConfig> {

    public TurquoiseBushFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

        net.minecraft.util.math.random.Random random = context.getRandom();
        // 🎲 Шанс 30% на запуск генерации куста в этой точке
        if (random.nextFloat() > 0.3F) {
            return false;
        }

        // 1. Проверяем, подходит ли блок под кустом для его роста
        BlockPos groundPos = origin.down();
        BlockState groundState = world.getBlockState(groundPos);
        if (!groundState.isOf(Blocks.GRASS_BLOCK) && !groundState.isOf(Blocks.DIRT)) {
            return false;
        }

        // 2. Проверяем, что на точке спавна сейчас воздух
        if (!world.getBlockState(origin).isAir()) {
            return false;
        }

        // 3. СКАНИРОВАНИЕ: Ищем Нумен Дерево в радиусе (X: 6, Y: 3, Z: 6)
        boolean foundNumenTree = false;
        int radiusX = 6;
        int radiusY = 3;
        int radiusZ = 6;

        // Ставим метку "treeSearch" для внешнего цикла
        treeSearch:
        for (int x = -radiusX; x <= radiusX; x++) {
            for (int y = -radiusY; y <= radiusY; y++) {
                for (int z = -radiusZ; z <= radiusZ; z++) {
                    BlockPos checkPos = origin.add(x, y, z);
                    BlockState checkState = world.getBlockState(checkPos);

                    // Если нашли бревно или листву Нумен Дерева
                    if (checkState.isOf(ModBlocks.NUMEN_LOG) || checkState.isOf(ModBlocks.NUMEN_LEAVES)) {
                        foundNumenTree = true;
                        break treeSearch; // Брейкаем СРАЗУ ВСЕ ТРИ ЦИКЛА по метке
                    }
                }
            }
        }

        // Если рядом нет ни одного блока Нумен Дерева, куст здесь расти не будет
        if (!foundNumenTree) {
            return false;
        }

        // 4. Спавним куст
        world.setBlockState(origin, ModBlocks.TURQUOISE_BUSH.getDefaultState(), 2);

        return true;
    }
}