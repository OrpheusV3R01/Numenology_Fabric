package numenology.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.client.model.FocusingGauntletModel;
import numenology.item.custom.FocusingGauntletItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Optional;

public class FocusingGauntletFirstPerson {

    private static final Identifier TEXTURE =
            new Identifier(Numenology.MOD_ID, "textures/item/focusing_gauntlet.png");

    // Ручные поправки для первого лица, если перчатка сядет чуть выше/ниже кисти
    private static final float ADJUST_X = -0.045f;
    private static final float ADJUST_Y = 0.0f; // Сдвиг вниз, чтобы сесть на ладонь
    private static final float ADJUST_Z = 0.0f;

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

        public void renderFirstPersonArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                         int light, FocusingGauntletItem item, ItemStack stack, Hand hand) {

            this.animatable = item;
            this.currentStack = stack;

            BakedGeoModel bakedModel = getGeoModel().getBakedModel(getGeoModel().getModelResource(item));
            if (bakedModel == null) return;

            matrices.push();

            RenderLayer renderLayer = getRenderType(item, TEXTURE, vertexConsumers, 0f);
            VertexConsumer buffer = vertexConsumers.getBuffer(renderLayer);

            boolean isLeft = (hand == Hand.OFF_HAND);

            // Настраиваем видимость по именам из твоего JSON
            for (GeoBone topLevelBone : bakedModel.topLevelBones()) {
                if (topLevelBone.getName().equals("bipedRightArm")) {
                    setBoneVisibilityRecursively(topLevelBone, !isLeft);
                } else if (topLevelBone.getName().equals("bipedLeftArm")) {
                    setBoneVisibilityRecursively(topLevelBone, isLeft);
                } else {
                    setBoneVisibilityRecursively(topLevelBone, false);
                }
            }

            // Ищем кость, чтобы убедиться, что геометрия существует
            String targetBoneName = isLeft ? "bipedLeftArm" : "bipedRightArm";
            Optional<GeoBone> armBoneOpt = getGeoModel().getBone(targetBoneName);

            if (armBoneOpt.isPresent()) {
                // Применяем точечные сдвиги для идеальной посадки от 1-го лица
                matrices.translate(ADJUST_X, ADJUST_Y, ADJUST_Z);

                actuallyRender(matrices, item, bakedModel, renderLayer, vertexConsumers, buffer,
                        false, 0f, light, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            }

            matrices.pop();

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

    private static InnerArmorRenderer getRenderer() {
        if (armorRenderer == null) {
            armorRenderer = new InnerArmorRenderer();
        }
        return armorRenderer;
    }

    public static void render(MatrixStack matrices,
                              VertexConsumerProvider vertexConsumers,
                              int light,
                              ItemStack stack,
                              Hand hand) {

        AbstractClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        if (!(stack.getItem() instanceof FocusingGauntletItem item)) return;

        InnerArmorRenderer renderer = getRenderer();

        matrices.push();

        boolean isSlim = player.getModel().equals("slim");
        if (isSlim) {
            matrices.translate(hand == Hand.MAIN_HAND ? -0.03125f : 0.03125f, -0.03125f, 0.0f);
        }

        renderer.renderFirstPersonArm(matrices, vertexConsumers, light, item, stack, hand);

        matrices.pop();
    }
}