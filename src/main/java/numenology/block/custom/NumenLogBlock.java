package numenology.block.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.Direction;

public class NumenLogBlock extends PillarBlock {

    public static final BooleanProperty RESIN = BooleanProperty.of("resin");

    public NumenLogBlock(Settings settings) {
        super(settings);

        // ВАЖНО: сначала stateManager, потом defaultState
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(AXIS, Direction.Axis.Y)
                .with(RESIN, false)
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(AXIS, RESIN);
    }
}