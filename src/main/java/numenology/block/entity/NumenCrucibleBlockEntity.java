package numenology.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import numenology.block.ModBlockEntities;
import numenology.block.custom.NumenCrucibleBlock;
import numenology.block.custom.CrucibleState;
import numenology.energy.NumenChunkData;
import numenology.energy.NumenEnergyManager;
import numenology.recipe.CrucibleRecipe;
import numenology.recipe.CrucibleRecipeManager;
import numenology.item.ModItems;

public class NumenCrucibleBlockEntity extends BlockEntity {

    private int resinCount = 0;

    private int boilingTime = 0;
    private boolean isBoiling = false;
    private static final int MAX_BOILING_TIME = 100;

    private ItemStack input = ItemStack.EMPTY;
    private int progress = 0;

    // 🔥 НОВОЕ
    private boolean failed = false;

    private String failMessage = null;

    public void setFailMessage(String message) {
        this.failMessage = message;
    }

    public NumenCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NUMEN_CRUCIBLE, pos, state);
    }

    public void addResin() {
        resinCount++;
        markDirty();
    }

    public boolean isFull() {
        return resinCount >= 3;
    }

    public void reset() {
        resinCount = 0;
        boilingTime = 0;
        isBoiling = false;
        progress = 0;
        input = ItemStack.EMPTY;
        failed = false;
        markDirty();
    }

    public void startBoiling() {
        isBoiling = true;
        boilingTime = 0;
        markDirty();
    }

    public boolean isBoiling() {
        return isBoiling;
    }

    public void setInput(ItemStack stack) {
        this.input = stack.copy();
        this.input.setCount(1);
        this.progress = 0;
        this.failed = false;
        markDirty();
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean hasInput() {
        return !input.isEmpty();
    }

    public static void tick(World world, BlockPos pos, BlockState state, NumenCrucibleBlockEntity be) {
        if (world.isClient) return;

        CrucibleState current = state.get(NumenCrucibleBlock.STATE);

        // 🔥 WATER → NUMEN (Процесс кипения воды)
        if (current == CrucibleState.WATER) {
            if (!be.isBoiling) return;

            be.boilingTime++;

            if (be.boilingTime >= MAX_BOILING_TIME) {
                world.setBlockState(pos,
                        state.with(NumenCrucibleBlock.STATE, CrucibleState.NUMEN));

                // 🔊 ЗВУК ПЕРЕХОДА В NUMEN
                world.playSound(null, pos,
                        SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE,
                        SoundCategory.BLOCKS,
                        1.0f, 0.8f);

                be.reset();
            }

            return;
        }

        // 🧪 NUMEN → ПРОЦЕСС ТРАНСМУТАЦИИ
        if (current == CrucibleState.NUMEN) {
            if (!be.hasInput()) return;

            be.progress++;

            // ❗ ШЛАК (Неудачная трансмутация / Отсутствие знаний)
            if (be.failed) {
                if (be.progress >= 100) {
                    boolean hasEnergyToFail = false;

                    // Проверяем штрафную энергию (нужно минимум 20)
                    if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                        net.minecraft.util.math.ChunkPos chunkPos = new net.minecraft.util.math.ChunkPos(pos);
                        NumenChunkData chunkData = NumenEnergyManager.getChunk(serverWorld, chunkPos);

                        if (chunkData != null && chunkData.getEnergy() >= 20) {
                            chunkData.removeEnergy(20);
                            chunkData.addMiasma(5);
                            numenology.block.NumenWorldState.get(serverWorld).markDirty();
                            hasEnergyToFail = true; // Энергии хватило на фейл
                        }
                    }

                    if (hasEnergyToFail) {
                        // Энергии хватило — спавним шлак, пишем ошибку игроку
                        world.spawnEntity(new ItemEntity(
                                world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                                new ItemStack(ModItems.SLAG)
                        ));

                        world.playSound(null, pos,
                                SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                                SoundCategory.BLOCKS,
                                0.8f, 0.6f);

                        if (world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false) instanceof net.minecraft.server.network.ServerPlayerEntity player) {
                            if (be.failMessage != null) {
                                player.sendMessage(net.minecraft.text.Text.literal(be.failMessage), true);
                            }
                        }
                    } else {
                        // ❌ ДАЖЕ НА ШЛАК НЕ ХВАТИЛО ЭНЕРГИИ — просто шипим, всё испаряется
                        world.playSound(null, pos,
                                SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                                SoundCategory.BLOCKS,
                                1.0f, 1.0f);
                    }

                    be.failMessage = null;
                    world.setBlockState(pos, state.with(NumenCrucibleBlock.STATE, CrucibleState.EMPTY));
                    be.reset();
                }

                return;
            }

            // ✅ НОРМ РЕЦЕПТ (УСПЕШНЫЙ КРАФТ)
            CrucibleRecipe recipe = CrucibleRecipeManager.getRecipe(be.input);

            if (recipe == null) {
                be.progress = 0;
                return;
            }

            if (be.progress >= recipe.time) {
                boolean hasEnergyToCraft = false;

                if (world instanceof net.minecraft.server.world.ServerWorld serverWorld) {
                    net.minecraft.util.math.ChunkPos chunkPos = new net.minecraft.util.math.ChunkPos(pos);
                    NumenChunkData chunkData = NumenEnergyManager.getChunk(serverWorld, chunkPos);

                    // Проверяем, есть ли минимум 5 энергии для успешного завершения
                    if (chunkData != null && chunkData.getEnergy() >= 5) {
                        chunkData.removeEnergy(5);
                        if (world.random.nextBoolean()) {
                            chunkData.addMiasma(1);
                        }

                        numenology.block.NumenWorldState.get(serverWorld).markDirty();
                        hasEnergyToCraft = true; // Энергия успешно списана
                    }
                }

                if (hasEnergyToCraft) {
                    // ✅ УСПЕХ: Энергии хватило, спавним результат трансмутации
                    world.spawnEntity(new ItemEntity(
                            world,
                            pos.getX() + 0.5,
                            pos.getY() + 1.0,
                            pos.getZ() + 0.5,
                            recipe.result.copy()
                    ));

                    world.playSound(null, pos,
                            SoundEvents.BLOCK_AMETHYST_BLOCK_RESONATE,
                            SoundCategory.BLOCKS,
                            1.0f, 1.2f);
                } else {
                    // ❌ ФЕЙЛ: Энергии не хватило. Ничего не спавним, просто шипим
                    world.playSound(null, pos,
                            SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE,
                            SoundCategory.BLOCKS,
                            1.0f, 1.0f);
                }

                // В любом случае переводим тигель обратно в EMPTY и сбрасываем состояние
                world.setBlockState(pos,
                        state.with(NumenCrucibleBlock.STATE, CrucibleState.EMPTY));

                be.reset();
            }
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.putInt("ResinCount", resinCount);
        nbt.putInt("BoilingTime", boilingTime);
        nbt.putBoolean("IsBoiling", isBoiling);
        nbt.putInt("Progress", progress);
        nbt.putBoolean("Failed", failed);

        if (!input.isEmpty()) {
            nbt.put("Input", input.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        resinCount = nbt.getInt("ResinCount");
        boilingTime = nbt.getInt("BoilingTime");
        isBoiling = nbt.getBoolean("IsBoiling");
        progress = nbt.getInt("Progress");
        failed = nbt.getBoolean("Failed");

        if (nbt.contains("Input")) {
            input = ItemStack.fromNbt(nbt.getCompound("Input"));
        } else {
            input = ItemStack.EMPTY;
        }
    }
}