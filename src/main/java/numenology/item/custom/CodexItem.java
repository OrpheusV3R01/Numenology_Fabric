package numenology.item.custom;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;

// Импортируем ТОЛЬКО наш класс-прослойку
import numenology.client.CodexScreenOpener;

public class CodexItem extends Item {

    public CodexItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        // Проверка остается, но теперь внутри безопасный вызов
        if (world.isClient) {
            CodexScreenOpener.open();
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}