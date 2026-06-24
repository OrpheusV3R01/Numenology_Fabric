package numenology.client.model;

import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.item.custom.FocusingGauntletItem;
import software.bernie.geckolib.model.GeoModel;

public class FocusingGauntletModel extends GeoModel<FocusingGauntletItem> {

	// Путь к .geo.json файлу:
	// assets/numenology/geo/focusing_gauntlet.geo.json
	@Override
	public Identifier getModelResource(FocusingGauntletItem animatable) {
		return new Identifier(Numenology.MOD_ID, "geo/focusing_gauntlet.geo.json");
	}

	// Путь к текстуре:
	// assets/numenology/textures/item/focusing_gauntlet.png
	@Override
	public Identifier getTextureResource(FocusingGauntletItem animatable) {
		return new Identifier(Numenology.MOD_ID, "textures/item/focusing_gauntlet.png");
	}

	// Анимаций нет — возвращаем пустой путь-заглушку.
	// GeckoLib просто не найдёт файл и не будет воспроизводить анимации.
	@Override
	public Identifier getAnimationResource(FocusingGauntletItem animatable) {
		return new Identifier(Numenology.MOD_ID, "animations/focusing_gauntlet.animation.json");
	}
}