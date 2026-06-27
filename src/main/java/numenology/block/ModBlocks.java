package numenology.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;

import net.minecraft.block.*;
import net.minecraft.block.WoodType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.TallBlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;


import numenology.Numenology;
import numenology.block.custom.*;
import numenology.nodes.NumenNodeBlock;
import numenology.world.gen.NumenTreeGrower;
import numenology.world.gen.UmbraTreeGrower;
import numenology.world.gen.LumenTreeGrower;

public class ModBlocks {


    public static final Block NUMEN_ORE = new Block(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f, 3.0f).requiresTool()
    );

    public static final Block NUMEN_CLUSTER = new NumenClusterBlock(
            FabricBlockSettings.copyOf(Blocks.STONE)
                    .strength(3.0f, 3.0f)
                    .sounds(BlockSoundGroup.AMETHYST_CLUSTER)
                    .nonOpaque()
                    .luminance(state -> 7)
    );

    public static final Block TURQUOISE_BUSH = registerBlock("turquoise_bush",
            new TurquoiseBushBlock(FabricBlockSettings.copyOf(Blocks.AZALEA).nonOpaque()));

    public static final Block DEEPSLATE_NUMEN_ORE = new Block(
            FabricBlockSettings.copyOf(Blocks.DEEPSLATE).strength(4.5f, 3.0f).requiresTool()
    );

    public static final Block NUMEN_NODE = new NumenNodeBlock(
            AbstractBlock.Settings.create().strength(-1.0f).nonOpaque().noCollision().luminance(state -> 12)
    );

    public static Block NUMEN_CRUCIBLE = new NumenCrucibleBlock(
            FabricBlockSettings.copyOf(Blocks.CAULDRON).strength(2.5f).nonOpaque()
    );

    public static final Block HEMATITE_BLOCK = new Block(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f).requiresTool()
    );

    public static final Block HEMATITE_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f).requiresTool()
    );

    public static final Block HEMATITE_STAIRS = new StairsBlock(
            HEMATITE_BLOCK.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f).requiresTool()
    );

    public static final Block HEMATITE_BRICKS = new Block(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f).requiresTool()
    );

    public static final Block HEMATITE_BRICK_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f).requiresTool()
    );

    public static final Block HEMATITE_BRICK_STAIRS = new StairsBlock(
            HEMATITE_BRICKS.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.0f).requiresTool()
    );

    // ======================
    // NUMEN STONE
    // ======================

    public static final Block NUMEN_STONE = new Block(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.5f).requiresTool()
    );

    public static final Block NUMEN_STONE_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.5f).requiresTool()
    );

    public static final Block NUMEN_STONE_STAIRS = new StairsBlock(
            NUMEN_STONE.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.5f).requiresTool()
    );

    public static final Block NUMEN_BRICKS = new Block(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.5f).requiresTool()
    );


    public static final Block NUMEN_BRICK_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.5f).requiresTool()
    );

    public static final Block NUMEN_BRICK_STAIRS = new StairsBlock(
            NUMEN_BRICKS.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.STONE).strength(3.5f).requiresTool()
    );

    public static final Block NUMEN_SAPLING = new SaplingBlock(
            new NumenTreeGrower(),
            FabricBlockSettings.copyOf(Blocks.BIRCH_SAPLING)
    );

    public static final Block NUMEN_LOG = new NumenLogBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_LOG)
    );

    public static final Block NUMEN_RESIN_LOG = new NumenLogBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_LOG)
    );

    public static final Block STRIPPED_NUMEN_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.STRIPPED_BIRCH_LOG)
    );

    public static final Block NUMEN_LEAVES = new LeavesBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES).ticksRandomly().nonOpaque()
    );

    public static final Block NUMEN_PLANKS = new Block(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block NUMEN_STEEL_BLOCK = registerBlock(
            "numen_steel_block",
            new Block(AbstractBlock.Settings
                    .create()
                    .strength(3.5f)
                    .requiresTool()
            )
    );

    public static final Block RUNIC_NUMEN_BRICKS = registerBlock(
            "runic_numen_bricks",
            new Block(AbstractBlock.Settings
                    .create()
                    .strength(5.0f)
                    .requiresTool()
                    .luminance(state -> 5)
            )
    );

    public static final Block HEMATITE_WALL = registerBlock("hematite_wall",
            new WallBlock(FabricBlockSettings.copyOf(ModBlocks.HEMATITE_BLOCK)));

    public static final Block HEMATITE_BRICK_WALL = registerBlock("hematite_brick_wall",
            new WallBlock(FabricBlockSettings.copyOf(ModBlocks.HEMATITE_BRICKS)));

    public static final Block NUMEN_STONE_WALL = registerBlock("numen_stone_wall",
            new WallBlock(FabricBlockSettings.copyOf(ModBlocks.NUMEN_STONE)));

    public static final Block NUMEN_BRICK_WALL = registerBlock("numen_brick_wall",
            new WallBlock(FabricBlockSettings.copyOf(ModBlocks.NUMEN_BRICKS)));

    public static final Block CANVAS_BED = registerBlock("canvas_bed",
            new CanvasBedBlock(FabricBlockSettings.copyOf(Blocks.WHITE_BED).nonOpaque()));

    public static final Block NUMEN_SMELTERY = registerBlock(
            "numen_smeltery",
            new NumenSmelteryBlock(AbstractBlock.Settings
                    .create()
                    .strength(4.0f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
            )
    );

    public static final Block DRYING_TABLE = registerBlock(
            "drying_table",
            new DryingTableBlock(AbstractBlock.Settings
                    .create()
                    .nonOpaque()
                    .strength(2.5f)
                    .requiresTool()
                    .sounds(BlockSoundGroup.STONE)
                    .luminance(state -> 15)
            )
    );




    public static final Block NUMEN_GLASS = registerBlock(
            "numen_glass",
            new GlassBlock(AbstractBlock.Settings
                    .create()
                    .strength(0.3f)
                    .nonOpaque()
            )
    );

    // ✅ ФИКС: ВАНИЛЬНАЯ ДВЕРЬ
    public static final Block NUMEN_DOOR = new DoorBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_DOOR).nonOpaque(),
            BlockSetType.BIRCH
    );

    public static final Block UMBRA_DOOR = registerBlock("umbra_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_DOOR).nonOpaque(), BlockSetType.BIRCH));

    public static final Block LUMEN_DOOR = registerBlock("lumen_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.BIRCH_DOOR).nonOpaque(), BlockSetType.BIRCH));

    public static final Block NUMEN_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block NUMEN_STAIRS = new StairsBlock(
            NUMEN_PLANKS.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block NUMEN_FENCE = new FenceBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block NUMEN_FENCE_GATE = new FenceGateBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS),
            WoodType.BIRCH
    );

    public static final Block UMBRA_SAPLING = new SaplingBlock(
            new UmbraTreeGrower(),
            FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)
    );

    public static final Block UMBRA_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.OAK_LOG)
    );

    public static final Block UMBRA_RESIN_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.OAK_LOG).strength(3.0f)
    );

    public static final Block STRIPPED_UMBRA_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG)
    );

    public static final Block UMBRA_LEAVES = new UmbraLeavesBlock(
            FabricBlockSettings.copyOf(Blocks.OAK_LEAVES).ticksRandomly().nonOpaque()
    );

    public static final Block UMBRA_PLANKS = new Block(
            FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
    );

    public static final Block UMBRA_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
    );

    public static final Block UMBRA_STAIRS = new StairsBlock(
            UMBRA_PLANKS.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
    );

    public static final Block UMBRA_FENCE = new FenceBlock(
            FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
    );

    public static final Block UMBRA_FENCE_GATE = new FenceGateBlock(
            FabricBlockSettings.copyOf(Blocks.OAK_PLANKS),
            WoodType.OAK
    );

    public static final Block LUMEN_SAPLING = new SaplingBlock(
            new LumenTreeGrower(),
            FabricBlockSettings.copyOf(Blocks.BIRCH_SAPLING)
    );

    public static final Block LUMEN_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_LOG)
    );

    public static final Block LUMEN_RESIN_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_LOG)
    );

    public static final Block STRIPPED_LUMEN_LOG = new PillarBlock(
            FabricBlockSettings.copyOf(Blocks.STRIPPED_BIRCH_LOG)
    );

    public static final Block LUMEN_LEAVES = new LeavesBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_LEAVES).ticksRandomly().nonOpaque().luminance(state -> 7)
    );

    public static final Block LUMEN_PLANKS = new Block(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block LUMEN_SLAB = new SlabBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block LUMEN_STAIRS = new StairsBlock(
            LUMEN_PLANKS.getDefaultState(),
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block LUMEN_FENCE = new FenceBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS)
    );

    public static final Block LUMEN_FENCE_GATE = new FenceGateBlock(
            FabricBlockSettings.copyOf(Blocks.BIRCH_PLANKS),
            WoodType.BIRCH
    );

    public static final Block RESEARCH_TABLE = registerBlock(
            "research_table",
            new ResearchTableBlock(
                    FabricBlockSettings.copyOf(Blocks.CAULDRON)
                            .strength(2.5f)
                            .sounds(BlockSoundGroup.WOOD)
            )
    );

    public static void registerModBlocks() {

        registerBlock("numen_ore", NUMEN_ORE);
        registerBlock("deepslate_numen_ore", DEEPSLATE_NUMEN_ORE);
        registerBlock("numen_node", NUMEN_NODE);

        NUMEN_CRUCIBLE = registerBlock("numen_crucible", NUMEN_CRUCIBLE);

        registerBlock("hematite_block", HEMATITE_BLOCK);
        registerBlock("hematite_slab", HEMATITE_SLAB);
        registerBlock("hematite_stairs", HEMATITE_STAIRS);
        registerBlock("hematite_bricks", HEMATITE_BRICKS);
        registerBlock("hematite_brick_slab", HEMATITE_BRICK_SLAB);
        registerBlock("hematite_brick_stairs", HEMATITE_BRICK_STAIRS);

        registerBlock("numen_stone", NUMEN_STONE);
        registerBlock("numen_stone_slab", NUMEN_STONE_SLAB);
        registerBlock("numen_stone_stairs", NUMEN_STONE_STAIRS);
        registerBlock("numen_bricks", NUMEN_BRICKS);
        registerBlock("numen_brick_slab", NUMEN_BRICK_SLAB);
        registerBlock("numen_brick_stairs", NUMEN_BRICK_STAIRS);

        registerBlock("numen_sapling", NUMEN_SAPLING);
        registerBlock("numen_log", NUMEN_LOG);
        registerBlock("numen_resin_log", NUMEN_RESIN_LOG);
        registerBlock("stripped_numen_log", STRIPPED_NUMEN_LOG);
        registerBlock("numen_leaves", NUMEN_LEAVES);
        registerBlock("numen_planks", NUMEN_PLANKS);
        registerBlock("numen_slab", NUMEN_SLAB);
        registerBlock("numen_stairs", NUMEN_STAIRS);
        registerBlock("numen_fence", NUMEN_FENCE);
        registerBlock("numen_fence_gate", NUMEN_FENCE_GATE);

        registerBlock("umbra_sapling", UMBRA_SAPLING);
        registerBlock("umbra_log", UMBRA_LOG);
        registerBlock("umbra_resin_log", UMBRA_RESIN_LOG);
        registerBlock("stripped_umbra_log", STRIPPED_UMBRA_LOG);
        registerBlock("umbra_leaves", UMBRA_LEAVES);
        registerBlock("umbra_planks", UMBRA_PLANKS);
        registerBlock("umbra_slab", UMBRA_SLAB);
        registerBlock("umbra_stairs", UMBRA_STAIRS);
        registerBlock("umbra_fence", UMBRA_FENCE);
        registerBlock("umbra_fence_gate", UMBRA_FENCE_GATE);

        registerBlock("lumen_sapling", LUMEN_SAPLING);
        registerBlock("lumen_log", LUMEN_LOG);
        registerBlock("lumen_resin_log", LUMEN_RESIN_LOG);
        registerBlock("stripped_lumen_log", STRIPPED_LUMEN_LOG);
        registerBlock("lumen_leaves", LUMEN_LEAVES);
        registerBlock("lumen_planks", LUMEN_PLANKS);
        registerBlock("lumen_slab", LUMEN_SLAB);
        registerBlock("lumen_stairs", LUMEN_STAIRS);
        registerBlock("lumen_fence", LUMEN_FENCE);
        registerBlock("lumen_fence_gate", LUMEN_FENCE_GATE);

        registerBlock("numen_door", NUMEN_DOOR);
        registerBlock("numen_cluster", NUMEN_CLUSTER);
    }


    private static Block registerBlock(String name, Block block) {
        Identifier id = new Identifier(Numenology.MOD_ID, name);
        Block registered = Registry.register(Registries.BLOCK, id, block);

        if (block instanceof DoorBlock) {
            Registry.register(Registries.ITEM, id,
                    new TallBlockItem(registered, new FabricItemSettings()));
        } else {
            Registry.register(Registries.ITEM, id,
                    new BlockItem(registered, new FabricItemSettings()));
        }

        return registered;
    }
}