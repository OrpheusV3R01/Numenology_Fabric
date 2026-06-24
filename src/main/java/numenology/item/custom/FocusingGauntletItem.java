package numenology.item.custom;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FocusingGauntletItem extends TrinketItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private Object renderProvider = GeoItem.makeRenderer(this);

    public FocusingGauntletItem(Settings settings) {
        super(settings);
    }

    // ── GeoItem: рендер-провайдер ─────────────────────────────────────────────

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {
            // Пустой провайдер — рендер идёт через TrinketRenderer (FocusingGauntletRenderer)
            // и FocusingGauntletFirstPerson напрямую, минуя стандартный GeoItem pipeline
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return () -> this.renderProvider;
    }

    // ── GeoItem: анимации ─────────────────────────────────────────────────────

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        // Анимаций нет
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    // ── Trinkets: бафф атаки пока предмет надет ──────────────────────────────

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(
            ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {

        var modifiers = super.getModifiers(stack, slot, entity, uuid);

        modifiers.put(
                EntityAttributes.GENERIC_ATTACK_DAMAGE,
                new EntityAttributeModifier(
                        uuid,
                        "numenology:gauntlet_damage",
                        2.0,
                        EntityAttributeModifier.Operation.ADDITION
                )
        );

        return modifiers;
    }
}