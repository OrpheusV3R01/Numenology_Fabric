package numenology.block.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import numenology.block.ModBlockEntities;
import numenology.recipe.DryingTableRecipe;
import numenology.recipe.ModRecipes;
import org.jetbrains.annotations.Nullable;

public class DryingTableBlockEntity extends BlockEntity implements Inventory, NamedScreenHandlerFactory {

    public final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(2, ItemStack.EMPTY);
    // слот 0 — вход, слот 1 — выход

    private int progress = 0;
    private int maxProgress = 0;
    private int idleTime = 0; // счётчик времени без рецепта

    // 8 минут = 9600 тиков — предмет превращается в пепел
    private static final int BURN_TO_ASH_TIME = 9600;

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> progress;
                case 1 -> maxProgress;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            if (index == 0) progress = value;
            if (index == 1) maxProgress = value;
        }

        @Override
        public int size() { return 2; }
    };

    public DryingTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DRYING_TABLE_BE, pos, state);
    }

    // ===== NBT =====
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt("Progress", progress);
        nbt.putInt("MaxProgress", maxProgress);
        nbt.putInt("IdleTime", idleTime);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.inventory.clear(); // 🌟 ФИКС: Очищаем клиентский инвентарь перед чтением новых данных

        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt("Progress");
        maxProgress = nbt.getInt("MaxProgress");
        idleTime = nbt.getInt("IdleTime");
    }

    // ===== TICK =====
    public static void tick(World world, BlockPos pos, BlockState state, DryingTableBlockEntity be) {
        if (world.isClient) return;

        ItemStack inputStack = be.inventory.get(0);

        // Слот пуст — сбрасываем всё
        if (inputStack.isEmpty()) {
            be.progress = 0;
            be.maxProgress = 0;
            be.idleTime = 0;
            return;
        }

        DryingTableRecipe recipe = be.getCurrentRecipe(world);

        if (recipe != null) {
            // Рецепт найден — сушим
            be.idleTime = 0;

            if (be.maxProgress != recipe.getDryingTime()) {
                be.maxProgress = recipe.getDryingTime();
                be.progress = 0;
            }

            be.progress++;

            // Частицы дыма каждые 20 тиков
            if (be.progress % 20 == 0) {
                world.addParticle(
                        ParticleTypes.SMOKE,
                        pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5,
                        0, 0.05, 0
                );
            }

            if (be.progress >= be.maxProgress) {
                be.craftRecipe(world, pos, recipe);
                be.progress = 0;
            }

        } else {
            // Рецепта нет — считаем время до пепла
            be.progress = 0;
            be.maxProgress = 0;
            be.idleTime++;

            if (be.idleTime >= BURN_TO_ASH_TIME) {
                // Превращаем в пепел
                be.inventory.set(0, ItemStack.EMPTY);
                be.idleTime = 0;

                ItemStack ash = new ItemStack(
                        net.minecraft.registry.Registries.ITEM.get(
                                new net.minecraft.util.Identifier("numenology", "ash")
                        )
                );

                if (!ash.isEmpty()) {
                    dropStack(world, pos, ash);
                }
            }
        }

        be.markDirty();
    }

    // ===== РЕЦЕПТ =====
    private DryingTableRecipe getCurrentRecipe(World world) {
        SimpleInventory inv = new SimpleInventory(1);
        inv.setStack(0, this.inventory.get(0));
        return world.getRecipeManager()
                .getFirstMatch(ModRecipes.DRYING_TABLE_TYPE, inv, world)
                .orElse(null);
    }

    private void craftRecipe(World world, BlockPos pos, DryingTableRecipe recipe) {
        this.inventory.get(0).decrement(recipe.getCount());
        ItemStack result = recipe.getOutput(null).copy();

        // Если в выходном слоте есть место — кладём туда
        ItemStack outputSlot = this.inventory.get(1);
        if (outputSlot.isEmpty()) {
            this.inventory.set(1, result);
        } else if (outputSlot.isOf(result.getItem()) &&
                outputSlot.getCount() + result.getCount() <= outputSlot.getMaxCount()) {
            outputSlot.increment(result.getCount());
        } else {
            // Выходной слот занят — дропаем рядом
            dropStack(world, pos, result);
        }
    }

    // ===== DROP =====
    private static void dropStack(World world, BlockPos pos, ItemStack stack) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 1.0;
        double z = pos.getZ() + 0.5;
        ItemEntity entity = new ItemEntity(world, x, y, z, stack);
        entity.setVelocity(0, 0.1, 0);
        world.spawnEntity(entity);
    }

    public PropertyDelegate getPropertyDelegate() { return propertyDelegate; }

    // ===== DISPLAY NAME =====
    @Override
    public Text getDisplayName() {
        return Text.translatable("block.numenology.drying_table");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public net.minecraft.screen.ScreenHandler createMenu(int syncId,
                                                         net.minecraft.entity.player.PlayerInventory playerInventory,
                                                         net.minecraft.entity.player.PlayerEntity player) {
        return new numenology.screen.DryingTableScreenHandler(syncId, playerInventory, this);
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        // Создает пакет, который отправляет NBT данные блока на клиент при любом изменении
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        // Проверяем, что мы находимся на сервере и мир существует
        if (this.world != null && !this.world.isClient) {
            // Уведомляем клиентские рендереры о том, что состояние блока изменилось
            this.world.updateListeners(this.pos, this.getCachedState(), this.getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    // ===== INVENTORY =====
    @Override public int size() { return inventory.size(); }
    @Override public boolean isEmpty() {
        for (ItemStack s : inventory) if (!s.isEmpty()) return false;
        return true;
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        // 🌟 Для слота ВЫХОДА (1): воронки не могут ничего туда положить
        if (slot == 1) {
            return false;
        }

        // 🌟 Для слота ВХОДА (0): воронка может положить предмет только если:
        // 1. Выходной слот (1) пуст.
        // 2. В самом входном слоте (0) еще нет предметов (чтобы воронка держала там строго по 1 шт).
        if (slot == 0) {
            return this.getStack(1).isEmpty() && this.getStack(0).isEmpty();
        }

        return true;
    }

    @Override public ItemStack getStack(int slot) { return inventory.get(slot); }
    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(inventory, slot, amount);
        if (!result.isEmpty()) {
            markDirty(); // 🌟 ФИКС: Воронка или игрок забрали часть предметов — обновляем клиент
        }
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(inventory, slot);
        if (!result.isEmpty()) {
            markDirty(); // 🌟 ФИКС: Предмет полностью забрали — обновляем клиент
        }
        return result;
    }
    @Override public void setStack(int slot, ItemStack stack) { inventory.set(slot, stack); markDirty(); }
    @Override public void clear() { inventory.clear(); }
    @Override public boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) { return true; }
}