package numenology.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.slot.Slot;

import numenology.block.entity.DryingTableBlockEntity;

public class DryingTableScreenHandler extends ScreenHandler {

    private final PropertyDelegate propertyDelegate;
    private final DryingTableBlockEntity blockEntity;

    // Серверный конструктор
    public DryingTableScreenHandler(int syncId, PlayerInventory playerInventory, DryingTableBlockEntity blockEntity) {
        super(ModScreenHandlers.DRYING_TABLE, syncId);

        this.blockEntity = blockEntity;
        this.propertyDelegate = blockEntity.getPropertyDelegate();

        // ===== СЛОТЫ СУШИЛКИ =====
// Слот 0 — вход (предмет для сушки)
        this.addSlot(new Slot(blockEntity, 0, 54, 27) {
            @Override
            public boolean canInsert(ItemStack stack) {
                // 🌟 ОГРАНИЧЕНИЕ: Можно положить предмет только если выходной слот (1) абсолютно пуст
                return blockEntity.getStack(1).isEmpty();
            }

            @Override
            public int getMaxItemCount() {
                // 🌟 ОГРАНИЧЕНИЕ: В слот можно положить максимум 1 единицу предмета
                return 1;
            }
        });

// Слот 1 — выход
        this.addSlot(new Slot(blockEntity, 1, 106, 27) {
            @Override
            public boolean canInsert(ItemStack stack) {
                // 🌟 ОГРАНИЧЕНИЕ: Игрок не может вручную положить сюда никакой предмет
                return false;
            }
        });

// ===== ИНВЕНТАРЬ ИГРОКА (3 ряда) =====
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                // Изменили 104 на 86, чтобы поднять сетку на 18 пикселей вверх
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }

// ===== ХОТБАР =====
        for (int m = 0; m < 9; ++m) {
            // Изменили 162 на 144, чтобы поднять хотбар вслед за инвентарем
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

        this.addProperties(this.propertyDelegate);
    }

    // Клиентский fallback
    public DryingTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new DryingTableBlockEntity(
                net.minecraft.util.math.BlockPos.ORIGIN,
                net.minecraft.block.Blocks.AIR.getDefaultState()
        ));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int max = this.propertyDelegate.get(1);
        int barSize = 24;
        return max != 0 ? progress * barSize / max : 0;
    }

    // Добавь эти методы в любое место внутри класса DryingTableScreenHandler
    public int getProgress() {
        return this.propertyDelegate.get(0); // Получаем текущий прогресс
    }

    public int getMaxProgress() {
        return this.propertyDelegate.get(1); // Получаем максимальное время рецепта
    }
}