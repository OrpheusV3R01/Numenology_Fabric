package numenology.block.custom;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.item.ItemPlacementContext;

import numenology.block.ModBlockEntities;
import numenology.block.entity.NumenCrucibleBlockEntity;
import numenology.item.ModItems;
import numenology.recipe.CrucibleRecipeManager;

public class NumenCrucibleBlock extends BlockWithEntity {

    public static final EnumProperty<CrucibleState> STATE =
            EnumProperty.of("state", CrucibleState.class);

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public NumenCrucibleBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState()
                .with(STATE, CrucibleState.EMPTY)
                .with(FACING, Direction.NORTH));
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;

        if (state.get(STATE) == CrucibleState.WATER && isHeated(world, pos)) {

            if (random.nextFloat() < 0.6f) {
                world.addParticle(ParticleTypes.BUBBLE_POP,
                        x + (random.nextDouble() - 0.5) * 0.4,
                        y,
                        z + (random.nextDouble() - 0.5) * 0.4,
                        0, 0.02, 0);
            }

            if (random.nextFloat() < 0.2f) {
                world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        x + (random.nextDouble() - 0.5) * 0.3,
                        y + 0.2,
                        z + (random.nextDouble() - 0.5) * 0.3,
                        0, 0.03, 0);
            }
        }

        if (state.get(STATE) == CrucibleState.NUMEN) {

            if (random.nextFloat() < 0.4f) {
                world.addParticle(ParticleTypes.END_ROD,
                        x + (random.nextDouble() - 0.5) * 0.3,
                        y,
                        z + (random.nextDouble() - 0.5) * 0.3,
                        0, 0.02, 0);
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STATE, FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NumenCrucibleBlockEntity(pos, state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    private boolean isHeated(World world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());

        return below.isOf(Blocks.FIRE)
                || below.isOf(Blocks.SOUL_FIRE)
                || below.isOf(Blocks.CAMPFIRE)
                || below.isOf(Blocks.SOUL_CAMPFIRE);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, ModBlockEntities.NUMEN_CRUCIBLE, NumenCrucibleBlockEntity::tick);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {

        ItemStack stack = player.getStackInHand(hand);
        CrucibleState current = state.get(STATE);

        // EMPTY → WATER
        if (current == CrucibleState.EMPTY && stack.isOf(Items.WATER_BUCKET)) {

            if (!world.isClient) {
                world.setBlockState(pos, state.with(STATE, CrucibleState.WATER));

                world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY,
                        SoundCategory.BLOCKS, 1.0f, 1.0f);

                if (!player.getAbilities().creativeMode) {
                    player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                }
            }

            return ActionResult.SUCCESS;
        }

        // WATER → смола
        if (current == CrucibleState.WATER && stack.isOf(ModItems.NUMEN_RESIN)) {

            if (!world.isClient) {

                if (!isHeated(world, pos)) return ActionResult.PASS;

                BlockEntity be = world.getBlockEntity(pos);

                if (be instanceof NumenCrucibleBlockEntity crucible) {

                    crucible.addResin();

                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }

                    if (crucible.isFull() && !crucible.isBoiling()) {
                        crucible.startBoiling();
                    }
                }
            }

            return ActionResult.SUCCESS;
        }

        // 💥 NUMEN → ВХОД
        if (current == CrucibleState.NUMEN && !stack.isEmpty()) {

            if (!world.isClient) {

                BlockEntity be = world.getBlockEntity(pos);

                if (be instanceof NumenCrucibleBlockEntity crucible && !crucible.hasInput()) {

                    var recipe = CrucibleRecipeManager.getRecipe(stack);

                    boolean hasKnowledge = false;

                    if (player instanceof net.minecraft.server.network.ServerPlayerEntity serverPlayer) {

                        // 🔥 ТРИГГЕР ПЕРВОЙ ТРАНСМУТАЦИИ
                        numenology.research.KnowledgeManager.trigger(
                                serverPlayer,
                                "numenology:numen_transmutation_tier1",
                                world.getTime(),
                                72000
                        );

                        // 🔥 ПРОВЕРКА ЗНАНИЯ ИЗ РЕЦЕПТА
                        if (recipe != null) {
                            hasKnowledge = numenology.research.KnowledgeManager
                                    .get(serverPlayer)
                                    .hasCompleted(recipe.requiredKnowledge);
                        }
                    }

                    // ❗ НЕТ ЗНАНИЯ ИЛИ НЕТ РЕЦЕПТА → ШЛАК
                    if (!hasKnowledge || recipe == null) {

                        crucible.setInput(stack);
                        crucible.setFailed(true);

                        crucible.setFailMessage(
                                !hasKnowledge
                                        ? "Материя ведёт себя нестабильно... Я явно чего-то не понимаю."
                                        : "Кажется, это не работает... стоит лучше изучить процесс."
                        );

                        if (!player.getAbilities().creativeMode) {
                            stack.decrement(1);
                        }

                        return ActionResult.SUCCESS;
                    }

                    // ✅ НОРМАЛЬНЫЙ КРАФТ
                    crucible.setInput(stack);

                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                }
            }

            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
