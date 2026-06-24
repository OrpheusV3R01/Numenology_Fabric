package numenology.world.gen;

import numenology.Numenology;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.stateprovider.BlockStateProviderType;

public class ModStateProviders {

    public static final BlockStateProviderType<NumenLogStateProvider> NUMEN_LOG_PROVIDER =
            Registry.register(
                    Registries.BLOCK_STATE_PROVIDER_TYPE,
                    new Identifier(Numenology.MOD_ID, "numen_log_provider"),
                    new BlockStateProviderType<>(NumenLogStateProvider.CODEC)
            );

    public static void register() {
        // можно оставить пустым, но удобно для вызова
    }
}