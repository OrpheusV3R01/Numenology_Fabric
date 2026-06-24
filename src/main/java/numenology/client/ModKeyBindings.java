package numenology.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import numenology.network.ModPackets;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static KeyBinding duffelBagKey;

    public static void register() {
        duffelBagKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.numenology.open_duffel_bag",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B, // Кнопка 'B' по умолчанию
                "category.numenology.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (duffelBagKey.wasPressed()) {
                if (client.player != null && client.currentScreen == null) {
                    // Отправляем пакет на сервер
                    ClientPlayNetworking.send(ModPackets.OPEN_DUFFEL_BAG_PACKET, PacketByteBufs.empty());
                }
            }
        });
    }
}