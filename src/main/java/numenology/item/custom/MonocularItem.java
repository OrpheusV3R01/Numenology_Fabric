package numenology.item.custom;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import numenology.client.render.MonocularGeoRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MonocularItem extends TrinketItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    // Создаем один общий экземпляр RenderProvider для обоих методов библиотеки
    private final RenderProvider provider = new RenderProvider() {
        private MonocularGeoRenderer renderer;

        @Override
        public BuiltinModelItemRenderer getCustomRenderer() {
            if (this.renderer == null) {
                this.renderer = new MonocularGeoRenderer();
            }
            return this.renderer;
        }
    };

    private final Supplier<Object> renderProvider = () -> this.provider;

    public MonocularItem(Settings settings) {
        super(settings);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Оставляем пустым
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // Первый обязательный метод (требовался в этой ошибке)
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(this.provider);
    }

    // Второй обязательный метод (требовался в предыдущей ошибке)
    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    public static boolean isWearing(net.minecraft.entity.LivingEntity entity) {
        if (entity instanceof net.minecraft.entity.player.PlayerEntity player) {
            if (player.isCreative()) return true;
            return dev.emi.trinkets.api.TrinketsApi.getTrinketComponent(player)
                    .map(trinkets -> trinkets.isEquipped(numenology.item.ModItems.MONOCULAR))
                    .orElse(false);
        }
        return false;
    }

    public static boolean isMagicVisible(net.minecraft.entity.LivingEntity entity) {
        if (entity instanceof net.minecraft.entity.player.PlayerEntity player) {
            if (player.isCreative()) return true;

            boolean wearingMonocular = dev.emi.trinkets.api.TrinketsApi.getTrinketComponent(player)
                    .map(trinkets -> trinkets.isEquipped(numenology.item.ModItems.MONOCULAR))
                    .orElse(false);

            if (wearingMonocular) return true;

            boolean holdingMeter = player.getMainHandStack().isOf(numenology.item.ModItems.NUMENOMETER) ||
                    player.getOffHandStack().isOf(numenology.item.ModItems.NUMENOMETER);

            return holdingMeter;
        }
        return false;
    }

    @Override
    public boolean canEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        return slot.inventory().getSlotType().getGroup().equals("head")
                && slot.inventory().getSlotType().getName().equals("face");
    }
}