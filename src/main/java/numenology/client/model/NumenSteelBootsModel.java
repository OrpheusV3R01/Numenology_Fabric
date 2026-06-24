package numenology.client.model;

import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.item.custom.NumenSteelBootsItem;
import software.bernie.geckolib.model.GeoModel;

public class NumenSteelBootsModel extends GeoModel<NumenSteelBootsItem> {

    @Override
    public Identifier getModelResource(NumenSteelBootsItem object) {
        return new Identifier(Numenology.MOD_ID,
                "geo/numen_steel_boots.geo.json");
    }

    @Override
    public Identifier getTextureResource(NumenSteelBootsItem object) {
        return new Identifier(Numenology.MOD_ID,
                "textures/armor/numen_steel_boots.png");
    }

    @Override
    public Identifier getAnimationResource(NumenSteelBootsItem animatable) {
        return null;
    }
}