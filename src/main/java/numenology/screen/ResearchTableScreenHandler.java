package numenology.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.item.ItemStack;

public class ResearchTableScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public ResearchTableScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(3));
    }

    public ResearchTableScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(ModScreenHandlers.RESEARCH_TABLE, syncId);

        this.inventory = inventory;

        int tableX = 20;
        int tableY = 15;

        // Codex
        this.addSlot(new Slot(inventory, 0, tableX, tableY) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(numenology.item.ModItems.NUMEN_CODEX);
            }
        });

        // Paper
        this.addSlot(new Slot(inventory, 1, tableX + 20, tableY) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(net.minecraft.item.Items.PAPER);
            }
        });

        // Tools
        this.addSlot(new Slot(inventory, 2, tableX + 40, tableY) {


            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(numenology.item.ModItems.SCRIBER_TOOLS);
            }
        });

        // =========================
        // ✅ ИНВЕНТАРЬ (ЦЕНТР И СДВИГ ВНИЗ)
        // =========================

// Если invX и invY — это точка начала всей нарисованной сетки (включая серые бортики)
        int invX = 70;
        int invY = 154;

// Инвентарь игрока (3x9)
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9,
                        invX + j * 18,
                        invY + i * 18
                ));
            }
        }

// Хотбар
// Расстояние между инвентарем и хотбаром в 4 пикселя: (18 * 3) + 4 = 58
        for (int j = 0; j < 9; ++j) {
            this.addSlot(new Slot(playerInventory, j,
                    invX + j * 18,
                    invY + 58
            ));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }
}