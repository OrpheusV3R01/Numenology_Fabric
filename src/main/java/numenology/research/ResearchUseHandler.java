package numenology.research;

import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import numenology.item.ModItems;

public class ResearchUseHandler {

    public static void register() {

        UseItemCallback.EVENT.register((player, world, hand) -> {

            if (world.isClient) {
                return TypedActionResult.pass(player.getStackInHand(hand));
            }

            if (!(player instanceof ServerPlayerEntity serverPlayer)) {
                return TypedActionResult.pass(player.getStackInHand(hand));
            }

            if (hand != Hand.MAIN_HAND) {
                return TypedActionResult.pass(player.getStackInHand(hand));
            }

            if (player.isSneaking()) {

                if (player.getMainHandStack().getItem() == ModItems.NUMEN_CODEX) {

                    var data = KnowledgeManager.get(serverPlayer);


                    for (String id : data.getAllSuspected().keySet()) {

                        KnowledgeManager.complete(serverPlayer, id);

                        return TypedActionResult.success(player.getStackInHand(hand));
                    }
                }
            }

            return TypedActionResult.pass(player.getStackInHand(hand));
        });
    }
}