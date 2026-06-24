package numenology.item.custom;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;

import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;

import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import software.bernie.geckolib.util.GeckoLibUtil;

import software.bernie.geckolib.renderer.GeoArmorRenderer;

import numenology.client.render.NumenSteelBootsRenderer;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class NumenSteelBootsItem extends ArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache =
            GeckoLibUtil.createInstanceCache(this);

    private final Supplier<Object> renderProvider =
            GeoItem.makeRenderer(this);

    public NumenSteelBootsItem(
            ArmorMaterial material,
            Type type,
            Settings settings
    ) {
        super(material, type, settings);
    }

    @Override
    public void registerControllers(
            AnimatableManager.ControllerRegistrar controllers
    ) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {

        consumer.accept(new RenderProvider() {

            private NumenSteelBootsRenderer renderer;

            public NumenSteelBootsRenderer getHumanoidArmorModel(
                    LivingEntity livingEntity,
                    ItemStack itemStack,
                    EquipmentSlot equipmentSlot,
                    BipedEntityModel<LivingEntity> original
            ) {

                if (this.renderer == null)
                    this.renderer = new NumenSteelBootsRenderer();

                this.renderer.prepForRender(
                        livingEntity,
                        itemStack,
                        equipmentSlot,
                        original
                );

                return renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return renderProvider;
    }
}