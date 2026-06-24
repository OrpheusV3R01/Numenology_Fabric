package numenology.world.gen;

import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import numenology.Numenology;

public class UmbraTreeGrower extends SaplingGenerator {

    public static final RegistryKey<ConfiguredFeature<?, ?>> UMBRA_TREE =
            RegistryKey.of(
                    RegistryKeys.CONFIGURED_FEATURE,
                    new Identifier(Numenology.MOD_ID, "umbra_tree")
            );

    @Override
    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return UMBRA_TREE;
    }
}