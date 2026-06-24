package numenology.client;

import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import numenology.client.render.DryingTableRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.math.RotationAxis;
import numenology.client.hud.NumenHudRenderer;
import numenology.client.model.FocusingGauntletModel;
import numenology.client.render.FocusingGauntletRenderer;
import numenology.client.render.MonocularTrinketRenderer;
import numenology.client.render.NumenSteelChestplateRenderer;
import numenology.client.screen.DryingTableScreen;
import numenology.client.screen.NumenSmelteryScreen;
import numenology.item.custom.NumenSteelChestplateItem;
import numenology.screen.ModScreenHandlers;

import numenology.screen.ModScreenHandlers;
import numenology.client.screen.ResearchTableScreen;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.ItemStack;

import numenology.item.ModItems;

import numenology.client.render.ResearchTableRenderer;
import numenology.block.ModBlocks;
import numenology.block.ModBlockEntities;
import numenology.network.ModPackets;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import net.minecraft.entity.player.PlayerEntity;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import numenology.client.render.NumenSteelChestplateRenderer;
import numenology.item.custom.NumenSteelChestplateItem;



public class NumenologyClient implements ClientModInitializer {

    public static final EntityModelLayer GAUNTLET_LAYER = new EntityModelLayer(new Identifier("numenology", "focusing_gauntlet"), "main");

    @Override
    public void onInitializeClient() {
        System.out.println("CLIENT INIT");



        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new Identifier("numenology", "item/numenometer_3d"));
            out.accept(new Identifier("numenology", "item/numenometer_gui")); // 2D иконка

            out.accept(new Identifier("numenology", "item/numen_steel_helmet"));
        });

        ArmorRenderer.register((matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {

            MinecraftClient client = MinecraftClient.getInstance();

            ItemRenderer renderer = client.getItemRenderer();

            BakedModel model = client.getBakedModelManager().getModel(
                    new Identifier("numenology", "item/numen_steel_helmet")
            );

            matrices.push();

            // Следуем за головой
            contextModel.head.rotate(matrices);

            // =========================
            // FIX ITEM MODEL SPACE
            // =========================

            matrices.scale(1.0F, -1.0F, -1.0F);

            matrices.scale(0.625F, 0.625F, 0.625F);

            matrices.translate(-0.5F, -0.5F, -0.5F);

            matrices.translate(0.5, 0.95, 0.5);

            // =========================
            // ВОТ ЭТО ГЛАВНЫЙ ФИКС
            // =========================

            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

            renderer.renderItem(
                    stack,
                    ModelTransformationMode.NONE,
                    false,
                    matrices,
                    vertexConsumers,
                    light,
                    OverlayTexture.DEFAULT_UV,
                    model
            );

            matrices.pop();

        }, ModItems.NUMEN_STEEL_HELMET);

        BuiltinItemRendererRegistry.INSTANCE.register(ModItems.NUMENOMETER, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
            BakedModelManager modelManager = MinecraftClient.getInstance().getBakedModelManager();

            // 1. ПРОВЕРКА НА РУКИ (3D МОДЕЛЬ)
            if (mode.isFirstPerson() || mode == ModelTransformationMode.THIRD_PERSON_RIGHT_HAND || mode == ModelTransformationMode.THIRD_PERSON_LEFT_HAND) {
                BakedModel model3d = modelManager.getModel(new Identifier("numenology", "item/numenometer_3d"));
                matrices.push();
                model3d.getTransformation().getTransformation(mode).apply(false, matrices);
                // Твои проверенные координаты для рук[cite: 37]
                matrices.translate(-0.5f, 0.50f, 0.55f);
                renderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, model3d);
                matrices.pop();

            } else {
                BakedModel guiModel = modelManager.getModel(new Identifier("numenology", "item/numenometer_gui"));
                matrices.push();

                if (mode == ModelTransformationMode.GUI) {
                    matrices.translate(0.25f, 0.25f, 0.0f);
                    guiModel.getTransformation().getTransformation(mode).apply(false, matrices);
                    matrices.translate(0.25f, 0.25f, 0.0f);
                }
                else if (mode == ModelTransformationMode.GROUND) {
                    matrices.translate(0.25f, 0.25f, 0.25f);
                    guiModel.getTransformation().getTransformation(mode).apply(false, matrices);
                    matrices.scale(0.5f, 0.5f, 0.5f); // Увеличиваем, чтобы не был крошечным
                    matrices.translate(0.25f, 0.25f, 0.25f);
                }
                else if (mode == ModelTransformationMode.FIXED) {

                    matrices.translate(0.0f, 0.0f, 0.5f);

                    // ЦЕНТРИРУЕМ И МАСШТАБИРУЕМ
                    matrices.translate(0.25f, 0.25f, 0.0f);
                    matrices.scale(1f, 1f, 1f); // Вернем стандартный масштаб
                    matrices.translate(0.25f, 0.25f, 0.0f);
                }

                renderer.renderItem(stack, mode, false, matrices, vertexConsumers, light, overlay, guiModel);
                matrices.pop();
            }
        });



        TrinketRendererRegistry.registerRenderer(ModItems.FOCUSING_GAUNTLET, new FocusingGauntletRenderer());

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TURQUOISE_BUSH, RenderLayer.getCutout());

        ModelPredicateProviderRegistry.register(ModItems.GARDEN_BAG, new Identifier("filled"),
                (stack, world, entity, seed) -> {
                    return stack.hasNbt() && stack.getNbt().getBoolean("hasBush") ? 1.0F : 0.0F;
                });

        NumenHudRenderer.init();

        System.out.println("[Numenology] Client HUD initialized!");

