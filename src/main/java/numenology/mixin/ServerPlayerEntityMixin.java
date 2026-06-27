package numenology.mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import numenology.block.custom.CanvasBedBlock; // Замени на правильный импорт твоего блока, если папка отличается
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "setSpawnPoint", at = @At("HEAD"), cancellable = true)
    private void preventCanvasBedSpawn(RegistryKey<World> dimension, BlockPos pos, float angle, boolean forced, boolean sendMessage, CallbackInfo ci) {
        if (pos != null) {
            ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
            // Проверяем: если блок под новыми координатами спавна — наша холщовая кровать, отменяем установку спавна!
            if (player.getWorld().getBlockState(pos).getBlock() instanceof CanvasBedBlock) {
                ci.cancel();
            }
        }
    }
}