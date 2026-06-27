package numenology.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

import numenology.Numenology;
import numenology.block.ModBlocks;
import numenology.item.ModItems;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.NUMEN_CRUCIBLE)
                .pattern("I I")
                .pattern("B B")
                .pattern("SSS")
                .input('I', Items.IRON_INGOT)
                .input('B', ModBlocks.HEMATITE_BLOCK)
                .input('S', ModBlocks.HEMATITE_SLAB)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEMATITE_BLOCK, 4)
                .pattern("IDI")
                .pattern("DID")
                .pattern("IDI")
                .input('D', Items.COBBLED_DEEPSLATE)
                .input('I', Items.IRON_NUGGET)
                .criterion(hasItem(Items.COBBLED_DEEPSLATE), conditionsFromItem(Items.COBBLED_DEEPSLATE))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "hematite_block"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.NUMEN_SMELTERY, 1)
                .pattern("BBB")
                .pattern("BIB")
                .pattern("BBB")
                .input('B', ModBlocks.NUMEN_BRICKS)
                .input('I', Items.IRON_BARS)
                .criterion(hasItem(ModBlocks.NUMEN_BRICKS), conditionsFromItem(ModBlocks.NUMEN_BRICKS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_smeltery"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.NUMEN_STEEL_BLOCK, 1)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_steel_block"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.FOCUSING_LENS, 1)
                .pattern(" I ")
                .pattern("IGI")
                .pattern(" I ")
                .input('G', ModBlocks.NUMEN_GLASS)
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "focusing_lens"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.DUFFEL_BAG, 1)
                .pattern("SLS")
                .pattern("L L")
                .pattern("LLL")
                .input('L', Items.LEATHER)
                .input('S', Items.STRING)
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "duffel_bag"));


        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.NUMENOMETER, 1)
                .pattern("  F")
                .pattern(" I ")
                .pattern("S  ")
                .input('F', ModItems.FOCUSING_LENS)
                .input('S', Items.STICK)
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.FOCUSING_LENS), conditionsFromItem(ModItems.FOCUSING_LENS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numenometer"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.NUMEN_SWORD, 1)
                .pattern(" I ")
                .pattern(" I ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_sword"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.NUMEN_PICKAXE, 1)
                .pattern("III")
                .pattern(" S ")
                .pattern(" S ")
                .input('S', Items.STICK)
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_pickaxe"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.NUMEN_STEEL_HELMET, 1)
                .pattern("III")
                .pattern("I I")
                .pattern("   ")
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_steel_helmet"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.NUMEN_STEEL_CHESTPLATE, 1)
                .pattern("I I")
                .pattern("III")
                .pattern("III")
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_steel_chestplate"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.NUMEN_STEEL_LEGGINGS, 1)
                .pattern("III")
                .pattern("I I")
                .pattern("I I")
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_steel_leggings"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.LAMELLAR_PLATE, 3)
                .pattern("ILI")
                .pattern("LLL")
                .pattern("ILI")
                .input('I', Items.IRON_NUGGET)
                .input('L', Items.LEATHER)
                .criterion(hasItem(Items.IRON_INGOT), conditionsFromItem(Items.IRON_INGOT))
                .criterion(hasItem(Items.LEATHER), conditionsFromItem(Items.LEATHER))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "lamellar_plate"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.JAM_JAR, 3)
                .pattern("WWW")
                .pattern("G G")
                .pattern("GGG")
                .input('W', Items.OAK_PLANKS)
                .input('G', Items.GLASS)
                .criterion(hasItem(Items.OAK_PLANKS), conditionsFromItem(Items.OAK_PLANKS))
                .criterion(hasItem(Items.GLASS), conditionsFromItem(Items.GLASS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "jam_jar"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.TURQUOISE_JAM, 1)
                .pattern("   ")
                .pattern("JST")
                .pattern("   ")
                .input('J', ModItems.JAM_JAR)
                .input('T', ModItems.TURQUOISE_BERRIES)
                .input('S', Items.SUGAR)
                .criterion(hasItem(Items.SUGAR), conditionsFromItem(Items.SUGAR))
                .criterion(hasItem(ModItems.TURQUOISE_BERRIES), conditionsFromItem(ModItems.TURQUOISE_BERRIES))
                .criterion(hasItem(ModItems.JAM_JAR), conditionsFromItem(ModItems.JAM_JAR))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "turquoise_jam"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.BERRIES_JAM, 1)
                .pattern("   ")
                .pattern("JSB")
                .pattern("   ")
                .input('J', ModItems.JAM_JAR)
                .input('B', Items.SWEET_BERRIES)
                .input('S', Items.SUGAR)
                .criterion(hasItem(Items.SUGAR), conditionsFromItem(Items.SUGAR))
                .criterion(hasItem(Items.SWEET_BERRIES), conditionsFromItem(Items.SWEET_BERRIES))
                .criterion(hasItem(ModItems.JAM_JAR), conditionsFromItem(ModItems.JAM_JAR))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "berries_jam"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.JAM_SANDWICH, 4)
                .pattern("   ")
                .pattern("JB ")
                .pattern("   ")
                .input('J', ModItems.BERRIES_JAM)
                .input('B', Items.BREAD)
                .criterion(hasItem(ModItems.BERRIES_JAM), conditionsFromItem(ModItems.BERRIES_JAM))
                .criterion(hasItem(Items.BREAD), conditionsFromItem(Items.BREAD))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "jam_sandwich"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.FOOD, ModItems.TURQUOISE_JAM_SANDWICH, 4)
                .pattern("   ")
                .pattern("JB ")
                .pattern("   ")
                .input('J', ModItems.TURQUOISE_JAM)
                .input('B', Items.BREAD)
                .criterion(hasItem(ModItems.TURQUOISE_JAM), conditionsFromItem(ModItems.TURQUOISE_JAM))
                .criterion(hasItem(Items.BREAD), conditionsFromItem(Items.BREAD))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "turquoise_jam_sandwich"));


        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.CANVAS_FABRIC, 3)
                .pattern("   ")
                .pattern("SSS")
                .pattern("SSS")
                .input('S', Items.STRING)
                .criterion(hasItem(Items.STRING), conditionsFromItem(Items.STRING))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "canvas_fabric"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.DRYING_TABLE, 1)
                .pattern("SCS")
                .pattern("S S")
                .pattern("SFS")
                .input('C', ModItems.CANVAS_FABRIC)
                .input('S', Items.COBBLESTONE)
                .input('F', Items.CAMPFIRE)
                .criterion(hasItem(ModItems.CANVAS_FABRIC), conditionsFromItem(ModItems.CANVAS_FABRIC))
                .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                .criterion(hasItem(Items.CAMPFIRE), conditionsFromItem(Items.CAMPFIRE))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "drying_table"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.NUMEN_STEEL_BOOTS, 1)
                .pattern("   ")
                .pattern("I I")
                .pattern("I I")
                .input('I', ModItems.NUMEN_STEEL_INGOT)
                .criterion(hasItem(ModItems.NUMEN_STEEL_INGOT), conditionsFromItem(ModItems.NUMEN_STEEL_INGOT))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_steel_boots"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.LAMELLAR_CHESTPLATE, 1)
                .pattern("L L")
                .pattern("LLL")
                .pattern("LLL")
                .input('L', ModItems.LAMELLAR_PLATE)
                .criterion(hasItem(ModItems.LAMELLAR_PLATE), conditionsFromItem(ModItems.LAMELLAR_PLATE))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "lameral_chestplate"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.MONOCULAR, 1)
                .pattern("   ")
                .pattern("LFL")
                .pattern("   ")
                .input('F', ModItems.FOCUSING_LENS)
                .input('L', ModItems.IMBUED_LEATHER)
                .criterion(hasItem(ModItems.FOCUSING_LENS), conditionsFromItem(ModItems.FOCUSING_LENS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "monocular"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, ModBlocks.CANVAS_BED, 1)
                .pattern("   ")
                .pattern("WCC")
                .pattern("   ")
                .input('C', ModItems.CANVAS_FABRIC)
                .input('W', net.minecraft.registry.tag.ItemTags.WOOL)
                .criterion("has_wool", conditionsFromTag(net.minecraft.registry.tag.ItemTags.WOOL))
                .criterion(hasItem(ModItems.CANVAS_FABRIC), conditionsFromItem(ModItems.CANVAS_FABRIC))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "canvas_bed"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.FOCUSING_GAUNTLET, 1)
                .pattern("ILI")
                .pattern("SFS")
                .pattern("LLL")
                .input('F', ModItems.FOCUSING_LENS)
                .input('L', ModItems.IMBUED_LEATHER)
                .input('S', ModItems.SUPERCONDUCTOR)
                .input('I', Items.IRON_NUGGET)
                .criterion(hasItem(ModItems.FOCUSING_LENS), conditionsFromItem(ModItems.FOCUSING_LENS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "focusing_gauntlet"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.GARDEN_BAG, 1)
                .pattern("LFL")
                .pattern("FDF")
                .pattern("FFF")
                .input('F', ModItems.NUMEN_FABRIC)
                .input('L', ModItems.IMBUED_LEATHER)
                .input('D', Items.DIRT)
                .criterion(hasItem(ModItems.NUMEN_FABRIC), conditionsFromItem(ModItems.NUMEN_FABRIC))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "garden_bag"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.SCRIBER_TOOLS, 1)
                .pattern("BF")
                .pattern("IP")
                .input('F', Items.FEATHER)
                .input('B', Items.GLASS_BOTTLE)
                .input('I', Items.INK_SAC)
                .input('P', Items.PAPER)
                .criterion(hasItem(Items.GLASS_BOTTLE), conditionsFromItem(Items.GLASS_BOTTLE))
                .criterion(hasItem(Items.INK_SAC), conditionsFromItem(Items.INK_SAC))
                .criterion(hasItem(Items.PAPER), conditionsFromItem(Items.PAPER))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "scriber_tools"));

        // Рецепт для Hematite Wall
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEMATITE_WALL, 6)
                .pattern("###")
                .pattern("###")
                .input('#', ModBlocks.HEMATITE_BLOCK)
                .criterion(hasItem(ModBlocks.HEMATITE_BLOCK), conditionsFromItem(ModBlocks.HEMATITE_BLOCK))
                .offerTo(exporter);

        // Рецепт для Hematite Brick Wall
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEMATITE_BRICK_WALL, 6)
                .pattern("###")
                .pattern("###")
                .input('#', ModBlocks.HEMATITE_BRICKS)
                .criterion(hasItem(ModBlocks.HEMATITE_BRICKS), conditionsFromItem(ModBlocks.HEMATITE_BRICKS))
                .offerTo(exporter);

        createSlabRecipe(RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.HEMATITE_SLAB,
                Ingredient.ofItems(ModBlocks.HEMATITE_BLOCK))
                .criterion(hasItem(ModBlocks.HEMATITE_BLOCK),
                        conditionsFromItem(ModBlocks.HEMATITE_BLOCK))
                .offerTo(exporter);

        createStairsRecipe(ModBlocks.HEMATITE_STAIRS,
                Ingredient.ofItems(ModBlocks.HEMATITE_BLOCK))
                .criterion(hasItem(ModBlocks.HEMATITE_BLOCK),
                        conditionsFromItem(ModBlocks.HEMATITE_BLOCK))
                .offerTo(exporter);

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.HEMATITE_BRICKS, 4)
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.HEMATITE_BLOCK)
                .criterion(hasItem(ModBlocks.HEMATITE_BLOCK),
                        conditionsFromItem(ModBlocks.HEMATITE_BLOCK))
                .offerTo(exporter);

        createSlabRecipe(RecipeCategory.BUILDING_BLOCKS,
                ModBlocks.HEMATITE_BRICK_SLAB,
                Ingredient.ofItems(ModBlocks.HEMATITE_BRICKS))
                .criterion(hasItem(ModBlocks.HEMATITE_BRICKS),
                        conditionsFromItem(ModBlocks.HEMATITE_BRICKS))
                .offerTo(exporter);

        createStairsRecipe(ModBlocks.HEMATITE_BRICK_STAIRS,
                Ingredient.ofItems(ModBlocks.HEMATITE_BRICKS))
                .criterion(hasItem(ModBlocks.HEMATITE_BRICKS),
                        conditionsFromItem(ModBlocks.HEMATITE_BRICKS))
                .offerTo(exporter);

        offerSmelting(exporter,
                ModBlocks.NUMEN_LOG,
                ModItems.NUMEN_RESIN,
                "numen_resin_from_log");

        offerSmelting(exporter,
                ModBlocks.NUMEN_RESIN_LOG,
                ModItems.NUMEN_RESIN,
                "numen_resin_from_resin_log");

        createPlanks(exporter, ModBlocks.LUMEN_PLANKS, ModBlocks.LUMEN_LOG, "lumen_planks_from_log");
        createPlanks(exporter, ModBlocks.LUMEN_PLANKS, ModBlocks.LUMEN_RESIN_LOG, "lumen_planks_from_resin");

        createPlanks(exporter, ModBlocks.NUMEN_PLANKS, ModBlocks.NUMEN_LOG, "numen_planks_from_log");
        createPlanks(exporter, ModBlocks.NUMEN_PLANKS, ModBlocks.NUMEN_RESIN_LOG, "numen_planks_from_resin");

        createPlanks(exporter, ModBlocks.UMBRA_PLANKS, ModBlocks.UMBRA_LOG, "umbra_planks_from_log");
        createPlanks(exporter, ModBlocks.UMBRA_PLANKS, ModBlocks.UMBRA_RESIN_LOG, "umbra_planks_from_resin");

        createSticks(exporter, ModBlocks.LUMEN_PLANKS, "lumen_sticks");
        createSticks(exporter, ModBlocks.NUMEN_PLANKS, "numen_sticks");
        createSticks(exporter, ModBlocks.UMBRA_PLANKS, "umbra_sticks");


        createWoodSet(exporter,
                ModBlocks.NUMEN_PLANKS,
                ModBlocks.NUMEN_SLAB,
                ModBlocks.NUMEN_STAIRS,
                ModBlocks.NUMEN_FENCE,
                ModBlocks.NUMEN_FENCE_GATE,
                "numen");

        createWoodSet(exporter,
                ModBlocks.UMBRA_PLANKS,
                ModBlocks.UMBRA_SLAB,
                ModBlocks.UMBRA_STAIRS,
                ModBlocks.UMBRA_FENCE,
                ModBlocks.UMBRA_FENCE_GATE,
                "umbra");

        createWoodSet(exporter,
                ModBlocks.LUMEN_PLANKS,
                ModBlocks.LUMEN_SLAB,
                ModBlocks.LUMEN_STAIRS,
                ModBlocks.LUMEN_FENCE,
                ModBlocks.LUMEN_FENCE_GATE,
                "lumen");

        // ===== NUMEN DOOR =====
        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.NUMEN_DOOR, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.NUMEN_PLANKS)
                .criterion(hasItem(ModBlocks.NUMEN_PLANKS),
                        conditionsFromItem(ModBlocks.NUMEN_PLANKS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "numen_door"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.UMBRA_DOOR, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.UMBRA_PLANKS)
                .criterion(hasItem(ModBlocks.UMBRA_PLANKS),
                        conditionsFromItem(ModBlocks.UMBRA_PLANKS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "umbra_door"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, ModBlocks.LUMEN_DOOR, 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.LUMEN_PLANKS)
                .criterion(hasItem(ModBlocks.LUMEN_PLANKS),
                        conditionsFromItem(ModBlocks.LUMEN_PLANKS))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, "lumen_door"));


        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, ModBlocks.NUMEN_BRICKS, 4)
                .pattern("##")
                .pattern("##")
                .input('#', ModBlocks.NUMEN_STONE)
                .criterion(hasItem(ModBlocks.NUMEN_STONE),
                        conditionsFromItem(ModBlocks.NUMEN_STONE))
                .offerTo(exporter);

        createStoneSet(exporter,
                ModBlocks.NUMEN_STONE,
                ModBlocks.NUMEN_STONE_SLAB,
                ModBlocks.NUMEN_STONE_STAIRS,
                ModBlocks.NUMEN_STONE_WALL,
                "numen_stone");

        createStoneSet(exporter,
                ModBlocks.NUMEN_BRICKS,
                ModBlocks.NUMEN_BRICK_SLAB,
                ModBlocks.NUMEN_BRICK_STAIRS,
                ModBlocks.NUMEN_BRICK_WALL,
                "numen_bricks");

        // =========================
