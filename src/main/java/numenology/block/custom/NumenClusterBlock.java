package numenology.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class NumenClusterBlock extends Block {
    public static final DirectionProperty FACING = FacingBlock.FACING;

    // Хитбоксы адаптированы под твою плоскую широкую модель (высота ~7 пикселей)
    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);    // На полу
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(1.0, 9.0, 1.0, 15.0, 16.0, 15.0); // На потолке
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(1.0, 1.0, 9.0, 15.0, 15.0, 16.0); // На стене (север)
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(1.0, 1.0, 0.0, 15.0, 15.0, 7.0);  // На стене (юг)
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(9.0, 1.0, 1.0, 16.0, 15.0, 15.0);  // На стене (запад)
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 1.0, 1.0, 7.0, 15.0, 15.0);   // На стене (восток)

    public NumenClusterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        switch (direction) {
            case DOWN: return DOWN_SHAPE;
            case NORTH: return NORTH_SHAPE;
            case SOUTH: return SOUTH_SHAPE;
            case WEST: return WEST_SHAPE;
            case EAST: return EAST_SHAPE;
            default: return UP_SHAPE;
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty(); // Игрок проходит насквозь, коллизии нет
    }
}