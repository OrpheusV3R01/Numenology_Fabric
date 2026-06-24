package numenology.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import numenology.Numenology;
import numenology.network.packet.C2SCompleteResearchPacket;
import numenology.network.packet.C2SOpenDuffelBagPacket; // Импортируем новый пакет
import numenology.network.packet.S2CKnowledgeSyncPacket;

public class ModPackets {

    public static final Identifier COMPLETE_RESEARCH =
            new Identifier(Numenology.MOD_ID, "complete_research");

    public static final Identifier SYNC_KNOWLEDGE =
            new Identifier(Numenology.MOD_ID, "sync_knowledge");

    // Идентификатор пакета для открытия вещмешка
    public static final Identifier OPEN_DUFFEL_BAG_PACKET =
            new Identifier(Numenology.MOD_ID, "open_duffel_bag");

    // SERVER SIDE
    public static void registerC2S() {
        ServerPlayNetworking.registerGlobalReceiver(
                COMPLETE_RESEARCH,
                C2SCompleteResearchPacket::receive
        );

        // Регистрируем пакет открытия вещмешка точно так же, как и исследование!
        ServerPlayNetworking.registerGlobalReceiver(
                OPEN_DUFFEL_BAG_PACKET,
                C2SOpenDuffelBagPacket::receive
        );
    }

    // CLIENT SIDE
    public static void registerS2C() {
        ClientPlayNetworking.registerGlobalReceiver(
                SYNC_KNOWLEDGE,
                S2CKnowledgeSyncPacket::receive
        );
    }
}