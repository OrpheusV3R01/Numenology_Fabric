package numenology.item.custom;

import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import numenology.screen.DuffelBagScreenHandler;

public class DuffelBagItem extends TrinketItem {

    public DuffelBagItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        // Логика открытия мешка из руки при клике ПКМ
        if (!world.isClient()) {
            openScreen(user, itemStack);
        }

        return TypedActionResult.success(itemStack, world.isClient());
    }

    /**
     * Статичный метод для открытия экрана мешка.
     * Он нам понадобится как здесь (для ПКМ из руки), так и позже для пакета хоткея (когда мешок на спине).
     */
    public static void openScreen(PlayerEntity player, ItemStack bagStack) {
        player.openHandledScreen(new SimpleNamedScreenHandlerFactory(
                (syncId, playerInventory, playerEntity) ->
                        new DuffelBagScreenHandler(syncId, playerInventory, new BagInventory(bagStack)),
                Text.translatable("item.numenology.duffel_bag") // Локализация заголовка окна
        ));
    }
}