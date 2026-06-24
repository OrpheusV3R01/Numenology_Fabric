package numenology.nodes.render;

import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

import org.joml.Matrix4f;

import numenology.nodes.NumenNodeBlockEntity;

public class NumenNodeRenderer implements BlockEntityRenderer<NumenNodeBlockEntity> {

    private static final Identifier TEXTURE =
            new Identifier("numenology", "textures/block/numen_node.png");

    public NumenNodeRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(
            NumenNodeBlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay
    ) {
        matrices.push();

        // центр блока
        matrices.translate(0.5, 1.2, 0.5);

        float time = (entity.getWorld().getTime() + tickDelta);

        VertexConsumer buffer = vertexConsumers.getBuffer(
                RenderLayer.getEntityTranslucent(TEXTURE)
        );

        // 3 слоя (псевдо-сфера)
        renderLayer(matrices, buffer, light, time, 0);
        renderLayer(matrices, buffer, light, time, 60);
        renderLayer(matrices, buffer, light, time, 120);

        matrices.pop();
    }

    private void renderLayer(MatrixStack matrices, VertexConsumer buffer, int light, float time, float offset) {
        matrices.push();

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(time + offset));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45));

        matrices.scale(0.6f, 0.6f, 0.6f);

        Matrix4f matrix = matrices.peek().getPositionMatrix();

        // квад (плоскость)
        buffer.vertex(matrix, -1, 0, -1).texture(0, 0).light(light).next();
        buffer.vertex(matrix,  1, 0, -1).texture(1, 0).light(light).next();
        buffer.vertex(matrix,  1, 0,  1).texture(1, 1).light(light).next();
        buffer.vertex(matrix, -1, 0,  1).texture(0, 1).light(light).next();

        matrices.pop();
    }
}