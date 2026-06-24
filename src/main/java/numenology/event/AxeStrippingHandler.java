package numenology.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import numenology.block.ModBlocks;
import numenology.item.ModItems;

import net.minecraft.entity.ItemEntity;

public class AxeStrippingHandler {

    public static void register() {

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {

            if (world.isClient) return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);

            if (!(stack.getItem() instanceof AxeItem)) return ActionResult.PASS;

            BlockPos pos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            // 👉 если это бревно
            if (block == ModBlocks.NUMEN_LOG) {

                // меняем на обтёсанное
                world.setBlockState(pos,
                        ModBlocks.STRIPPED_NUMEN_LOG.getDefaultState()
                                .with(net.minecraft.state.property.Properties.AXIS,
                                        state.get(net.minecraft.state.property.Properties.AXIS))
                );

                // шанс дропа
                if (world.getRandom().nextFloat() < 0.5f) {

                    ItemEntity drop = new ItemEntity(
                            world,
                            pos.getX() + 0.5,
                            pos.getY() + 1,
                            pos.getZ() + 0.5,
                            new ItemStack(ModItems.NUMEN_RESIN)
                    );

                    world.spawnEntity(drop);
                }

                return ActionResult.SUCCESS;
            }

            return ActionResult.PASS;
        });
    }
}