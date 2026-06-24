package numenology.energy;

import net.minecraft.registry.Registry;
import net.minecraft.registry.SimpleRegistry;
import net.minecraft.util.Identifier;
import numenology.Numenology;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModAspects {
    // Создаем кастомный регистр внутри Fabric
    public static final Map<Identifier, NumenAspect> REGISTRY = new LinkedHashMap<>();

    // Объявляем твои аспект-компоненты
    public static final NumenAspect NUMEN = register("numen", 0x47A1FF, NumenAspect.Charge.NONE);
    public static final NumenAspect UMBRA = register("umbra", 0x1A1A24, NumenAspect.Charge.NEGATIVE);
    public static final NumenAspect LUMEN = register("lumen", 0xFFE57F, NumenAspect.Charge.POSITIVE);

    public static final NumenAspect POTENTIA = register("potentia", 0xFF4500, NumenAspect.Charge.POSITIVE);
    public static final NumenAspect MECHA = register("mecha", 0x4682B4, NumenAspect.Charge.NEGATIVE);
    public static final NumenAspect ANTIQUUS = register("antiquus", 0x7fb7a1, NumenAspect.Charge.NONE);

    public static final NumenAspect VITA = register("vita", 0x32CD32, NumenAspect.Charge.POSITIVE);
    public static final NumenAspect INFUNGUM = register("infungum", 0x7fab90, NumenAspect.Charge.POSITIVE);

    private static NumenAspect register(String id, int color, NumenAspect.Charge charge) {
        Identifier identifier = Identifier.of(Numenology.MOD_ID, id);
        NumenAspect aspect = new NumenAspect(id, color, charge);
        REGISTRY.put(identifier, aspect);
        return aspect;
    }

    // Поиск аспекта по его строковому ID (очень пригодится для NBT чтения!)
    public static NumenAspect getFromString(String id) {
        return REGISTRY.get(Identifier.of(Numenology.MOD_ID, id.toLowerCase()));
    }
}