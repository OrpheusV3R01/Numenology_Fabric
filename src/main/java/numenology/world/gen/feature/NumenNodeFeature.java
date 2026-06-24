package numenology.world.gen.feature;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import numenology.block.ModBlocks;
import numenology.nodes.NumenNodeBlockEntity;
import numenology.nodes.NodeType;        // Импортируем NodeType
import numenology.energy.NumenAspect;
import numenology.energy.ModAspects;    // Импортируем ModAspects

import java.util.HashMap;
import java.util.Map;

public class NumenNodeFeature extends Feature<DefaultFeatureConfig> {

    public NumenNodeFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        WorldAccess world = context.getWorld();
        BlockPos origin = context.getOrigin();

// 📍 ищем поверхность
        BlockPos top = world.getTopPosition(
                Heightmap.Type.WORLD_SURFACE_WG,
                origin
        );

// Спускаемся вниз, если наткнулись на листву или логи только что сгенерированных деревьев
        while ((world.getBlockState(top).isIn(BlockTags.LEAVES) || world.getBlockState(top).isIn(BlockTags.LOGS) || world.getBlockState(top).isAir()) && top.getY() > world.getBottomY()) {
            top = top.down();
        }
        top = top.up(); // встаем НА найденную твердую землю

// ❌ если финальная точка не воздух (например, забита структурой) — отменяем
        if (!world.isAir(top)) {
            return false;
        }

        // 💥 ставим узел
        BlockState state = ModBlocks.NUMEN_NODE.getDefaultState();
        world.setBlockState(top, state, 3);

        // 💥 ИНИЦИАЛИЗАЦИЯ УЗЛА
        var blockEntity = world.getBlockEntity(top);

        if (blockEntity instanceof NumenNodeBlockEntity node) {
            var random = context.getRandom();

            // 1. Случайно выбираем тип узла для генерации в мире
            NodeType generatedType = NodeType.NORMAL;
            float typeRoll = random.nextFloat();

            if (typeRoll < 0.65f) {
                generatedType = NodeType.NORMAL;       // 65% Обычный
            } else if (typeRoll < 0.77f) {
                generatedType = NodeType.PURE;         // 12% Чистый
            } else if (typeRoll < 0.85f) {
                generatedType = NodeType.FADING;       // 8% Увядающий
            } else if (typeRoll < 0.92f) {
                generatedType = NodeType.INFECTED;     // 7% Заражённый
            } else {
                generatedType = NodeType.HUNGRY;       // 8% Голодный
            }

            // 2. Рассчитываем максимальную ёмкость узла
            int capacity = 60 + random.nextInt(120);

            // 3. Формируем карту случайных дополнительных аспектов
            Map<NumenAspect, Integer> generatedAspects = new HashMap<>();

            // Чистая энергия Нумен есть всегда (кроме Чистого узла, где будет только она)
            generatedAspects.put(ModAspects.NUMEN, capacity);

            if (generatedType != NodeType.PURE) {
                // Добавляем Lumen с шансом 40%
                if (random.nextFloat() < 0.40f) {
                    generatedAspects.put(ModAspects.LUMEN, 20 + random.nextInt(50));
                }
                // Добавляем Umbra с шансом 40%
                if (random.nextFloat() < 0.40f) {
                    generatedAspects.put(ModAspects.UMBRA, 20 + random.nextInt(50));
                }
                // Редкие аспекты: Potentia или Vita с шансом 15%
                if (random.nextFloat() < 0.15f) {
                    generatedAspects.put(ModAspects.POTENTIA, 10 + random.nextInt(30));
                }
                if (random.nextFloat() < 0.15f) {
                    generatedAspects.put(ModAspects.VITA, 15 + random.nextInt(40));
                }
                // Если узел заражён миазмами, обязательно добавляем Infungum
                if (generatedType == NodeType.INFECTED) {
                    generatedAspects.put(ModAspects.INFUNGUM, 30 + random.nextInt(60));
                }
            }

            // Инициализируем узел новыми вариативными данными!
            node.initialize(generatedType, generatedAspects, capacity);
            return true;
        }

        return false;
    }
}