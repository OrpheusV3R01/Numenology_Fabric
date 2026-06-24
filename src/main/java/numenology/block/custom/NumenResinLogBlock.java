package numenology.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.state.StateManager;

public class NumenResinLogBlock extends PillarBlock {

    public NumenResinLogBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder); // Сохраняем только ванильное направление бревна (AXIS)
    }
}