package numenology.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import numenology.block.ModBlockEntities;
import numenology.screen.ResearchTableScreenHandler;

public class ResearchTableBlockEntity extends BlockEntity implements Inventory, NamedScreenHandlerFactory {

    private final DefaultedList<ItemStack> items =
            DefaultedList.ofSize(3, ItemStack.EMPTY);

    public ResearchTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESEARCH_TABLE, pos, state);
    }

    // =========================
    // GUI
    // =========================

    @Override
    public Text getDisplayName() {
        return Text.literal("Research Table");
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, net.minecraft.entity.player.PlayerEntity player) {
        return new ResearchTableScreenHandler(syncId, playerInventory, this);
    }

    // =========================
    // INVENTORY
    // =========================

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int slot) {
        return items.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = Inventories.splitStack(items, slot, amount);
        markDirty();
        return result;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = Inventories.removeStack(items, slot);
        markDirty();
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        items.set(slot, stack);
        markDirty();

        if (world != null && !world.isClient) {

            boolean hasTools = !items.get(2).isEmpty();
            boolean hasPaper = !items.get(1).isEmpty();
            boolean hasCodex = !items.get(0).isEmpty();

            numenology.block.custom.ResearchTableState newState;

            if (hasTools && hasPaper && hasCodex) {
                newState = numenology.block.custom.ResearchTableState.FULL;
            } else if (hasTools && hasPaper) {
                newState = numenology.block.custom.ResearchTableState.TOOLS_PAPER;
            } else if (hasTools) {
                newState = numenology.block.custom.ResearchTableState.TOOLS;
            } else {
                newState = numenology.block.custom.ResearchTableState.EMPTY;
            }

            BlockState current = world.getBlockState(pos);

            if (current.contains(numenology.block.custom.ResearchTableBlock.STATE)
                    && current.get(numenology.block.custom.ResearchTableBlock.STATE) != newState) {

                world.setBlockState(pos, current.with(
                        numenology.block.custom.ResearchTableBlock.STATE,
                        newState
                ));
            }
        }
    }

    @Override
    public boolean canPlayerUse(net.minecraft.entity.player.PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        items.clear();
    }

    // =========================
    // NBT
    // =========================

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, items);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, items);
    }
}