// RESEARCH TABLE
// =========================

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.RESEARCH_TABLE)
                .pattern("ACA")
                .pattern("D D")
                .pattern("D D")
                .input('A', ModBlocks.UMBRA_LOG)
                .input('C', ModBlocks.NUMEN_SLAB)
                .input('D', ModBlocks.UMBRA_PLANKS)
                .criterion(hasItem(ModBlocks.UMBRA_LOG), conditionsFromItem(ModBlocks.UMBRA_LOG))
                .offerTo(exporter);


    }


    private void offerSmelting(Consumer<RecipeJsonProvider> exporter,
                               net.minecraft.block.Block input,
                               net.minecraft.item.Item output,
                               String id) {

        CookingRecipeJsonBuilder.createSmelting(
                        Ingredient.ofItems(input),
                        RecipeCategory.MISC,
                        output,
                        0.3f,
                        200
                )
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, id));
    }

    private void createPlanks(Consumer<RecipeJsonProvider> exporter,
                              net.minecraft.block.Block result,
                              net.minecraft.block.Block input,
                              String id) {

        ShapelessRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, result, 4)
                .input(input)
                .criterion(hasItem(input), conditionsFromItem(input))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, id));
    }

    private void createSticks(Consumer<RecipeJsonProvider> exporter,
                              net.minecraft.block.Block planks,
                              String id) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, Items.STICK, 4)
                .pattern("#")
                .pattern("#")
                .input('#', planks)
                .criterion(hasItem(planks), conditionsFromItem(planks))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, id));
    }

    private void createWoodSet(Consumer<RecipeJsonProvider> exporter,
                               net.minecraft.block.Block planks,
                               net.minecraft.block.Block slab,
                               net.minecraft.block.Block stairs,
                               net.minecraft.block.Block fence,
                               net.minecraft.block.Block gate,
                               String name) {

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .input('#', planks)
                .criterion(hasItem(planks), conditionsFromItem(planks))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_slab"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .input('#', planks)
                .criterion(hasItem(planks), conditionsFromItem(planks))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_stairs"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, fence, 3)
                .pattern("#S#")
                .pattern("#S#")
                .input('#', planks)
                .input('S', Items.STICK)
                .criterion(hasItem(planks), conditionsFromItem(planks))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_fence"));


        ShapedRecipeJsonBuilder.create(RecipeCategory.REDSTONE, gate)
                .pattern("S#S")
                .pattern("S#S")
                .input('#', planks)
                .input('S', Items.STICK)
                .criterion(hasItem(planks), conditionsFromItem(planks))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_fence_gate"));
    }

    private void createStoneSet(Consumer<RecipeJsonProvider> exporter,
                                net.minecraft.block.Block base,
                                net.minecraft.block.Block slab,
                                net.minecraft.block.Block stairs,
                                net.minecraft.block.Block wall, // ← ДОБАВИЛИ ПАРАМЕТР
                                String name) {

        // SLAB
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, slab, 6)
                .pattern("###")
                .input('#', base)
                .criterion(hasItem(base), conditionsFromItem(base))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_slab"));

        // STAIRS
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, stairs, 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .input('#', base)
                .criterion(hasItem(base), conditionsFromItem(base))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_stairs"));

        // WALL
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, wall, 6)
                .pattern("###")
                .pattern("###")
                .input('#', base)
                .criterion(hasItem(base), conditionsFromItem(base))
                .offerTo(exporter, new Identifier(Numenology.MOD_ID, name + "_wall"));
    }

}