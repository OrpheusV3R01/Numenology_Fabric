package numenology.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.PlacedFeature;

public class ModWorldGen {

    // 🌳 NUMEN TREE (новый ID)
    public static final RegistryKey<PlacedFeature> NUMEN_TREE =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                    new Identifier("numenology", "numen_tree"));

    // 🌑 UMBRA TREE (новый ID)
    public static final RegistryKey<PlacedFeature> UMBRA_TREE =
            RegistryKey.of(RegistryKeys.PLACED_FEATURE,
                    new Identifier("numenology", "umbra_tree"));

    public static void generate() {

        // 🌳 NUMEN (реже, контролируется через rarity_filter)
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION,
                NUMEN_TREE
        );

        // 🌑 UMBRA (ещё реже)
        BiomeModifications.addFeature(
                BiomeSelectors.tag(BiomeTags.IS_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION,
                UMBRA_TREE
        );
    }
}