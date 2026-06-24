package numenology.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import numenology.item.custom.DuffelBagItem;

public class DuffelBagScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private static final int BAG_SIZE = 9;

    public DuffelBagScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(BAG_SIZE));
    }

    public DuffelBagScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.DUFFEL_BAG_SCREEN_HANDLER, syncId);
        checkSize(inventory, BAG_SIZE);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);

        // 1. Сетка ВЕЩМЕШКА 3x3 (Смещена на 3 пикселя вверх: Y = 14)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                this.addSlot(new Slot(inventory, col + row * 3, 62 + col * 18, 11 + row * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return inventory.isValid(0, stack);
                    }
                });
            }
        }

        // 2. Основной инвентарь ИГРОКА (3 ряда по 9 слотов)
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18) {
                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return !(getStack().getItem() instanceof DuffelBagItem);
                    }
                });
            }
        }

        // 3. Хотбар ИГРОКА (Выровнен в один ровный ряд: Y = 142)
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142) {
                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    return !(getStack().getItem() instanceof DuffelBagItem);
                }
            });
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();

            if (invSlot < BAG_SIZE) {
                if (!this.insertItem(originalStack, BAG_SIZE, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (originalStack.getItem() instanceof DuffelBagItem || !this.insertItem(originalStack, 0, BAG_SIZE, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.inventory.onClose(player);
    }
}