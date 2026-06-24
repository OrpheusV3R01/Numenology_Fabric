package numenology.world.gen.feature;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class ModFeatures {

    public static Feature<DefaultFeatureConfig> UMBRA_TREE;

    public static Feature<DefaultFeatureConfig> NUMEN_TREE;

    public static Feature<DefaultFeatureConfig> LUMEN_TREE;

    public static final Feature<?> NUMEN_NODE = new NumenNodeFeature();

    public static Feature<DefaultFeatureConfig> NUMEN_CLUSTER_FEATURE;

    public static Feature<DefaultFeatureConfig> TURQUOISE_BUSH_FEATURE;

    public static void register() {

        Registry.register(
                Registries.FEATURE,
                new Identifier("numenology", "numen_node"),
                NUMEN_NODE
        );

        NUMEN_CLUSTER_FEATURE = Registry.register(
                Registries.FEATURE,
                new Identifier("numenology", "numen_cluster_feature"),
                new NumenClusterFeature(DefaultFeatureConfig.CODEC)
        );

        TURQUOISE_BUSH_FEATURE = Registry.register(
                Registries.FEATURE,
                new Identifier("numenology", "turquoise_bush_feature"),
                new TurquoiseBushFeature(DefaultFeatureConfig.CODEC)
        );

        NUMEN_TREE = Registry.register(
                Registries.FEATURE,
                new Identifier("numenology", "numen_tree"),
                new NumenTreeFeature(DefaultFeatureConfig.CODEC)
        );

        UMBRA_TREE = Registry.register(
                Registries.FEATURE,
                new Identifier("numenology", "umbra_tree"),
                new UmbraTreeFeature(DefaultFeatureConfig.CODEC)
        );

        // 🌟 LUMEN TREE (ДОБАВЛЕНО)
        LUMEN_TREE = Registry.register(
                Registries.FEATURE,
                new Identifier("numenology", "lumen_tree"),
                new LumenTreeFeature(DefaultFeatureConfig.CODEC)
        );
    }
}