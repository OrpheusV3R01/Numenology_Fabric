package numenology.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;

public class ModBiomeModifications {

    public static void register() {

        // 🔮 NUMEN NODE
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                RegistryKey.of(
                        RegistryKeys.PLACED_FEATURE,
                        new Identifier("numenology", "numen_node")
                )
        );

        // 🌳 NUMEN TREE
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(
                        RegistryKeys.PLACED_FEATURE,
                        new Identifier("numenology", "numen_tree")
                )
        );

        // 🌑 UMBRA TREE
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(
                        RegistryKeys.PLACED_FEATURE,
                        new Identifier("numenology", "umbra_tree")
                )
        );

        // 🌟 LUMEN TREE (ДОБАВЛЕНО)
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(
                        RegistryKeys.PLACED_FEATURE,
                        new Identifier("numenology", "lumen_tree")
                )
        );

        // 🔮 NUMEN CLUSTER (ДОБАВЛЕНО)
        BiomeModifications.addFeature(
                BiomeSelectors.foundInOverworld(),
                GenerationStep.Feature.UNDERGROUND_ORES, // <--- Сюда
                RegistryKey.of(
                        RegistryKeys.PLACED_FEATURE,
                        new Identifier("numenology", "numen_cluster_placed")
                )
        );

        // 🌿 TURQUOISE BUSH (ДОБАВЛЕНО)
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION,
                RegistryKey.of(
                        RegistryKeys.PLACED_FEATURE,
                        new Identifier("numenology", "turquoise_bush_placed")
                )
        );
    }
}