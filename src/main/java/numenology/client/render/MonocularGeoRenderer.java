package numenology.client.render;

import numenology.client.model.MonocularGeoModel;
import numenology.item.custom.MonocularItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MonocularGeoRenderer extends GeoItemRenderer<MonocularItem> {
    public MonocularGeoRenderer() {
        super(new MonocularGeoModel());
    }
}