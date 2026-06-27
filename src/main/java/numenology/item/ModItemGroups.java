package numenology.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import numenology.Numenology;
import numenology.block.ModBlocks;

public class ModItemGroups {

    public static final ItemGroup NUMENOLOGY_GROUP = Registry.register(
            Registries.ITEM_GROUP,
            new Identifier(Numenology.MOD_ID, "numenology"),
            FabricItemGroup.builder()
                    .icon(() -> new ItemStack(ModItems.NUMEN_ITEM_GROUP))
                    .displayName(Text.translatable("itemgroup.numenology"))
                    .entries((context, entries) -> {

                        //Base
                        entries.add(ModItems.NUMEN_CODEX);
                        entries.add(ModItems.SCRIBER_TOOLS);

                        //Ore
                        entries.add(ModBlocks.NUMEN_ORE);
                        entries.add(ModBlocks.DEEPSLATE_NUMEN_ORE);
                        entries.add(ModBlocks.NUMEN_CLUSTER);

                        //Materials
                        entries.add(ModItems.NUMEN_RESIN);
                        entries.add(ModItems.LAMELLAR_PLATE);
                        entries.add(ModItems.CANVAS_FABRIC);
                        entries.add(ModItems.IMBUED_LEATHER);
                        entries.add(ModItems.NUMEN_FABRIC);
                        entries.add(ModItems.NUMEN_STEEL_INGOT);
                        entries.add(ModItems.SUPERCONDUCTOR);
                        entries.add(ModBlocks.NUMEN_STEEL_BLOCK);
                        entries.add(ModBlocks.NUMEN_GLASS);
                        entries.add(ModItems.FOCUSING_LENS);
                        entries.add(ModItems.SLAG);

                        //WorkStation
                        entries.add(ModBlocks.CANVAS_BED);
                        entries.add(ModBlocks.RESEARCH_TABLE);
                        entries.add(ModBlocks.DRYING_TABLE);
                        entries.add(ModBlocks.NUMEN_CRUCIBLE);
                        entries.add(ModBlocks.NUMEN_SMELTERY);

                        //Accessories and Tools
                        entries.add(ModItems.DUFFEL_BAG);
                        entries.add(ModItems.GARDEN_BAG);
                        entries.add(ModItems.NUMENOMETER);
                        entries.add(ModItems.MONOCULAR);
                        entries.add(ModItems.FOCUSING_GAUNTLET);

                        //Weapon and Tools
                        entries.add(ModItems.NUMEN_SWORD);
                        entries.add(ModItems.NUMEN_SHOVEL);
                        entries.add(ModItems.NUMEN_PICKAXE);
                        entries.add(ModItems.NUMEN_AXE);
                        entries.add(ModItems.NUMEN_HOE);


                        //Armor
                        entries.add(ModItems.NUMEN_STEEL_HELMET);
                        entries.add(ModItems.NUMEN_STEEL_CHESTPLATE);
                        entries.add(ModItems.NUMEN_STEEL_LEGGINGS);
                        entries.add(ModItems.NUMEN_STEEL_BOOTS);
                        entries.add(ModItems.LAMELLAR_CHESTPLATE);

                        //Food
                        entries.add(ModItems.TURQUOISE_BERRIES);
                        entries.add(ModItems.JAM_JAR);
                        entries.add(ModItems.TURQUOISE_JAM);
                        entries.add(ModItems.BERRIES_JAM);
                        entries.add(ModItems.TURQUOISE_JAM_SANDWICH);
                        entries.add(ModItems.JAM_SANDWICH);

                        //Flora
                        entries.add(ModBlocks.NUMEN_SAPLING);
                        entries.add(ModBlocks.UMBRA_SAPLING);
                        entries.add(ModBlocks.LUMEN_SAPLING);
                        entries.add(ModBlocks.NUMEN_LOG);
                        entries.add(ModBlocks.NUMEN_RESIN_LOG);
                        entries.add(ModBlocks.STRIPPED_NUMEN_LOG);
                        entries.add(ModBlocks.UMBRA_LOG);
                        entries.add(ModBlocks.UMBRA_RESIN_LOG);
                        entries.add(ModBlocks.STRIPPED_UMBRA_LOG);
                        entries.add(ModBlocks.LUMEN_LOG);
                        entries.add(ModBlocks.LUMEN_RESIN_LOG);
                        entries.add(ModBlocks.STRIPPED_LUMEN_LOG);
                        entries.add(ModBlocks.NUMEN_LEAVES);
                        entries.add(ModBlocks.UMBRA_LEAVES);
                        entries.add(ModBlocks.LUMEN_LEAVES);
                        entries.add(ModBlocks.TURQUOISE_BUSH);

                        //Wood Planks
                        entries.add(ModBlocks.NUMEN_PLANKS);
                        entries.add(ModBlocks.NUMEN_SLAB);
                        entries.add(ModBlocks.NUMEN_STAIRS);
                        entries.add(ModBlocks.NUMEN_FENCE);
                        entries.add(ModBlocks.NUMEN_FENCE_GATE);
                        entries.add(ModBlocks.NUMEN_DOOR);

                        entries.add(ModBlocks.UMBRA_PLANKS);
                        entries.add(ModBlocks.UMBRA_SLAB);
                        entries.add(ModBlocks.UMBRA_STAIRS);
                        entries.add(ModBlocks.UMBRA_FENCE);
                        entries.add(ModBlocks.UMBRA_FENCE_GATE);
                        entries.add(ModBlocks.UMBRA_DOOR);

                        entries.add(ModBlocks.LUMEN_PLANKS);
                        entries.add(ModBlocks.LUMEN_SLAB);
                        entries.add(ModBlocks.LUMEN_STAIRS);
                        entries.add(ModBlocks.LUMEN_FENCE);
                        entries.add(ModBlocks.LUMEN_FENCE_GATE);
                        entries.add(ModBlocks.LUMEN_DOOR);


                        //Hematite
                        entries.add(ModBlocks.HEMATITE_BLOCK);
                        entries.add(ModBlocks.HEMATITE_SLAB);
                        entries.add(ModBlocks.HEMATITE_STAIRS);
                        entries.add(ModBlocks.HEMATITE_WALL);
                        entries.add(ModBlocks.HEMATITE_BRICKS);
                        entries.add(ModBlocks.HEMATITE_BRICK_SLAB);
                        entries.add(ModBlocks.HEMATITE_BRICK_STAIRS);
                        entries.add(ModBlocks.HEMATITE_BRICK_WALL);


                        // NUMEN STONE
                        entries.add(ModBlocks.NUMEN_STONE);
                        entries.add(ModBlocks.NUMEN_STONE_SLAB);
                        entries.add(ModBlocks.NUMEN_STONE_STAIRS);
                        entries.add(ModBlocks.NUMEN_STONE_WALL);
                        entries.add(ModBlocks.NUMEN_BRICKS);
                        entries.add(ModBlocks.NUMEN_BRICK_SLAB);
                        entries.add(ModBlocks.NUMEN_BRICK_STAIRS);
                        entries.add(ModBlocks.NUMEN_BRICK_WALL);
                        entries.add(ModBlocks.RUNIC_NUMEN_BRICKS);




                        entries.add(ModBlocks.NUMEN_NODE);
                    })
                    .build()
    );

    public static void registerItemGroups() {
        Numenology.LOGGER.info("Registering Item Groups for " + Numenology.MOD_ID);
    }
}