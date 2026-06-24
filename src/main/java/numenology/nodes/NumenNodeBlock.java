package numenology.nodes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import numenology.energy.ModAspects;
import numenology.energy.NumenAspect;
import java.util.HashMap;
import java.util.Map;

public class NumenNodeBlock extends Block implements net.minecraft.block.BlockEntityProvider {

    public NumenNodeBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NumenNodeBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            World world,
            BlockState state,
            BlockEntityType<T> type
    ) {
        return world.isClient ? null : (w, pos, s, be) -> {
            if (be instanceof NumenNodeBlockEntity node) {
                NumenNodeBlockEntity.tick(w, pos, s, node);
            }
        };
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);

        if (!world.isClient && world.getBlockEntity(pos) instanceof NumenNodeBlockEntity node) {
            Map<NumenAspect, Integer> defaultAspects = new HashMap<>();
            NodeType type = NodeType.NORMAL;

            // Фиксированное значение для калибровки HUD шкал
            int calibrationAmount = 50;

            // Наполняем узел строго по 20 единиц каждого специализированного аспекта
            defaultAspects.put(ModAspects.LUMEN, calibrationAmount);
            defaultAspects.put(ModAspects.UMBRA, calibrationAmount);
            defaultAspects.put(ModAspects.POTENTIA, calibrationAmount);
            defaultAspects.put(ModAspects.VITA, calibrationAmount);
            defaultAspects.put(ModAspects.INFUNGUM, calibrationAmount);
            defaultAspects.put(ModAspects.MECHA, calibrationAmount);
            defaultAspects.put(ModAspects.ANTIQUUS, calibrationAmount);

            // Вычисляем точную сумму всех компонентов (7 аспектов * 20 = 140)
            int totalSum = defaultAspects.size() * calibrationAmount;

            // Записываем NUMEN как чистую сумму всех аспектов (он станет равен 140)
            defaultAspects.put(ModAspects.NUMEN, totalSum);

            // Инициализируем узел: теперь капа (capacity) гарантированно вместит все аспекты без остатка
            node.initialize(type, defaultAspects, totalSum);
        }
    }
}