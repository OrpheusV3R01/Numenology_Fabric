package numenology.client.render;

import numenology.client.model.LamellarChestplateModel;
import numenology.item.custom.LamellarChestplateItem;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class LamellarChestplateRenderer
        extends GeoArmorRenderer<LamellarChestplateItem> {

    public LamellarChestplateRenderer() {
        super(new LamellarChestplateModel());
    }
}