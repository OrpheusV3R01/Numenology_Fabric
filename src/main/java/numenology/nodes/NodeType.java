package numenology.nodes;

public enum NodeType {
    NORMAL("normal", true, 1.0f),         // Обычный узел
    PURE("pure", true, 1.5f),             // Чистый узел (только чистый Нумен)
    FADING("fading", false, 0.0f),        // Увядающий узел (теряет энергию)
    HUNGRY("hungry", false, 2.0f),        // Голодный узел (затягивает предметы/блоки)
    COLLAPSING("collapsing", false, 3.0f),// Коллапсирующий узел (высокий риск взрыва)
    INFECTED("infected", false, 0.8f),    // Заражённый узел (распространяет миазмы)
    STABILIZED("stabilized", true, 2.0f); // Стабилизированный узел (искусственный)

    private final String id;
    private final boolean isStable;
    private final float rgenSpeed; // Множитель скорости регенерации

    NodeType(String id, boolean isStable, float rgenSpeed) {
        this.id = id;
        this.isStable = isStable;
        this.rgenSpeed = rgenSpeed;
    }

    public String getId() { return id; }
    public boolean isStable() { return isStable; }
    public float getRgenSpeed() { return rgenSpeed; }
}