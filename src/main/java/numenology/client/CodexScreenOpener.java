package numenology.client;

import net.minecraft.client.MinecraftClient;
import numenology.client.screen.CodexScreen;

public class CodexScreenOpener {
    // Этот метод будет вызываться только на клиенте
    public static void open() {
        MinecraftClient.getInstance().setScreen(new CodexScreen());
    }
}