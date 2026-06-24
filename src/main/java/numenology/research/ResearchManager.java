package numenology.research;

import java.util.HashMap;
import java.util.Map;

public class ResearchManager {

    private static final Map<String, ResearchState> playerResearch = new HashMap<>();

    // 🔥 ИНИЦИАЛИЗАЦИЯ (вызвать 1 раз при запуске мода)
    public static void init() {
        setState("resin", ResearchState.COMPLETED);
    }

    public static ResearchState getState(String id) {
        return playerResearch.getOrDefault(id, ResearchState.NONE);
    }

    public static void setState(String id, ResearchState state) {
        playerResearch.put(id, state);
    }

    // 🔥 ГЛАВНАЯ ЛОГИКА
    public static boolean completeResearch(String id) {

        System.out.println("TRY COMPLETE: " + id);

        if (getState(id) != ResearchState.DISCOVERED) {
            System.out.println("FAILED: not DISCOVERED");
            return false;
        }

        // делаем completed
        setState(id, ResearchState.COMPLETED);

        // 🔥 ПРОГРЕССИЯ
        switch (id) {
            case "resin" -> setState("hematite", ResearchState.DISCOVERED);
            case "hematite" -> setState("crucible", ResearchState.DISCOVERED);
            case "crucible" -> setState("transmutation", ResearchState.DISCOVERED);
        }

        System.out.println("SUCCESS: " + id + " -> COMPLETED");

        return true;
    }
}