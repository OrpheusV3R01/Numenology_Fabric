package numenology.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;

import numenology.block.entity.NumenSmelteryBlockEntity;

public class NumenSmelteryScreenHandler extends ScreenHandler {

    private final net.minecraft.screen.PropertyDelegate propertyDelegate;

    private final NumenSmelteryBlockEntity blockEntity;

    // основной конструктор (используется сервером)
    public NumenSmelteryScreenHandler(int syncId, PlayerInventory playerInventory, NumenSmelteryBlockEntity blockEntity) {
        super(ModScreenHandlers.NUMEN_SMELTERY, syncId);

        this.blockEntity = blockEntity;

        // ===== СЛОТЫ ПЛАВИЛЬНИ (РЕАЛЬНЫЕ) =====
        this.addSlot(new Slot(blockEntity, 0, 44, 35));
        this.addSlot(new Slot(blockEntity, 1, 116, 35));


// 1. Основной инвентарь (3 ряда) - делаем всё на одной линии 103 (золотая середина)
        for (int m = 0; m < 3; ++m) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 104 + m * 18));
            }
        }

        // 2. Хотбар - опускаем на 161 (чтобы между инвентарем и хотбаром было ровно 4 пикселя как в ванилле)
        for (int m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 162));
        }

        this.propertyDelegate = blockEntity.getPropertyDelegate();
        this.addProperties(this.propertyDelegate);


    }

    // клиентский fallback (Fabric требует)
    public NumenSmelteryScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new NumenSmelteryBlockEntity(net.minecraft.util.math.BlockPos.ORIGIN, net.minecraft.block.Blocks.AIR.getDefaultState()));
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

        int barSize = 24; // ширина полоски

        return max != 0 ? progress * barSize / max : 0;
    }

}