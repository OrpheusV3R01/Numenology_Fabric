package numenology.client.render;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import numenology.item.ModItems;

public class DuffelBagRenderer implements TrinketRenderer {

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {

        if (stack.getItem() != ModItems.DUFFEL_BAG) return;

        if (contextModel instanceof BipedEntityModel<?> bipedModel) {
            matrices.push();

            // 1. Привязка к движению тела
            bipedModel.body.rotate(matrices);

            // 2. Масштаб под Blockbench-модель
            float scale = 0.5f;
            matrices.scale(scale, scale, scale);

            // 3. Смещение на спину (настроено с учетом твоих правок 0.2f, 0.4f, 0.7f)
            matrices.translate(0.2f, 1.3f, 0.8f);

            // 4. Повороты матрицы, чтобы рюкзак не висел вверх ногами
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            // 5. Получаем запеченную 3D модель из менеджера
            BakedModel bakedModel = MinecraftClient.getInstance().getBakedModelManager().getModel(
                    new Identifier("numenology", "item/duffel_bag_baked")
            );

            // 6. Рендерим через ПРАВИЛЬНЫЙ метод, который принимает BakedModel последним аргументом!
            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    stack,                        // ItemStack
                    ModelTransformationMode.NONE, // ModelTransformationMode
                    false,                        // boolean (leftHand)
                    matrices,                     // MatrixStack
                    vertexConsumers,              // VertexConsumerProvider
                    light,                        // int (light)
                    OverlayTexture.DEFAULT_UV,    // int (overlay)
                    bakedModel                    // BakedModel (Вот сюда передается наша 3D геометрия!)
            );

            matrices.pop();
        }
    }

    public static void register() {
        TrinketRendererRegistry.registerRenderer(ModItems.DUFFEL_BAG, new DuffelBagRenderer());
    }
}