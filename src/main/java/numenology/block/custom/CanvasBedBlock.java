package numenology.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.enums.BedPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class CanvasBedBlock extends BedBlock {

    // Хитбокс для ног (FOOT): плоская основа высотой в 1 пиксель
    protected static final VoxelShape FOOT_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

    // Хитбоксы подушки (высота 4 пикселя, толщина 8 пикселей сверху)
    protected static final VoxelShape HEAD_NORTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
            Block.createCuboidShape(0.0, 1.0, 0.0, 16.0, 4.0, 8.0)
    );

    protected static final VoxelShape HEAD_SOUTH_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
            Block.createCuboidShape(0.0, 1.0, 8.0, 16.0, 4.0, 16.0)
    );

    protected static final VoxelShape HEAD_WEST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
            Block.createCuboidShape(0.0, 1.0, 0.0, 8.0, 4.0, 16.0)
    );

    protected static final VoxelShape HEAD_EAST_SHAPE = VoxelShapes.union(
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0),
            Block.createCuboidShape(8.0, 1.0, 0.0, 16.0, 4.0, 16.0)
    );

    public CanvasBedBlock(Settings settings) {
        super(DyeColor.WHITE, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(PART, BedPart.FOOT).with(OCCUPIED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (state.get(PART) == BedPart.FOOT) {
            return FOOT_SHAPE;
        }
        return switch (state.get(FACING)) {
            case NORTH -> HEAD_NORTH_SHAPE;
            case SOUTH -> HEAD_SOUTH_SHAPE;
            case WEST -> HEAD_WEST_SHAPE;
            default -> HEAD_EAST_SHAPE;
        };
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        if (state.get(PART) != BedPart.HEAD) {
            BlockPos headPos = pos.offset(state.get(FACING));
            BlockState headState = world.getBlockState(headPos);
            if (headState.isOf(this)) {
                pos = headPos;
                state = headState;
            } else {
                return ActionResult.CONSUME;
            }
        }

        if (!world.getDimension().bedWorks()) {
            world.removeBlock(pos, false);
            BlockPos footPos = pos.offset(state.get(FACING).getOpposite());
            if (world.getBlockState(footPos).isOf(this)) {
                world.removeBlock(footPos, false);
            }
            world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5.0F, World.ExplosionSourceType.BLOCK);
            return ActionResult.SUCCESS;
        }

        if (state.get(OCCUPIED)) {
            player.sendMessage(net.minecraft.text.Text.translatable("block.minecraft.bed.occupied"), true);
            return ActionResult.SUCCESS;
        }

        // Запускаем стандартный, стабильный ванильный цикл сна (время проматывается на 100%)
        player.trySleep(pos).ifLeft(reason -> {
            if (reason != null && reason.getMessage() != null) {
                player.sendMessage(reason.getMessage(), true);
            }
        });

        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BedPart bedPart = state.get(PART);
        BlockPos otherPartPos = pos.offset(bedPart == BedPart.FOOT ? state.get(FACING) : state.get(FACING).getOpposite());
        BlockState otherPartState = world.getBlockState(otherPartPos);

        if (otherPartState.isOf(this) && otherPartState.get(PART) != bedPart) {
            world.setBlockState(otherPartPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, 2001, otherPartPos, Block.getRawIdFromState(otherPartState));
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}