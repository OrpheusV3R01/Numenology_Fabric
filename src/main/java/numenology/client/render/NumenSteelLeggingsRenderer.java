package numenology.client.render;

import numenology.client.model.NumenSteelLeggingsModel;
import numenology.item.custom.NumenSteelLeggingsItem;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class NumenSteelLeggingsRenderer
        extends GeoArmorRenderer<NumenSteelLeggingsItem> {

    public NumenSteelLeggingsRenderer() {
        super(new NumenSteelLeggingsModel());
    }
}