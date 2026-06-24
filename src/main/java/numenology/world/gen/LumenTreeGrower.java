package numenology.world.gen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;

public class LumenTreeGrower extends SaplingGenerator {

    @Override
    protected RegistryKey<net.minecraft.world.gen.feature.ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return RegistryKey.of(
                RegistryKeys.CONFIGURED_FEATURE,
                new Identifier("numenology", "lumen_tree")
        );
    }
}