package numenology.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import numenology.block.ModBlocks;
import numenology.block.custom.NumenResinLogBlock;
import numenology.item.ModItems;

public class ModLootTableProvider extends FabricBlockLootTableProvider {

    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {

        addDrop(ModBlocks.DRYING_TABLE);
        addDrop(ModBlocks.CANVAS_BED);

// ==========================================
        // 🌳 ДРОП ДЛЯ ОБЫЧНОГО БРЕВНА (NUMEN_LOG)
        // ==========================================
        // Метод addSurvivesExplosionCondition принимает блок и НАПРЯМУЮ оборачивает LootPool.builder()
        addDrop(ModBlocks.NUMEN_LOG, block -> LootTable.builder()
                .pool(this.addSurvivesExplosionCondition(block, LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(block)))));

        // ==========================================
        // 💧 ДРОП ДЛЯ СМОЛЯНИСТОГО БРЕВНА (NUMEN_RESIN_LOG)
        // ==========================================
        addDrop(ModBlocks.NUMEN_RESIN_LOG, block -> LootTable.builder()
                .pool(this.addSurvivesExplosionCondition(block, LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1.0F))
                        .with(ItemEntry.builder(block)))));


        addDrop(ModBlocks.UMBRA_RESIN_LOG);
        addDrop(ModBlocks.LUMEN_RESIN_LOG);
        addDrop(ModBlocks.NUMEN_CLUSTER, block -> oreDrops(block, ModItems.NUMEN_RESIN)
                // Применяем функцию изменения количества к выпадающему предмету (смоле)
                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))));

        addDrop(ModBlocks.HEMATITE_BLOCK);
        addDrop(ModBlocks.HEMATITE_STAIRS);
        addDrop(ModBlocks.HEMATITE_SLAB, slabDrops(ModBlocks.HEMATITE_SLAB));
        addDrop(ModBlocks.HEMATITE_WALL);

        addDrop(ModBlocks.HEMATITE_BRICKS);
        addDrop(ModBlocks.HEMATITE_BRICK_STAIRS);
        addDrop(ModBlocks.HEMATITE_BRICK_SLAB, slabDrops(ModBlocks.HEMATITE_BRICK_SLAB));
        addDrop(ModBlocks.HEMATITE_BRICK_WALL);

        addDrop(ModBlocks.NUMEN_STONE);
        addDrop(ModBlocks.NUMEN_STONE_STAIRS);
        addDrop(ModBlocks.NUMEN_STONE_SLAB, slabDrops(ModBlocks.NUMEN_STONE_SLAB));
        addDrop(ModBlocks.NUMEN_STONE_WALL);

        addDrop(ModBlocks.NUMEN_BRICKS);
        addDrop(ModBlocks.NUMEN_BRICK_STAIRS);
        addDrop(ModBlocks.NUMEN_BRICK_SLAB, slabDrops(ModBlocks.NUMEN_BRICK_SLAB));
        addDrop(ModBlocks.RUNIC_NUMEN_BRICKS);
        addDrop(ModBlocks.NUMEN_BRICK_WALL);

        addDrop(ModBlocks.NUMEN_STEEL_BLOCK);
        addDrop(ModBlocks.NUMEN_GLASS);
        addDrop(ModBlocks.NUMEN_SMELTERY);

        // ===== NUMEN DOOR =====
        addDrop(ModBlocks.NUMEN_DOOR, doorDrops(ModBlocks.NUMEN_DOOR));
        addDrop(ModBlocks.LUMEN_DOOR, doorDrops(ModBlocks.LUMEN_DOOR));
        addDrop(ModBlocks.UMBRA_DOOR, doorDrops(ModBlocks.UMBRA_DOOR));

        addDrop(ModBlocks.RESEARCH_TABLE);
    }
}