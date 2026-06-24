package numenology.block.custom;

import com.sun.jdi.Mirror;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.VariantSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import numenology.item.ModItems;
import numenology.item.custom.ScriberToolsItem;
import numenology.research.KnowledgeManager;
import numenology.block.entity.ResearchTableBlockEntity;

public class ResearchTableBlock extends BlockWithEntity {

    public static final EnumProperty<ResearchTableState> STATE =
            EnumProperty.of("state", ResearchTableState.class);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public ResearchTableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState()
                .with(STATE, ResearchTableState.EMPTY)
                .with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(STATE, FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ResearchTableBlockEntity(pos, state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public net.minecraft.block.BlockRenderType getRenderType(BlockState state) {
        return net.minecraft.block.BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        // =========================
        // DEBUG
        // =========================
        if (!world.isClient && player.isSneaking()) {

            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            var data = KnowledgeManager.get(serverPlayer);

            StringBuilder msg = new StringBuilder();
            msg.append("=== RESEARCH DEBUG ===\n");

            msg.append("ACTIVE (SUSPECTED):\n");
            for (var entry : data.getAllSuspected().entrySet()) {
                msg.append("- ").append(entry.getKey()).append("\n");
            }

            player.sendMessage(
                    net.minecraft.text.Text.literal(msg.toString()),
                    false
            );

            return ActionResult.SUCCESS;
        }

        if (world.isClient) return ActionResult.SUCCESS;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof ResearchTableBlockEntity table)) {
            return ActionResult.PASS;
        }

        // =========================
        // 🔥 ДОБАВЛЕНО: ОТКРЫТИЕ GUI (НЕ ЛОМАЕТ ЛОГИКУ)
        // =========================
        if (!player.isSneaking()) {
            player.openHandledScreen(table);
            return ActionResult.SUCCESS;
        }

        ItemStack held = player.getStackInHand(hand);
        ResearchTableState current = state.get(STATE);

        // =========================
        // ВСТАВКА ПРЕДМЕТОВ В СТОЛ
        // =========================

        if (current == ResearchTableState.EMPTY && held.isOf(ModItems.SCRIBER_TOOLS)) {
            table.setStack(2, held.copyWithCount(1));
            held.decrement(1);
            world.setBlockState(pos, state.with(STATE, ResearchTableState.TOOLS));
            return ActionResult.SUCCESS;
        }

        if (current == ResearchTableState.TOOLS && held.isOf(net.minecraft.item.Items.PAPER)) {
            table.setStack(1, held.copyWithCount(1));
            held.decrement(1);
            world.setBlockState(pos, state.with(STATE, ResearchTableState.TOOLS_PAPER));
            return ActionResult.SUCCESS;
        }

        if (current == ResearchTableState.TOOLS_PAPER && held.isOf(ModItems.NUMEN_CODEX)) {
            table.setStack(0, held.copyWithCount(1));
            world.setBlockState(pos, state.with(STATE, ResearchTableState.FULL));
            return ActionResult.SUCCESS;
        }

        // =========================
        // ЗАКРЕПЛЕНИЕ ЗНАНИЯ
        // =========================

        if (!held.isOf(ModItems.NUMEN_CODEX)) {
            return ActionResult.PASS;
        }

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        var data = KnowledgeManager.get(serverPlayer);

        String target = null;

        for (String id : data.getAllSuspected().keySet()) {
            if (data.canComplete(id)) {
                target = id;
                break;
            }
        }

        if (target == null) {
            player.sendMessage(
                    net.minecraft.text.Text.literal("Нет доступных знаний для закрепления"),
                    true
            );
            return ActionResult.SUCCESS;
        }

        ItemStack codex = table.getStack(0);
        ItemStack paper = table.getStack(1);
        ItemStack tools = table.getStack(2);

        if (codex.isEmpty()) {
            player.sendMessage(net.minecraft.text.Text.literal("В столе нет кодекса"), true);
            return ActionResult.SUCCESS;
        }

        if (paper.isEmpty()) {
            player.sendMessage(net.minecraft.text.Text.literal("В столе нет бумаги"), true);
            return ActionResult.SUCCESS;
        }

        if (tools.isEmpty()) {
            player.sendMessage(net.minecraft.text.Text.literal("В столе нет инструментов"), true);
            return ActionResult.SUCCESS;
        }

        if (!ScriberToolsItem.canUse(tools)) {
            player.sendMessage(net.minecraft.text.Text.literal("Инструменты пусты"), true);
            return ActionResult.SUCCESS;
        }

        KnowledgeManager.complete(serverPlayer, target);

        paper.decrement(1);
        ScriberToolsItem.use(tools);

        table.markDirty();

        return ActionResult.SUCCESS;
    }
}