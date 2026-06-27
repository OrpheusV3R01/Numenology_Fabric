package numenology.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import numenology.item.custom.*;

import numenology.Numenology;
import numenology.item.custom.CodexItem;

public class ModItems {

    public static final Item DUFFEL_BAG = registerItem("duffel_bag",
            new DuffelBagItem(new net.fabricmc.fabric.api.item.v1.FabricItemSettings().maxCount(1))
    );

    public static final Item NUMEN_STEEL_INGOT = registerItem("numen_steel_ingot",
            new Item(new Item.Settings()));

    public static final Item LAMELLAR_PLATE = registerItem("lamellar_plate",
            new Item(new Item.Settings()));


    public static final Item SUPERCONDUCTOR = registerItem("superconductor",
            new Item(new Item.Settings()));

    public static final Item NUMEN_RESIN = registerItem(
            "numen_resin",
            new Item(new FabricItemSettings())
    );

    public static final Item NUMEN_ITEM_GROUP = registerItem(
            "numen_item_group",
            new Item(new FabricItemSettings())
    );

    public static final Item FOCUSING_LENS = registerItem(
            "focusing_lens",
            new Item(new FabricItemSettings())
    );

    public static final Item NUMENOMETER = registerItem(
            "numenometer",
            new NumenometerItem(new FabricItemSettings().maxCount(1))
    );

    public static final Item IMBUED_LEATHER = registerItem(
            "imbued_leather",
            new Item(new FabricItemSettings())
    );

    public static final Item NUMEN_FABRIC = registerItem(
            "numen_fabric",
            new Item(new FabricItemSettings())
    );

    public static final Item SLAG = registerItem(
            "slag",
            new Item(new Item.Settings())
    );

    public static final Item CANVAS_FABRIC = registerItem(
            "canvas_fabric",
            new Item(new Item.Settings())
    );

    public static final Item NUMEN_CODEX = registerItem(
            "numen_codex",
            new CodexItem(new Item.Settings().maxCount(1))
    );

    public static final Item NUMEN_NODE_CORE = registerItem(
            "numen_node_core",
            new CodexItem(new Item.Settings().maxCount(1))
    );

    public static final Item SCRIBER_TOOLS = registerItem(
            "scriber_tools",
            new ScriberToolsItem(new Item.Settings().maxCount(1))
    );

    // Убедись, что импортировал dev.emi.trinkets.api.TrinketItem
    public static final Item MONOCULAR = registerItem("monocular",
            new MonocularItem(new FabricItemSettings().maxCount(1))); // Исправлено! Используем твой класс с поддержкой GeckoLib

    // Добавь в список предметов:
    public static final Item FOCUSING_GAUNTLET = registerItem(
            "focusing_gauntlet",
            new FocusingGauntletItem(new net.fabricmc.fabric.api.item.v1.FabricItemSettings().maxCount(1))
    );


    public static final Item NUMEN_SWORD = registerItem("numen_sword",
            new SwordItem(
                    NumenToolMaterial.NUMEN_STEEL,
                    3,       // Добавочный урон меча (Итоговый урон: 1 урон игрока + 4 материала + 3 меча = 8 урона)
                    -2.4f,   // Скорость атаки (Определяет кулдаун замаха)
                    new Item.Settings() // Настройки предмета, прочность подтянется из материала автоматически
            )
    );

    public static final Item NUMEN_PICKAXE = registerItem("numen_pickaxe",
            new PickaxeItem(
                    NumenToolMaterial.NUMEN_STEEL,
                    1,
                    -2.8f,
                    new Item.Settings()
            )
    );

    public static final Item NUMEN_STEEL_HELMET = registerItem(
            "numen_steel_helmet",
            new ArmorItem(NumenArmorMaterial.NUMEN_STEEL, ArmorItem.Type.HELMET, new Item.Settings().maxCount(1))
    );

    public static final Item NUMEN_STEEL_CHESTPLATE = registerItem(
            "numen_steel_chestplate",
            new NumenSteelChestplateItem(
                    NumenArmorMaterial.NUMEN_STEEL,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxCount(1)
            )
    );

