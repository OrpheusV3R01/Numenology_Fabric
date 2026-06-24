package numenology.client.model;

import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.item.custom.LamellarChestplateItem;
import software.bernie.geckolib.model.GeoModel;

public class LamellarChestplateModel extends GeoModel<LamellarChestplateItem> {

    @Override
    public Identifier getModelResource(LamellarChestplateItem object) {
        return new Identifier(Numenology.MOD_ID,
                "geo/lamellar_chestplate.geo.json");
    }

    @Override
    public Identifier getTextureResource(LamellarChestplateItem object) {
        return new Identifier(Numenology.MOD_ID,
                "textures/armor/lamellar_chestplate.png");
    }

    @Override
    public Identifier getAnimationResource(LamellarChestplateItem animatable) {
        return null;
    }
}