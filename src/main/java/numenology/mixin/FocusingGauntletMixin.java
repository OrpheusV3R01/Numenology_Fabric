package numenology.mixin;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import numenology.item.ModItems;
import numenology.client.render.FocusingGauntletFirstPerson;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class FocusingGauntletMixin {

    @Inject(method = "renderRightArm", at = @At("TAIL"))
    private void injectRightGauntlet(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
        TrinketsApi.getTrinketComponent(player).ifPresent(component -> {
            component.getAllEquipped().stream()
                    .filter(pair -> pair.getRight().isOf(ModItems.FOCUSING_GAUNTLET))
                    .findFirst()
                    .ifPresent(pair -> {
                        ItemStack gauntletStack = pair.getRight();
                        // Рендерим правую перчатку от первого лица
                        FocusingGauntletFirstPerson.render(matrices, vertexConsumers, light, gauntletStack, Hand.MAIN_HAND);
                    });
        });
    }

    @Inject(method = "renderLeftArm", at = @At("TAIL"))
    private void injectLeftGauntlet(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, CallbackInfo ci) {
        TrinketsApi.getTrinketComponent(player).ifPresent(component -> {
            component.getAllEquipped().stream()
                    .filter(pair -> pair.getRight().isOf(ModItems.FOCUSING_GAUNTLET))
                    .findFirst()
                    .ifPresent(pair -> {
                        ItemStack gauntletStack = pair.getRight();
                        // Рендерим левую перчатку от первого лица
                        FocusingGauntletFirstPerson.render(matrices, vertexConsumers, light, gauntletStack, Hand.OFF_HAND);
                    });
        });
    }
}