    public static final Item LAMELLAR_CHESTPLATE = registerItem(
            "lamellar_chestplate",
            new LamellarChestplateItem(
                    NumenArmorMaterial.LAMELLAR,
                    ArmorItem.Type.CHESTPLATE,
                    new Item.Settings().maxCount(1)
            )
    );

    public static final Item NUMEN_STEEL_LEGGINGS = registerItem(
            "numen_steel_leggings",
            new NumenSteelLeggingsItem(
                    NumenArmorMaterial.NUMEN_STEEL,
                    ArmorItem.Type.LEGGINGS,
                    new Item.Settings().maxCount(1)
            )
    );

    public static final Item NUMEN_STEEL_BOOTS = registerItem(
            "numen_steel_boots",
            new NumenSteelBootsItem(
                    NumenArmorMaterial.NUMEN_STEEL,
                    ArmorItem.Type.BOOTS,
                    new Item.Settings().maxCount(1)
            )
    );

    public static final Item TURQUOISE_BERRIES = registerItem("turquoise_berries",
            new Item(new FabricItemSettings().food(
                    new net.minecraft.item.FoodComponent.Builder()
                            .hunger(4) // 2 полных окорочка
                            .saturationModifier(0.6f) // Уровень насыщения как у хлеба
                            // Регенерация I на 5 секунд (100 тиков) с шансом 100%
                            .statusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                                    net.minecraft.entity.effect.StatusEffects.REGENERATION, 100, 0), 1.0f)
                            .alwaysEdible()
                            .build()
            )));

    public static final Item TURQUOISE_JAM = registerItem("turquoise_jam",
            new Item(new FabricItemSettings()
                    .maxCount(16) // Ограничение стака до 16
                    .food(new net.minecraft.item.FoodComponent.Builder()
                            .hunger(7)
                            .saturationModifier(0.6f)
                            .statusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                                    net.minecraft.entity.effect.StatusEffects.REGENERATION, 200, 0), 1.0f)
                            .statusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                                    net.minecraft.entity.effect.StatusEffects.NIGHT_VISION, 2400, 0), 1.0f)
                            .alwaysEdible()
                            .build()
                    )));

    public static final Item TURQUOISE_JAM_SANDWICH = registerItem("turquoise_jam_sandwich",
            new Item(new FabricItemSettings()
                    .maxCount(16) // Ограничение стака до 16
                    .food(new net.minecraft.item.FoodComponent.Builder()
                            .hunger(7)
                            .saturationModifier(0.6f)
                            .statusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                                    net.minecraft.entity.effect.StatusEffects.REGENERATION, 200, 0), 1.0f)
                            .statusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                                    net.minecraft.entity.effect.StatusEffects.NIGHT_VISION, 600, 0), 1.0f)
                            .alwaysEdible()
                            .build()
                    )));

    public static final Item BERRIES_JAM = registerItem("berries_jam",
            new Item(new FabricItemSettings()
                    .maxCount(16)
                    .food(new net.minecraft.item.FoodComponent.Builder()
                            .hunger(7)
                            .saturationModifier(0.6f)
                            .build()
            )));

    public static final Item JAM_SANDWICH = registerItem("jam_sandwich",
            new Item(new FabricItemSettings()
                    .maxCount(16)
                    .food(new net.minecraft.item.FoodComponent.Builder()
                            .hunger(7)
                            .saturationModifier(0.6f)
                            .build()
                    )));

    public static final Item JAM_JAR = registerItem(
            "jam_jar",
            new ScriberToolsItem(new Item.Settings().maxCount(16))
    );

    // Садовый мешок для пересадки
    public static final Item GARDEN_BAG = registerItem("garden_bag",
            new GardenBagItem(new FabricItemSettings().maxCount(1)));

    public static ItemStack createScriberTools() {
        ItemStack stack = new ItemStack(SCRIBER_TOOLS);
        ScriberToolsItem.setUses(stack, 0);
        return stack;
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(
                Registries.ITEM,
                new Identifier(Numenology.MOD_ID, name),
                item
        );
    }

    public static void registerModItems() {
        Numenology.LOGGER.info("Registering items for " + Numenology.MOD_ID);
    }
}