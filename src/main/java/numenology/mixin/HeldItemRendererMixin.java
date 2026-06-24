package numenology.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

import numenology.item.ModItems;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.util.math.RotationAxis;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    private float smoothProgress = 0.0F;

    @Inject(
            method = "renderFirstPersonItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V",
                    shift = At.Shift.AFTER
            )
    )
    private void numenometerTilt(
            AbstractClientPlayerEntity player,
            float tickDelta,
            float pitch,
            Hand hand,
            float swingProgress,
            ItemStack stack,
            float equipProgress,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            CallbackInfo ci
    ) {
        if (!stack.isOf(ModItems.NUMENOMETER)) return;
        if (!player.isUsingItem()) {
            smoothProgress = 0.0F;
            return;
        }

        float useTime = player.getItemUseTime() + tickDelta;
        float target = Math.min(useTime / 10.0F, 1.0F);

// 🔥 ИНЕРЦИЯ (главное улучшение плавности)
        smoothProgress += (target - smoothProgress) * 0.13F;

// 🔥 финальный progress
        float progress = smoothProgress;


        float side = hand == Hand.MAIN_HAND ? 1F : -1F;

// 👉 лёгкое приближение к глазу
        matrices.translate(
                -0.15F * side * progress, // в центр экрана
                -0.1F * progress,         // вверх
                -0.25F * progress         // к камере
        );

// 👉 твоя система поворотов (оставляем, но через smooth progress)
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(50F * side * progress));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90F * progress));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-10F * progress));

        float time = player.age + tickDelta;
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(
                (float)Math.sin(time * 0.15F) * 0.8F * progress
        ));
    }


}