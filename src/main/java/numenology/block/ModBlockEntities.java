package numenology.block;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import numenology.Numenology;
import numenology.block.entity.DryingTableBlockEntity;
import numenology.block.entity.NumenCrucibleBlockEntity;
import numenology.block.entity.NumenSmelteryBlockEntity;
import numenology.block.entity.ResearchTableBlockEntity;
import numenology.nodes.NumenNodeBlockEntity;

public class ModBlockEntities {

    public static BlockEntityType<NumenNodeBlockEntity> NUMEN_NODE;
    public static BlockEntityType<NumenCrucibleBlockEntity> NUMEN_CRUCIBLE;
    public static BlockEntityType<ResearchTableBlockEntity> RESEARCH_TABLE;
    public static BlockEntityType<NumenSmelteryBlockEntity> NUMEN_SMELTERY_BE;
    public static BlockEntityType<DryingTableBlockEntity> DRYING_TABLE_BE;

    public static void registerBlockEntities() {

        System.out.println("REGISTERING BLOCK ENTITIES");

        // =========================
        // NUMEN NODE
        // =========================
        NUMEN_NODE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Numenology.MOD_ID, "numen_node"),
                FabricBlockEntityTypeBuilder.create(
                        NumenNodeBlockEntity::new,
                        ModBlocks.NUMEN_NODE
                ).build(null)
        );


            NUMEN_SMELTERY_BE = Registry.register(
                    Registries.BLOCK_ENTITY_TYPE,
                    new Identifier(Numenology.MOD_ID, "numen_smeltery"),
                    FabricBlockEntityTypeBuilder.create(
                            NumenSmelteryBlockEntity::new,
                            ModBlocks.NUMEN_SMELTERY
                    ).build()
            );

        // =========================
        // NUMEN CRUCIBLE
        // =========================
        NUMEN_CRUCIBLE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Numenology.MOD_ID, "numen_crucible"),
                FabricBlockEntityTypeBuilder.create(
                        NumenCrucibleBlockEntity::new,
                        Registries.BLOCK.get(new Identifier(Numenology.MOD_ID, "numen_crucible"))
                ).build(null)
        );

        // =========================
        // RESEARCH TABLE
        // =========================
        RESEARCH_TABLE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Numenology.MOD_ID, "research_table"),
                FabricBlockEntityTypeBuilder.create(
                        ResearchTableBlockEntity::new,
                        ModBlocks.RESEARCH_TABLE
                ).build(null)
        );

        DRYING_TABLE_BE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(Numenology.MOD_ID, "drying_table"),
                FabricBlockEntityTypeBuilder.create(
                        DryingTableBlockEntity::new,
                        ModBlocks.DRYING_TABLE
                ).build(null)
        );

        System.out.println("BLOCK ENTITIES REGISTERED");
    }
}