// Твоя существующая строка регистрации рендерера (оставь её)
        TrinketRendererRegistry.registerRenderer(ModItems.FOCUSING_GAUNTLET, new FocusingGauntletRenderer());

        MonocularTrinketRenderer.register();

        numenology.client.render.DuffelBagRenderer.register();

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            out.accept(new net.minecraft.util.Identifier("numenology", "item/duffel_bag_baked"));
        });


        HandledScreens.register(
                ModScreenHandlers.NUMEN_SMELTERY,
                NumenSmelteryScreen::new
        );

        // =========================
        // 🔥 GUI РЕГИСТРАЦИЯ (ВОТ ЧЕГО НЕ ХВАТАЛО)
        // =========================
        HandledScreens.register(
                ModScreenHandlers.RESEARCH_TABLE,
                ResearchTableScreen::new
        );

        // =========================
        // 🔥 ВОТ ФИКС
        // =========================
        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.RESEARCH_TABLE,
                RenderLayer.getCutout()
        );

        // =========================
        // 🔮 NUMEN NODE
        // =========================
        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.NUMEN_NODE,
                RenderLayer.getTranslucent()
        );

        // =========================
        // 🌳 NUMEN TREE
        // =========================
        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.NUMEN_SAPLING,
                RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.NUMEN_LEAVES,
                RenderLayer.getCutout()
        );

        ModPackets.registerS2C();

        // =========================
        // 🌑 UMBRA TREE
        // =========================
        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.UMBRA_SAPLING,
                RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.UMBRA_LEAVES,
                RenderLayer.getCutout()
        );

        // =========================
        // 🌕 LUMEN TREE
        // =========================
        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.LUMEN_SAPLING,
                RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.LUMEN_LEAVES,
                RenderLayer.getCutout()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
                ModBlocks.NUMEN_GLASS,
                RenderLayer.getTranslucent()
        );

        BlockRenderLayerMap.INSTANCE.putBlock(
                        ModBlocks.NUMEN_CLUSTER,
                        RenderLayer.getTranslucent()
        );

        net.minecraft.client.render.block.entity.BlockEntityRendererFactories.register(
                numenology.block.ModBlockEntities.NUMEN_NODE,
                numenology.nodes.NumenNodeRenderer::new
        );

        BlockEntityRendererFactories.register(
                ModBlockEntities.DRYING_TABLE_BE,
                numenology.client.render.DryingTableRenderer::new
        );

        net.minecraft.client.gui.screen.ingame.HandledScreens.register(
                numenology.screen.ModScreenHandlers.DUFFEL_BAG_SCREEN_HANDLER,
                numenology.screen.DuffelBagScreen::new
        );

        // Регистрация горячих клавиш мода
        numenology.client.ModKeyBindings.register();

        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DRYING_TABLE,
                RenderLayer.getCutout());

        HandledScreens.register(ModScreenHandlers.DRYING_TABLE, DryingTableScreen::new);

        // =========================
        // 🧪 ТИГЕЛЬ РЕНДЕР (ФИКС)
        // =========================
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {

            ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
                if (world != null && pos != null) {
                    return BiomeColors.getWaterColor(world, pos);
                }
                return 0x3F76E4;
            }, ModBlocks.NUMEN_CRUCIBLE);

            System.out.println("REGISTER RENDERER");

        });



    }
}