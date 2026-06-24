package numenology.codex;

import java.util.*;

public class CodexManager {

    private static final Map<String, CodexEntry> ENTRIES = new HashMap<>();
    private static final Map<String, CodexCategory> CATEGORIES = new HashMap<>();

    public static void init() {

        // категории
        registerCategory(new CodexCategory("basic", "Основы"));

        // === РЕАЛЬНЫЕ ENTRY ДЛЯ НОД ===

        register(new CodexEntry(
                "entry_resin",
                "basic",
                List.of(
                        "Нумен смола.\n\nТы коснулся неизвестного.",
                        "Она реагирует.\nНо не подчиняется.",
                        "Это начало."
                )
        ));

        register(new CodexEntry(
                "entry_hematite",
                "basic",
                List.of(
                        "Гематит.\n\nМатериал, содержащий силу.",
                        "Он взаимодействует с Нуменом.",
                        "Возможно, его можно использовать..."
                )
        ));

        register(new CodexEntry(
                "entry_crucible",
                "basic",
                List.of(
                        "Тигель.\n\nПервый инструмент алхимии.",
                        "Позволяет управлять материей.",
                        "Теперь ты можешь создавать."
                )
        ));

        register(new CodexEntry(
                "entry_transmutation",
                "basic",
                List.of(
                        "Трансмутация.\n\nМатерия поддаётся.",
                        "Ты начал её понимать.",
                        "Но это только начало."
                )
        ));
    }

    public static void register(CodexEntry entry) {
        ENTRIES.put(entry.getId(), entry);
    }

    public static void registerCategory(CodexCategory category) {
        CATEGORIES.put(category.getId(), category);
    }

    public static CodexEntry getEntry(String id) {
        return ENTRIES.get(id);
    }

    public static List<CodexEntry> getEntriesByCategory(String categoryId) {
        return ENTRIES.values().stream()
                .filter(e -> e.getCategoryId().equals(categoryId))
                .toList();
    }

    public static Collection<CodexCategory> getCategories() {
        return CATEGORIES.values();
    }
}