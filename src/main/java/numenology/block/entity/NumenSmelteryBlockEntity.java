package numenology.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import numenology.block.ModBlockEntities;
import numenology.block.ModBlocks;
import numenology.block.custom.NumenSmelteryBlock;
import numenology.recipe.NumenSmelteryRecipe;
import numenology.recipe.ModRecipes;
import numenology.research.KnowledgeManager;

public class NumenSmelteryBlockEntity extends BlockEntity implements Inventory, net.minecraft.screen.NamedScreenHandlerFactory {



    public NumenSmelteryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NUMEN_SMELTERY_BE, pos, state);
    }

    public final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(2, ItemStack.EMPTY);

    private int progress = 0;
    private static final int MAX_PROGRESS = 200;



    private final net.minecraft.screen.PropertyDelegate propertyDelegate = new net.minecraft.screen.PropertyDelegate() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> MAX_PROGRESS;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
        }

        @Override
        public int size() {
            return 2;
        }
    };

    // ===== NBT =====
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
    }

    // ===== TICK =====
    public static void tick(World world, BlockPos pos, BlockState state, NumenSmelteryBlockEntity be)
    {

        if (world.isClient) return;

        boolean shouldBeLit =
                isValidMultiblock(world, pos) ||
                        isValidMultiblockRotated(world, pos, 1) ||
                        isValidMultiblockRotated(world, pos, 2) ||
                        isValidMultiblockRotated(world, pos, 3);
        if (shouldBeLit && world instanceof net.minecraft.server.world.ServerWorld serverWorld) {

            for (var player : serverWorld.getPlayers()) {

                double distance = player.squaredDistanceTo(
                        pos.getX(), pos.getY(), pos.getZ()
                );

                if (distance < 64) {

                    var knowledge = KnowledgeManager.get(player);

                    if (knowledge.hasCompleted("numenology:heated_forge")) {

                        KnowledgeManager.trigger(
                                player,
                                "numenology:molten_steel",
                                world.getTime(),
                                72000
                        );
                    }
                }
            }
        }

        if (state.get(NumenSmelteryBlock.LIT) != shouldBeLit) {
            world.setBlockState(pos, state.with(NumenSmelteryBlock.LIT, shouldBeLit), 3);
        }

        NumenSmelteryRecipe recipe = be.getCurrentRecipe(world);

        boolean isUnlocked = true; // пока всегда true

        // 🔥 1. ЗАПРАШИВАЕМ ДАННЫЕ ЭНЕРГИИ ЧАНКА СЕРВЕРА
        boolean hasEnoughEnergy = false;
        if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
            net.minecraft.util.math.ChunkPos chunkPos = new net.minecraft.util.math.ChunkPos(pos);
            numenology.energy.NumenChunkData chunkData = numenology.energy.NumenEnergyManager.getChunk(serverWorld, chunkPos);
            // Разрешаем плавить только если энергии 10 или больше
            if (chunkData != null && chunkData.getEnergy() >= 10) {
                hasEnoughEnergy = true;
            }
        }

        // Добавляем проверку энергии '&& hasEnoughEnergy' в первое условие прогресса
        if (shouldBeLit && recipe != null && isUnlocked && hasEnoughEnergy) {
            be.progress++;

            if (be.progress >= MAX_PROGRESS) {
                if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    net.minecraft.util.math.ChunkPos chunkPos = new net.minecraft.util.math.ChunkPos(pos);
                    numenology.energy.NumenChunkData chunkData = numenology.energy.NumenEnergyManager.getChunk(serverWorld, chunkPos);
                    if (chunkData != null) {
                        chunkData.removeEnergy(10);
                        chunkData.addMiasma(5);
                        numenology.block.NumenWorldState.get(serverWorld).markDirty();
                    }
                }

                be.craftRecipe(world, pos, recipe);
                be.progress = 0;
            }
        } else {
            be.progress = 0;
        }

        // Добавляем проверку энергии '&& hasEnoughEnergy' во второе условие прогресса
        if (shouldBeLit && recipe != null && hasEnoughEnergy) {
            be.progress++;

            if (be.progress >= MAX_PROGRESS) {
                if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    net.minecraft.util.math.ChunkPos chunkPos = new net.minecraft.util.math.ChunkPos(pos);
                    numenology.energy.NumenChunkData chunkData = numenology.energy.NumenEnergyManager.getChunk(serverWorld, chunkPos);
                    if (chunkData != null) {
                        chunkData.removeEnergy(10);
                        chunkData.addMiasma(5);
                        numenology.block.NumenWorldState.get(serverWorld).markDirty();
                    }
                }

                be.craftRecipe(world, pos, recipe);
                be.progress = 0;
            }
        } else {
            be.progress = 0;
        }
    }

    // ===== РЕЦЕПТ =====
    private NumenSmelteryRecipe getCurrentRecipe(World world) {

        SimpleInventory inv = new SimpleInventory(2);
        inv.setStack(0, this.inventory.get(0));
        inv.setStack(1, this.inventory.get(1));

        return world.getRecipeManager()
                .getFirstMatch(ModRecipes.NUMEN_SMELTERY_TYPE, inv, world)
                .orElse(null);
    }

    private void craftRecipe(World world, BlockPos pos, NumenSmelteryRecipe recipe) {

        this.inventory.get(0).decrement(recipe.getCountA());
        this.inventory.get(1).decrement(recipe.getCountB());

        ItemStack result = recipe.getOutput(null).copy();
        dropStack(world, pos, result);
    }

    // ===== DROP =====
    private static void dropStack(World world, BlockPos origin, ItemStack stack) {

        for (int rot = 0; rot < 4; rot++) {

            BlockPos grillPos = rotate(origin, 1, 0, 0, rot);

            if (world.getBlockState(grillPos).isOf(Blocks.IRON_BARS)) {

                net.minecraft.util.math.Direction outDir = null;

                for (net.minecraft.util.math.Direction dir : net.minecraft.util.math.Direction.Type.HORIZONTAL) {
                    if (world.isAir(grillPos.offset(dir))) {
                        outDir = dir;
                        break;
                    }
                }

                if (outDir == null) {
                    outDir = net.minecraft.util.math.Direction.NORTH;
                }

                double x = grillPos.getX() + 0.5 + outDir.getOffsetX() * 0.6;
                double y = grillPos.getY() + 0.5;
                double z = grillPos.getZ() + 0.5 + outDir.getOffsetZ() * 0.6;

                var entity = new net.minecraft.entity.ItemEntity(world, x, y, z, stack);
                entity.setVelocity(outDir.getOffsetX() * 0.15, 0.0, outDir.getOffsetZ() * 0.15);

                world.spawnEntity(entity);
                return;
            }
        }
    }

    // ===== ROTATION =====
    private static BlockPos rotate(BlockPos origin, int x, int y, int z, int rot) {
        return switch (rot) {
            case 1 -> origin.add(z, y, x);
            case 2 -> origin.add(-x, y, z);
            case 3 -> origin.add(-z, y, -x);
            default -> origin.add(x, y, -z);
        };
    }

    // ===== СТРУКТУРА =====
    private static boolean isValidMultiblockRotated(World world, BlockPos origin, int rotation) {

        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (!world.getBlockState(rotate(origin, x, -1, z, rotation)).isOf(ModBlocks.NUMEN_BRICKS)) {
                    return false;
                }
            }
        }

        for (int x = 0; x < 3; x++) {
            if (!world.getBlockState(rotate(origin, x, 0, 2, rotation)).isOf(ModBlocks.NUMEN_BRICKS)) {
                return false;
            }
        }

        if (!world.getBlockState(rotate(origin, 1, 0, 1, rotation)).isOf(Blocks.LAVA)) return false;
        if (!world.getBlockState(rotate(origin, 1, 0, 0, rotation)).isOf(Blocks.IRON_BARS)) return false;

        for (int x = 0; x < 3; x++) {
            if (!world.getBlockState(rotate(origin, x, 1, 2, rotation)).isOf(ModBlocks.NUMEN_BRICK_STAIRS)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidMultiblock(World world, BlockPos origin) {
        return isValidMultiblockRotated(world, origin, 0);
    }



    public net.minecraft.screen.PropertyDelegate getPropertyDelegate() {
        return propertyDelegate;
    }

    @Override
    public Text getDisplayName() {
        // Явно указываем ключ. Minecraft обязан искать его в твоем ru_ru.json
        return Text.translatable("block.numenology.numen_smeltery");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public net.minecraft.screen.ScreenHandler createMenu(int syncId, net.minecraft.entity.player.PlayerInventory playerInventory, net.minecraft.entity.player.PlayerEntity player) {
        return new numenology.screen.NumenSmelteryScreenHandler(syncId, playerInventory, this);
    }

    // ===== INVENTORY =====
    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return Inventories.splitStack(inventory, slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) {
        return true;
    }
}