package numenology.recipe;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class CrucibleReloadListener implements IdentifiableResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return new Identifier("numenology", "crucible_recipes");
    }

    @Override
    public CompletableFuture<Void> reload(Synchronizer synchronizer,
                                          ResourceManager manager,
                                          Profiler prepareProfiler,
                                          Profiler applyProfiler,
                                          Executor prepareExecutor,
                                          Executor applyExecutor) {

        // ПРАВИЛЬНЫЙ FLOW

        return CompletableFuture
                .runAsync(() -> {
                    // prepare стадия (можно парсить JSON)
                }, prepareExecutor)
                .thenCompose(synchronizer::whenPrepared)
                .thenRunAsync(() -> {
                    // apply стадия
                    CrucibleRecipeManager.load(manager);
                }, applyExecutor);
    }
}