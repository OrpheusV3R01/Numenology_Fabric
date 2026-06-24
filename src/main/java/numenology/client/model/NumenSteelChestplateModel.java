package numenology.client.model;

import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.item.custom.NumenSteelChestplateItem;
import software.bernie.geckolib.model.GeoModel;

public class NumenSteelChestplateModel extends GeoModel<NumenSteelChestplateItem> {

    @Override
    public Identifier getModelResource(NumenSteelChestplateItem object) {
        return new Identifier(Numenology.MOD_ID,
                "geo/numen_steel_chestplate.geo.json");
    }

    @Override
    public Identifier getTextureResource(NumenSteelChestplateItem object) {
        return new Identifier(Numenology.MOD_ID,
                "textures/armor/numen_steel_chestplate.png");
    }

    @Override
    public Identifier getAnimationResource(NumenSteelChestplateItem animatable) {
        return null;
    }
}