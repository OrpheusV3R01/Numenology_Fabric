package numenology.research;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import numenology.block.ModBlocks;
import numenology.item.ModItems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeRegistry {

    private static final Map<String, KnowledgeEntry> ENTRIES = new HashMap<>();

    // =========================
    // РЕГИСТРАЦИЯ
    // =========================

    static {

        // 🔹 БАЗА — первый контакт
        register(new KnowledgeEntry(
                "numenology:first_contact_numen",
                List.of()
        ));

        // 🔹 ГЕМАТИТ
        register(new KnowledgeEntry(
                "numenology:hematite_processing",
                List.of("numenology:first_contact_numen")
        ));

        // 🔹 ТИГЕЛЬ
        register(new KnowledgeEntry(
                "numenology:crucible_basics",
                List.of(
                        "numenology:hematite_processing"
                )
        ));

        // 🔹 ТРАНСМУТАЦИЯ (Тир 1)
        register(new KnowledgeEntry(
                "numenology:numen_transmutation_tier1",
                List.of("numenology:crucible_basics")
        ));

// =========================
// 🌿 ВОЛШЕБНАЯ ФЛОРА (НЕЗАВИСИМАЯ)
// =========================
        register(new KnowledgeEntry(
                "numenology:magic_flora",
                List.of() // ❗ без зависимостей
        ));

// =========================
// 🌌 УЗЛЫ И АСПЕКТЫ
// =========================
        register(new KnowledgeEntry(
                "numenology:nodes_and_aspects",
                List.of("numenology:magic_flora")
        ));

// =========================
// ⚡ НЕОБУЗДАННАЯ СИЛА
// =========================
        register(new KnowledgeEntry(
                "numenology:force_unleashed",
                List.of("numenology:nodes_and_aspects")
        ));

// =========================
// 🔥 РАСКАЛЁННАЯ КУЗНЯ
// =========================
        register(new KnowledgeEntry(
                "numenology:heated_forge",
                List.of("numenology:numen_transmutation_tier1")
        ));

// =========================
// 🫙 ЖИДКАЯ СТАЛЬ
// =========================
        register(new KnowledgeEntry(
                "numenology:molten_steel",
                List.of("numenology:heated_forge")
        ));

// =========================
// 🧪 ИНСТРУМЕНТЫ ИССЛЕДОВАНИЯ
// =========================
        register(new KnowledgeEntry(
                "numenology:research_tools",
                List.of("numenology:molten_steel")
        ));

    }




    public static ItemStack getIcon(String id) {
        return switch (id) {
            case "numenology:first_contact_numen" -> new ItemStack(ModItems.NUMEN_CODEX);
            case "numenology:hematite_processing" -> new ItemStack(ModBlocks.HEMATITE_BLOCK);
            case "numenology:magic_flora" -> new ItemStack(ModBlocks.NUMEN_SAPLING);
            case "numenology:nodes_and_aspects" -> new ItemStack(Items.ENDER_EYE);
            case "numenology:force_unleashed" -> new ItemStack(Items.COMPASS);
            case "numenology:heated_forge" -> new ItemStack(ModBlocks.NUMEN_SMELTERY);
            case "numenology:molten_steel" -> new ItemStack(ModItems.NUMEN_STEEL_INGOT);
            case "numenology:research_tools" -> new ItemStack(ModItems.MONOCULAR);
            case "numenology:crucible_basics" -> new ItemStack(ModBlocks.NUMEN_CRUCIBLE);
            case "numenology:numen_transmutation_tier1" -> new ItemStack(ModItems.SLAG);
            default -> new ItemStack(Items.BARRIER);
        };
    }

    // =========================
    // API
    // =========================

    private static void register(KnowledgeEntry entry) {
        ENTRIES.put(entry.id, entry);
    }

    public static KnowledgeEntry get(String id) {
        return ENTRIES.get(id);
    }

    // 🔥 НОВОЕ — для UI
    public static Map<String, KnowledgeEntry> getAll() {
        return ENTRIES;
    }

    // 🔥 НОВОЕ — перевод ID → lang ключ
    public static String getTitleKey(String id) {
        return "research." + id.replace(":", ".") + ".title";
    }

    public static String getDescKey(String id) {
        return "research." + id.replace(":", ".") + ".desc";
    }
}