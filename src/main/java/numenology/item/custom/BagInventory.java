package numenology.item.custom;

import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class BagInventory implements ImplementedInventory {
    // Создаем фиксированный список из 9 слотов (сетка 3x3)
    private final DefaultedList<ItemStack> items = DefaultedList.ofSize(9, ItemStack.EMPTY);
    private final ItemStack bagStack;

    public BagInventory(ItemStack bagStack) {
        this.bagStack = bagStack;

        // Читаем существующие предметы из NBT вещмешка, если они там есть
        NbtCompound nbt = bagStack.getNbt();
        if (nbt != null && nbt.contains("Inventory", 10)) {
            Inventories.readNbt(nbt.getCompound("Inventory"), items);
        }
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void markDirty() {
        // При любом изменении инвентаря (положили/забрали предмет) — записываем данные в NBT мешка
        NbtCompound nbt = bagStack.getOrCreateNbt();
        NbtCompound inventoryNbt = new NbtCompound();
        Inventories.writeNbt(inventoryNbt, items);
        nbt.put("Inventory", inventoryNbt);
    }

    /**
     * Валидация предметов для слотов мешка.
     * Здесь реализуется жесткое ограничение: нельзя вкладывать мешок в мешок.
     */
    @Override
    public boolean isValid(int slot, ItemStack stack) {
        // Если предмет, который пытаются положить, является нашим вещмешком — запрещаем
        return !(stack.getItem() instanceof DuffelBagItem);
    }
}