package numenology.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import numenology.block.entity.ResearchTableBlockEntity;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ResearchTableRenderer implements BlockEntityRenderer<ResearchTableBlockEntity> {

    private final BlockRenderManager blockRenderManager;

    public ResearchTableRenderer(BlockEntityRendererFactory.Context ctx) {
        this.blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
    }

    @Override
    public void render(ResearchTableBlockEntity entity, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {

        // 🔥 РИСУЕМ ОБЫЧНУЮ МОДЕЛЬ БЛОКА
        blockRenderManager.renderBlockAsEntity(
                entity.getCachedState(),
                matrices,
                vertexConsumers,
                light,
                overlay
        );
    }
}