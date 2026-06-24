package numenology.client.model;

import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.item.custom.MonocularItem;
import software.bernie.geckolib.model.GeoModel;

public class MonocularGeoModel extends GeoModel<MonocularItem> {

    @Override
    public Identifier getModelResource(MonocularItem animatable) {
        return new Identifier(Numenology.MOD_ID, "geo/monocular.geo.json");
    }

    @Override
    public Identifier getTextureResource(MonocularItem animatable) {
        return new Identifier(Numenology.MOD_ID, "textures/item/monocular.png");
    }

    @Override
    public Identifier getAnimationResource(MonocularItem animatable) {
        return null; // Если анимаций нет
    }
}