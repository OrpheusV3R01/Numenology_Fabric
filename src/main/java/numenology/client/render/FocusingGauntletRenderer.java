package numenology.client.render;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.client.TrinketRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.client.model.FocusingGauntletModel;
import numenology.item.custom.FocusingGauntletItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class FocusingGauntletRenderer implements TrinketRenderer {

    private static final Identifier TEXTURE =
            new Identifier(Numenology.MOD_ID, "textures/item/focusing_gauntlet.png");

    private static class InnerArmorRenderer extends GeoArmorRenderer<FocusingGauntletItem> {
        public InnerArmorRenderer() {
            super(new FocusingGauntletModel());
        }

        @Override
        public Identifier getTextureLocation(FocusingGauntletItem animatable) {
            return TEXTURE;
        }

        @Override
        public long getInstanceId(FocusingGauntletItem animatable) {
            if (this.currentStack != null) {
                return software.bernie.geckolib.animatable.GeoItem.getId(this.currentStack);
            }
            return super.getInstanceId(animatable);
        }

        public void renderDirect(MatrixStack matrices, BakedGeoModel bakedModel,
                                 VertexConsumerProvider bufferSource, float partialTick, int packedLight,
                                 ItemStack stack, FocusingGauntletItem item, boolean isLeft) {
            this.currentStack = stack;
            this.animatable = item;

            RenderLayer renderLayer = getRenderType(item, TEXTURE, bufferSource, partialTick);
            VertexConsumer buffer = bufferSource.getBuffer(renderLayer);

            // Синхронизируем видимость строго по структуре твоего JSON
            for (GeoBone topLevelBone : bakedModel.topLevelBones()) {
                if (topLevelBone.getName().equals("bipedRightArm")) {
                    setBoneVisibilityRecursively(topLevelBone, !isLeft);
                } else if (topLevelBone.getName().equals("bipedLeftArm")) {
                    setBoneVisibilityRecursively(topLevelBone, isLeft);
                } else {
                    setBoneVisibilityRecursively(topLevelBone, false);
                }
            }

            actuallyRender(matrices, item, bakedModel, renderLayer, bufferSource, buffer,
                    false, partialTick, packedLight, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);

            this.currentStack = null;
            this.animatable = null;
        }

        private void setBoneVisibilityRecursively(GeoBone bone, boolean visible) {
            bone.setHidden(!visible);
            for (GeoBone child : bone.getChildBones()) {
                setBoneVisibilityRecursively(child, visible);
            }
        }
    }

    private static InnerArmorRenderer armorRenderer;

    private static InnerArmorRenderer getGeoRenderer() {
        if (armorRenderer == null) {
            armorRenderer = new InnerArmorRenderer();
        }
        return armorRenderer;
    }

    @Override
    public void render(ItemStack stack,
                       SlotReference slotReference,
                       EntityModel<? extends LivingEntity> contextModel,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light,
                       LivingEntity entity,
                       float limbAngle, float limbDistance,
                       float tickDelta, float animationProgress,
                       float headYaw, float headPitch) {

        if (!(contextModel instanceof BipedEntityModel<?> bipedModel)) return;
        if (!(stack.getItem() instanceof FocusingGauntletItem item)) return;

        InnerArmorRenderer renderer = getGeoRenderer();
        var geoModel = renderer.getGeoModel();

        BakedGeoModel bakedModel;
        try {
            bakedModel = geoModel.getBakedModel(geoModel.getModelResource(item));
        } catch (Exception e) {
            return;
        }

        String slotName = slotReference.inventory().getSlotType().getName();
        boolean isLeft = slotName.contains("left");
        var armPart = isLeft ? bipedModel.leftArm : bipedModel.rightArm;

        matrices.push();

        // Поворачиваем вслед за рукой игрока
        armPart.rotate(matrices);

        // КОРРЕКЦИЯ СМЕЩЕНИЯ (подгоняем модель ровно на кость руки)
        // Если перчатка всё ещё чуть смещена, поправь эти три значения:
        if (isLeft) {
            matrices.translate(0.3f, -0.1f, 0.0f);
        } else {
            matrices.translate(0.3f, -0.1f, 0.0f);
        }

        renderer.renderDirect(matrices, bakedModel, vertexConsumers, tickDelta, light, stack, item, isLeft);

        matrices.pop();
    }
}