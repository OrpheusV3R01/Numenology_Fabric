package numenology.network.packet;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import numenology.item.custom.DuffelBagItem;

public class C2SOpenDuffelBagPacket {
    public static void receive(MinecraftServer server, ServerPlayerEntity player,
                               ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            // Ищем вещмешок в слотах аксессуаров игрока через Trinkets API
            TrinketsApi.getTrinketComponent(player).ifPresent(trinkets -> {
                var equipped = trinkets.getEquipped(stack -> stack.getItem() instanceof DuffelBagItem);
                if (!equipped.isEmpty()) {
                    // Берем ItemStack мешка и открываем GUI
                    ItemStack bagStack = equipped.get(0).getRight();
                    DuffelBagItem.openScreen(player, bagStack);
                }
            });
        });
    }
}