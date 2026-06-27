package numenology.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;

import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;

import java.util.concurrent.CompletableFuture;

import numenology.block.ModBlocks;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public ModBlockTagProvider(FabricDataOutput output,
                               CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        getOrCreateTagBuilder(BlockTags.AXE_MINEABLE)

                .add(ModBlocks.NUMEN_LOG)
                .add(ModBlocks.NUMEN_RESIN_LOG)
                .add(ModBlocks.STRIPPED_NUMEN_LOG)
                .add(ModBlocks.NUMEN_PLANKS)
                .add(ModBlocks.NUMEN_SLAB)
                .add(ModBlocks.NUMEN_STAIRS)
                .add(ModBlocks.NUMEN_FENCE)
                .add(ModBlocks.NUMEN_FENCE_GATE)
                .add(ModBlocks.NUMEN_DOOR) // ← ДОБАВЛЕНО
                .add(ModBlocks.UMBRA_DOOR)
                .add(ModBlocks.LUMEN_DOOR)
                .add(ModBlocks.UMBRA_LOG)
                .add(ModBlocks.UMBRA_RESIN_LOG)
                .add(ModBlocks.STRIPPED_UMBRA_LOG)
                .add(ModBlocks.UMBRA_PLANKS)
                .add(ModBlocks.UMBRA_SLAB)
                .add(ModBlocks.UMBRA_STAIRS)
                .add(ModBlocks.UMBRA_FENCE)
                .add(ModBlocks.UMBRA_FENCE_GATE)
                .add(ModBlocks.RESEARCH_TABLE)

                .add(ModBlocks.LUMEN_LOG)
                .add(ModBlocks.LUMEN_RESIN_LOG)
                .add(ModBlocks.STRIPPED_LUMEN_LOG)
                .add(ModBlocks.LUMEN_PLANKS)
                .add(ModBlocks.LUMEN_SLAB)
                .add(ModBlocks.LUMEN_STAIRS)
                .add(ModBlocks.LUMEN_FENCE)
                .add(ModBlocks.LUMEN_FENCE_GATE)
                .add(ModBlocks.CANVAS_BED);

        // Тег для оград (чтобы они соединялись)
        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(ModBlocks.HEMATITE_WALL)
                .add(ModBlocks.HEMATITE_BRICK_WALL)
                .add(ModBlocks.NUMEN_STONE_WALL)
                .add(ModBlocks.NUMEN_BRICK_WALL);

        //Тег кроватей
        getOrCreateTagBuilder(BlockTags.BEDS)
                .add(ModBlocks.CANVAS_BED);

        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)

                .add(ModBlocks.NUMEN_ORE)
                .add(ModBlocks.DEEPSLATE_NUMEN_ORE)
                .add(ModBlocks.NUMEN_CLUSTER)
                .add(ModBlocks.NUMEN_CRUCIBLE)
                .add(ModBlocks.NUMEN_STEEL_BLOCK)
                .add(ModBlocks.NUMEN_GLASS)
                .add(ModBlocks.NUMEN_SMELTERY)
                .add(ModBlocks.DRYING_TABLE)
                .add(ModBlocks.RUNIC_NUMEN_BRICKS)

                .add(ModBlocks.NUMEN_STONE)
                .add(ModBlocks.NUMEN_STONE_SLAB)
                .add(ModBlocks.NUMEN_STONE_STAIRS)
                .add(ModBlocks.NUMEN_BRICKS)
                .add(ModBlocks.NUMEN_BRICK_SLAB)
                .add(ModBlocks.NUMEN_BRICK_STAIRS)

                .add(ModBlocks.HEMATITE_BLOCK)
                .add(ModBlocks.HEMATITE_SLAB)
                .add(ModBlocks.HEMATITE_STAIRS)
                .add(ModBlocks.HEMATITE_BRICKS)
                .add(ModBlocks.HEMATITE_BRICK_SLAB)
                .add(ModBlocks.HEMATITE_WALL)
                .add(ModBlocks.HEMATITE_BRICK_WALL)
                .add(ModBlocks.NUMEN_STONE_WALL)
                .add(ModBlocks.NUMEN_BRICK_WALL)
                .add(ModBlocks.HEMATITE_BRICK_STAIRS);


        getOrCreateTagBuilder(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.NUMEN_ORE)
                .add(ModBlocks.DEEPSLATE_NUMEN_ORE)
                .add(ModBlocks.NUMEN_CRUCIBLE)
                .add(ModBlocks.NUMEN_STEEL_BLOCK)
                .add(ModBlocks.NUMEN_SMELTERY)

                .add(ModBlocks.NUMEN_STONE)
                .add(ModBlocks.NUMEN_BRICKS)

                .add(ModBlocks.HEMATITE_BLOCK)
                .add(ModBlocks.HEMATITE_BRICKS)
                .add(ModBlocks.HEMATITE_WALL)
                .add(ModBlocks.HEMATITE_BRICK_WALL)
                .add(ModBlocks.NUMEN_STONE_WALL)
                .add(ModBlocks.NUMEN_BRICK_WALL);

        getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(ModBlocks.NUMEN_LEAVES)
                .add(ModBlocks.UMBRA_LEAVES)
                .add(ModBlocks.LUMEN_LEAVES);

        getOrCreateTagBuilder(BlockTags.HOE_MINEABLE)
                .add(ModBlocks.NUMEN_LEAVES)
                .add(ModBlocks.UMBRA_LEAVES)
                .add(ModBlocks.LUMEN_LEAVES);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.NUMEN_LOG)
                .add(ModBlocks.NUMEN_RESIN_LOG)
                .add(ModBlocks.UMBRA_LOG)
                .add(ModBlocks.UMBRA_RESIN_LOG)
                .add(ModBlocks.LUMEN_LOG)
                .add(ModBlocks.LUMEN_RESIN_LOG);

        getOrCreateTagBuilder(BlockTags.PLANKS)
                .add(ModBlocks.NUMEN_PLANKS)
                .add(ModBlocks.UMBRA_PLANKS)
                .add(ModBlocks.LUMEN_PLANKS);

        getOrCreateTagBuilder(BlockTags.FENCES)
                .add(ModBlocks.NUMEN_FENCE)
                .add(ModBlocks.UMBRA_FENCE)
                .add(ModBlocks.LUMEN_FENCE);

        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(ModBlocks.NUMEN_FENCE_GATE)
                .add(ModBlocks.UMBRA_FENCE_GATE)
                .add(ModBlocks.LUMEN_FENCE_GATE);
    }
}