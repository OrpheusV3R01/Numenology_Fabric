package numenology.client.render;

import numenology.client.model.NumenSteelBootsModel;
import numenology.item.custom.NumenSteelBootsItem;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NumenSteelBootsRenderer
        extends GeoArmorRenderer<NumenSteelBootsItem> {

    public NumenSteelBootsRenderer() {
        super(new NumenSteelBootsModel());
    }
}