package numenology.block.custom;

import net.minecraft.block.LeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.math.random.Random; // ✅ ПРАВИЛЬНЫЙ

public class UmbraLeavesBlock extends LeavesBlock {

    public UmbraLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {

        if (random.nextFloat() < 0.08f) {

            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() - 0.1;
            double z = pos.getZ() + random.nextDouble();

            world.addParticle(
                    ParticleTypes.SMOKE,
                    x, y, z,
                    0.0, -0.02, 0.0
            );
        }

        if (random.nextFloat() < 0.05f) {

            double x = pos.getX() + random.nextDouble();
            double y = pos.getY() - 0.2;
            double z = pos.getZ() + random.nextDouble();

            world.addParticle(
                    ParticleTypes.ASH,
                    x, y, z,
                    0.0, -0.01, 0.0
            );
        }
    }
}