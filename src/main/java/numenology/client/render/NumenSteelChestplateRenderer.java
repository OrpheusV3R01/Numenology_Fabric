package numenology.client.render;

import numenology.client.model.NumenSteelChestplateModel;
import numenology.item.custom.NumenSteelChestplateItem;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NumenSteelChestplateRenderer
        extends GeoArmorRenderer<NumenSteelChestplateItem> {

    public NumenSteelChestplateRenderer() {
        super(new NumenSteelChestplateModel());
    }
}