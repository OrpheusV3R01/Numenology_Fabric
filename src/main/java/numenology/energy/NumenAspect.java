package numenology.energy;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class NumenAspect {
    private final String id;
    private final int color;
    private final Charge charge;

    public NumenAspect(String id, int color, Charge charge) {
        this.id = id;
        this.color = color;
        this.charge = charge;
    }

    public String getId() { return id; }
    public int getColor() { return color; }
    public Charge getCharge() { return charge; }

    // Автоматическое получение локализованного имени (для HUD или описаний)
    public Text getDisplayName() {
        return Text.translatable("aspect.numenology." + id);
    }

    // Перечисление для зарядов (очень удобно для логики ритуалов!)
    public enum Charge {
        NONE,         // Универсальный / Нет заряда
        POSITIVE,     // Передающий / Ускоряющий
        NEGATIVE      // Поглощающий / Стабилизирующий
    }
}