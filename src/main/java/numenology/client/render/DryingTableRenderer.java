package numenology.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;

import numenology.block.entity.DryingTableBlockEntity;

public class DryingTableRenderer implements BlockEntityRenderer<DryingTableBlockEntity> {

    public DryingTableRenderer(BlockEntityRendererFactory.Context context) {
        // контекст нужен для некоторых рендереров, здесь пока не используем
    }

    @Override
    public void render(DryingTableBlockEntity blockEntity,
                       float tickDelta,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       int overlay) {

        // 1. Сначала пытаемся взять предмет из входного слота (0)
        ItemStack stack = blockEntity.getStack(0);

        // 2. Если вход пуст, проверяем выходной слот (1)
        if (stack.isEmpty()) {
            stack = blockEntity.getStack(1);
        }

        // 3. Если оба слота абсолютно пусты — прекращаем рендер, на столе ничего нет
        if (stack.isEmpty()) return;

        // Дальше идёт твой неизменённый код рендеринга...
        matrices.push();
        matrices.translate(0.5, 1.0, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        matrices.scale(0.4f, 0.4f, 0.4f);

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack,
                ModelTransformationMode.FIXED,
                light,
                overlay,
                matrices,
                vertexConsumers,
                blockEntity.getWorld(),
                0
        );

        matrices.pop();
    }
}