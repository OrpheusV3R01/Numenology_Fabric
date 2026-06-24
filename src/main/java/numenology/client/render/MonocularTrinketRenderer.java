package numenology.client.render;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import numenology.item.ModItems;
import numenology.item.custom.MonocularItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;

public class MonocularTrinketRenderer implements TrinketRenderer {
    private static final MonocularGeoRenderer RENDERER = new MonocularGeoRenderer();

    @Override
    public void render(ItemStack stack, SlotReference slotReference, EntityModel<? extends LivingEntity> contextModel,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity,
                       float limbAngle, float limbDistance, float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {

        if (stack.getItem() != ModItems.MONOCULAR) return;

        if (contextModel instanceof BipedEntityModel<?> bipedModel) {
            MonocularItem item = (MonocularItem) stack.getItem();

            matrices.push();

            // 1. Привязываем матрицу к поворотам головы игрока
            bipedModel.head.rotate(matrices);

            // 2. Позиционируем монокуляр (подгоняй координаты Y, Z здесь)
            matrices.translate(0.0F, 1.3F, -0.05F);
            matrices.scale(0.85F, 0.85F, 0.85F);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(180.0F));

            // 3. Получаем запеченную модель GeckoLib, чтобы достать из неё живые кости
            BakedGeoModel bakedModel = RENDERER.getGeoModel().getBakedModel(RENDERER.getGeoModel().getModelResource(item));

            if (bakedModel != null) {
                // Получаем правильный тип слоя рендера
                RenderLayer renderType = RENDERER.getRenderType(
                        item,
                        RENDERER.getTextureLocation(item),
                        vertexConsumers,
                        tickDelta
                );

                // 4. Отрезаем краш: перебираем все верхние кости модели вместо передачи null
                for (GeoBone bone : bakedModel.topLevelBones()) {
                    RENDERER.renderRecursively(
                            matrices,
                            item,
                            bone, // Передаем настоящую кость из модели
                            renderType,
                            vertexConsumers,
                            vertexConsumers.getBuffer(renderType),
                            false,
                            tickDelta,
                            light,
                            OverlayTexture.DEFAULT_UV,
                            1.0F, 1.0F, 1.0F, 1.0F
                    );
                }
            }

            matrices.pop();
        }
    }

    public static void register() {
        TrinketRendererRegistry.registerRenderer(ModItems.MONOCULAR, new MonocularTrinketRenderer());
    }
}