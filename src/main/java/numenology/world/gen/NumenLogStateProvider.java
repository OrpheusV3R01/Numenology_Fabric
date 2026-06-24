package numenology.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import net.minecraft.world.gen.stateprovider.BlockStateProviderType;
import numenology.block.ModBlocks;
import numenology.block.custom.NumenLogBlock;

public class NumenLogStateProvider extends BlockStateProvider {

    public static final Codec<NumenLogStateProvider> CODEC = Codec.unit(() -> new NumenLogStateProvider());

    @Override
    protected BlockStateProviderType<?> getType() {
        return ModStateProviders.NUMEN_LOG_PROVIDER;
    }

    @Override
    public BlockState get(Random random, BlockPos pos) {
        return ModBlocks.NUMEN_LOG.getDefaultState()
                .with(NumenLogBlock.RESIN, random.nextFloat() < 0.2f);
    }
}