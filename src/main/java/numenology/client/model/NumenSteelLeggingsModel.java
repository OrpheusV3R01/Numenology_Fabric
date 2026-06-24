package numenology.client.model;

import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.item.custom.NumenSteelLeggingsItem;
import software.bernie.geckolib.model.GeoModel;

public class NumenSteelLeggingsModel extends GeoModel<NumenSteelLeggingsItem> {

    @Override
    public Identifier getModelResource(NumenSteelLeggingsItem object) {
        return new Identifier(Numenology.MOD_ID,
                "geo/numen_steel_leggings.geo.json");
    }

    @Override
    public Identifier getTextureResource(NumenSteelLeggingsItem object) {
        return new Identifier(Numenology.MOD_ID,
                "textures/armor/numen_steel_leggings.png");
    }

    @Override
    public Identifier getAnimationResource(NumenSteelLeggingsItem animatable) {
        return null;
    }
}