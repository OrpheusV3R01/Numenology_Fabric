package numenology.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;

import net.minecraft.block.enums.BedPart;
import net.minecraft.data.client.*;

import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import numenology.block.ModBlocks;
import numenology.block.custom.CanvasBedBlock;
import numenology.block.custom.NumenSmelteryBlock;
import numenology.item.ModItems;

public class ModBlockModelProvider extends FabricModelProvider {

    public ModBlockModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator generator) {


        // ======================
        // NUMEN
        // ======================

        generator.registerLog(ModBlocks.NUMEN_LOG).log(ModBlocks.NUMEN_LOG);
        generator.registerLog(ModBlocks.STRIPPED_NUMEN_LOG).log(ModBlocks.STRIPPED_NUMEN_LOG);
        generator.registerLog(ModBlocks.NUMEN_RESIN_LOG).log(ModBlocks.NUMEN_RESIN_LOG);

        generator.registerSimpleCubeAll(ModBlocks.NUMEN_LEAVES);

        generator.registerTintableCrossBlockState(
                ModBlocks.NUMEN_SAPLING,
                BlockStateModelGenerator.TintType.NOT_TINTED
        );

        generator.registerCubeAllModelTexturePool(ModBlocks.NUMEN_PLANKS)
                .slab(ModBlocks.NUMEN_SLAB)
                .stairs(ModBlocks.NUMEN_STAIRS)
                .fence(ModBlocks.NUMEN_FENCE)
                .fenceGate(ModBlocks.NUMEN_FENCE_GATE);

        generator.registerSimpleCubeAll(ModBlocks.NUMEN_ORE);

        // ======================
        // UMBRA
        // ======================

        generator.registerLog(ModBlocks.UMBRA_LOG).log(ModBlocks.UMBRA_LOG);
        generator.registerLog(ModBlocks.STRIPPED_UMBRA_LOG).log(ModBlocks.STRIPPED_UMBRA_LOG);
        generator.registerLog(ModBlocks.UMBRA_RESIN_LOG).log(ModBlocks.UMBRA_RESIN_LOG);

        generator.registerSimpleCubeAll(ModBlocks.UMBRA_LEAVES);

        generator.registerTintableCrossBlockState(
                ModBlocks.UMBRA_SAPLING,
                BlockStateModelGenerator.TintType.NOT_TINTED
        );

        generator.registerCubeAllModelTexturePool(ModBlocks.UMBRA_PLANKS)
                .slab(ModBlocks.UMBRA_SLAB)
                .stairs(ModBlocks.UMBRA_STAIRS)
                .fence(ModBlocks.UMBRA_FENCE)
                .fenceGate(ModBlocks.UMBRA_FENCE_GATE);

        // ======================
        // LUMEN
        // ======================

        generator.registerLog(ModBlocks.LUMEN_LOG).log(ModBlocks.LUMEN_LOG);
        generator.registerLog(ModBlocks.STRIPPED_LUMEN_LOG).log(ModBlocks.STRIPPED_LUMEN_LOG);
        generator.registerLog(ModBlocks.LUMEN_RESIN_LOG).log(ModBlocks.LUMEN_RESIN_LOG);

        generator.registerSimpleCubeAll(ModBlocks.LUMEN_LEAVES);

        generator.registerTintableCrossBlockState(
                ModBlocks.LUMEN_SAPLING,
                BlockStateModelGenerator.TintType.NOT_TINTED
        );

        generator.registerCubeAllModelTexturePool(ModBlocks.LUMEN_PLANKS)
                .slab(ModBlocks.LUMEN_SLAB)
                .stairs(ModBlocks.LUMEN_STAIRS)
                .fence(ModBlocks.LUMEN_FENCE)
                .fenceGate(ModBlocks.LUMEN_FENCE_GATE);

        // ======================
        // ORES
        // ======================

        generator.registerSimpleCubeAll(ModBlocks.DEEPSLATE_NUMEN_ORE);

        // ======================
        // HEMATITE
        // ======================

        generator.registerCubeAllModelTexturePool(ModBlocks.HEMATITE_BLOCK)
                .slab(ModBlocks.HEMATITE_SLAB)
                .stairs(ModBlocks.HEMATITE_STAIRS)
                .wall(ModBlocks.HEMATITE_WALL); // ← ДОБАВЛЕНО

        generator.registerCubeAllModelTexturePool(ModBlocks.HEMATITE_BRICKS)
                .slab(ModBlocks.HEMATITE_BRICK_SLAB)
                .stairs(ModBlocks.HEMATITE_BRICK_STAIRS)
                .wall(ModBlocks.HEMATITE_BRICK_WALL); // ← ДОБАВЛЕНО


        // ======================
        // NUMEN STONE
        // ======================

        generator.registerCubeAllModelTexturePool(ModBlocks.NUMEN_STONE)
                .slab(ModBlocks.NUMEN_STONE_SLAB)
                .stairs(ModBlocks.NUMEN_STONE_STAIRS)
                .wall(ModBlocks.NUMEN_STONE_WALL); // ← ДОБАВЛЕНО

        generator.registerCubeAllModelTexturePool(ModBlocks.NUMEN_BRICKS)
                .slab(ModBlocks.NUMEN_BRICK_SLAB)
                .stairs(ModBlocks.NUMEN_BRICK_STAIRS)
                .wall(ModBlocks.NUMEN_BRICK_WALL); // ← ДОБАВЛЕНО

// ======================
        // CANVAS BED
        // ======================
        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ModBlocks.CANVAS_BED)
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()) // Отвечает за FACING (North, South, East, West)
                        .coordinate(
                                BlockStateVariantMap.create(CanvasBedBlock.PART) // Отвечает за part=head и part=foot
                                        .register(BedPart.FOOT, BlockStateVariant.create().put(VariantSettings.MODEL, new Identifier("numenology", "block/canvas_bed_foot")))
                                        .register(BedPart.HEAD, BlockStateVariant.create().put(VariantSettings.MODEL, new Identifier("numenology", "block/canvas_bed_head")))
                        )
        );

        // ======================
        // NUMEN SMELTERY
        // ======================

        generator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ModBlocks.NUMEN_SMELTERY)
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
                        .coordinate(
                                BlockStateVariantMap.create(NumenSmelteryBlock.LIT)

                                        // ===== OFF =====
                                        .register(false,
                                                BlockStateVariant.create().put(
                                                        VariantSettings.MODEL,
                                                        generator.createSubModel(
                                                                ModBlocks.NUMEN_SMELTERY,
                                                                "_off",
                                                                Models.ORIENTABLE,
                                                                id -> new TextureMap()
                                                                        .put(TextureKey.FRONT, new Identifier("numenology", "block/numen_smeltery_front"))
                                                                        .put(TextureKey.SIDE, new Identifier("numenology", "block/numen_smeltery_side"))
                                                                        .put(TextureKey.TOP, new Identifier("numenology", "block/numen_smeltery_top"))
                                                                        .put(TextureKey.BOTTOM, new Identifier("numenology", "block/numen_smeltery_bottom"))
                                                        )
                                                )
                                        )

                                        // ===== ON =====
                                        .register(true,
                                                BlockStateVariant.create().put(
                                                        VariantSettings.MODEL,
                                                        generator.createSubModel(
                                                                ModBlocks.NUMEN_SMELTERY,
                                                                "_on",
                                                                Models.ORIENTABLE,
                                                                id -> new TextureMap()
                                                                        .put(TextureKey.FRONT, new Identifier("numenology", "block/numen_smeltery_front_on"))
                                                                        .put(TextureKey.SIDE, new Identifier("numenology", "block/numen_smeltery_side"))
                                                                        .put(TextureKey.TOP, new Identifier("numenology", "block/numen_smeltery_top"))
                                                                        .put(TextureKey.BOTTOM, new Identifier("numenology", "block/numen_smeltery_bottom"))
                                                        )
                                                )
                                        )
                        )
        );

        generator.registerParentedItemModel(
                ModBlocks.NUMEN_SMELTERY,
                new Identifier("numenology", "block/numen_smeltery_off")
        );

        // ======================
        // NUMENOLOGY — MATERIALS
        // ======================

        generator.registerSimpleCubeAll(ModBlocks.NUMEN_STEEL_BLOCK);

        generator.registerSimpleCubeAll(ModBlocks.NUMEN_GLASS);
    }


    @Override
    public void generateItemModels(ItemModelGenerator generator) {
        generator.register(ModItems.NUMEN_RESIN, Models.GENERATED);

        generator.register(ModItems.IMBUED_LEATHER, Models.GENERATED);
        generator.register(ModItems.NUMEN_FABRIC, Models.GENERATED);
        generator.register(ModBlocks.CANVAS_BED.asItem(), Models.GENERATED);
    }
}