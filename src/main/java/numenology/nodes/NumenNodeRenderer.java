package numenology.nodes;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import numenology.item.ModItems;
import numenology.item.custom.MonocularItem;
import net.minecraft.particle.DustParticleEffect;
import org.joml.Vector3f;

public class NumenNodeRenderer implements BlockEntityRenderer<NumenNodeBlockEntity> {

    public NumenNodeRenderer(BlockEntityRendererFactory.Context ctx) {}

    @Override
    public void render(NumenNodeBlockEntity entity, float tickDelta,
                       MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {

        var player = MinecraftClient.getInstance().player;

        if (player == null || !MonocularItem.isMagicVisible(player)) {
            return;
        }

        if (entity.getWorld() == null) return;

        ItemStack stack = new ItemStack(ModItems.NUMEN_NODE_CORE);

        float time = entity.getWorld().getTime() + tickDelta;

        // ==============================
        // FLOATING / HOVER
        // ==============================

        float hover =
                MathHelper.sin(time * 0.05f) * 0.05f;

        // ==============================
        // PULSE
        // ==============================

        float pulse =
                1.0f + MathHelper.sin(time * 0.08f) * 0.04f;

        // ==============================
        // MAIN CORE PASS
        // ==============================

        matrices.push();

        matrices.translate(0.5, 0.5 + hover, 0.5);

        matrices.multiply(
                RotationAxis.POSITIVE_Y.rotationDegrees(time * 1.2f)
        );

        matrices.multiply(
                RotationAxis.POSITIVE_X.rotationDegrees(time * 0.6f)
        );

        matrices.scale(
                0.75f * pulse,
                0.75f * pulse,
                0.75f * pulse
        );

        MinecraftClient.getInstance().getItemRenderer().renderItem(
                stack,
                ModelTransformationMode.GROUND,
                light,
                overlay,
                matrices,
                vertexConsumers,
                entity.getWorld(),
                0
        );

        // ==============================
        // PARTICLES
        // ==============================


        if (entity.getWorld().random.nextFloat() < 0.015f) {

            double px =
                    entity.getPos().getX() + 0.5 +
                            (entity.getWorld().random.nextDouble() - 0.5) * 0.4;

            double py =
                    entity.getPos().getY() + 0.5 +
                            (entity.getWorld().random.nextDouble() - 0.5) * 0.4;

            double pz =
                    entity.getPos().getZ() + 0.5 +
                            (entity.getWorld().random.nextDouble() - 0.5) * 0.4;

            entity.getWorld().addParticle(
                    new DustParticleEffect(
                            new Vector3f(
                                    0.75f,
                                    0.92f,
                                    1.0f
                            ),
                            0.5f
                    ),
                    px,
                    py,
                    pz,
                    0.0,
                    0.002,
                    0.0
            );
        }

        // ==============================
// AMBIENT SOUND
// ==============================

        if (entity.getWorld().getTime() % 160 == 0) {

            double distance = player.squaredDistanceTo(
                    entity.getPos().getX() + 0.5,
                    entity.getPos().getY() + 0.5,
                    entity.getPos().getZ() + 0.5
            );

            if (distance < 100) {

                entity.getWorld().playSound(
                        player,
                        entity.getPos(),
                        SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
                        SoundCategory.BLOCKS,
                        0.12f,
                        0.45f + entity.getWorld().random.nextFloat() * 0.15f
                );
            }
        }

        matrices.pop();

    }
}