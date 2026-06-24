package numenology;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import numenology.codex.CodexManager;
import numenology.recipe.ModRecipes;
import numenology.research.ResearchEvents;
import numenology.research.ResearchManager;
import numenology.research.ResearchUseHandler;
import numenology.screen.ModScreenHandlers;
import numenology.world.LumenEnergySystem;
import numenology.world.gen.ModBiomeModifications;
import numenology.world.gen.ModStateProviders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import numenology.block.ModBlockEntities;
import numenology.block.ModBlocks;
import numenology.command.NumenCommand;
import numenology.item.ModItems;
import numenology.item.ModItemGroups;
import numenology.world.gen.ModOreGeneration;
import numenology.world.gen.feature.ModFeatures;
import numenology.world.NumenTreeManager;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import numenology.recipe.CrucibleRecipeManager;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;
import numenology.recipe.CrucibleReloadListener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import numenology.research.KnowledgeManager;
import numenology.network.ModPackets;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;


// 🔹 Integration
import numenology.integration.trinkets.TrinketsIntegration;
import software.bernie.geckolib.GeckoLib;

public class Numenology implements ModInitializer {

	public static final String MOD_ID = "numenology";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {


		LOGGER.info("Loading Numenology...");

		// ==============================
		// 🔹 Integration (ОЧЕНЬ РАНО)
		// ==============================
		TrinketsIntegration.init();
		CodexManager.init();
		//ResearchManager.init();

		// ==============================
		// 🔥 СНАЧАЛА БАЗА (КРИТИЧНО)
		// ==============================

		ModItems.registerModItems();
		ModBlocks.registerModBlocks();


		// 🔥 ВАЖНО: ПЕРЕНЕСЛИ СЮДА
		ModBlockEntities.registerBlockEntities();

		// ==============================
		// ОСТАЛЬНОЕ
		// ==============================
		GeckoLib.initialize();

		ModRecipes.register();
		ModFeatures.register();
		ResourceManagerHelper.get(ResourceType.SERVER_DATA)
				.registerReloadListener(new CrucibleReloadListener());

		ModPackets.registerC2S();
		ModScreenHandlers.register();
		// ==============================
		// 🔥 ОБТЁСЫВАНИЕ
		// ==============================

		StrippableBlockRegistry.register(ModBlocks.NUMEN_LOG, ModBlocks.STRIPPED_NUMEN_LOG);
		StrippableBlockRegistry.register(ModBlocks.NUMEN_RESIN_LOG, ModBlocks.STRIPPED_NUMEN_LOG);

		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			// Проверяем только на сервере (чтобы не дублировать дроп) и только если игрок держит топор в активной руке
			if (!world.isClient() && hand == Hand.MAIN_HAND && player.getStackInHand(hand).getItem() instanceof AxeItem) {
				BlockState state = world.getBlockState(hitResult.getBlockPos());

				// Если кликнули топором по смолянистому бревну
				if (state.isOf(ModBlocks.NUMEN_RESIN_LOG)) {
					// Создаем сущность предмета смолы на месте блока
					ItemEntity resinEntity = new ItemEntity(world,
							hitResult.getBlockPos().getX() + 0.5,
							hitResult.getBlockPos().getY() + 0.5,
							hitResult.getBlockPos().getZ() + 0.5,
							new ItemStack(ModItems.NUMEN_RESIN, 1)
					);
					world.spawnEntity(resinEntity);

					// Возвращаем PASS, чтобы ванильный топор ДО СИХ ПОР обтесал бревно в stripped-версию
					return ActionResult.PASS;
				}
			}
			return ActionResult.PASS;
		});

		StrippableBlockRegistry.register(ModBlocks.UMBRA_LOG, ModBlocks.STRIPPED_UMBRA_LOG);
		StrippableBlockRegistry.register(ModBlocks.UMBRA_RESIN_LOG, ModBlocks.STRIPPED_UMBRA_LOG);

		StrippableBlockRegistry.register(ModBlocks.LUMEN_LOG, ModBlocks.STRIPPED_LUMEN_LOG);
		StrippableBlockRegistry.register(ModBlocks.LUMEN_RESIN_LOG, ModBlocks.STRIPPED_LUMEN_LOG);

		// ==============================
		// 🌍 ГЕНЕРАЦИЯ
		// ==============================

		ModOreGeneration.generateOres();
		ModBiomeModifications.register();
		ModStateProviders.register();
		NumenTreeManager.register();

		// ==============================
		// 🔥 ТОПЛИВО
		// ==============================

		FuelRegistry.INSTANCE.add(ModBlocks.NUMEN_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.NUMEN_RESIN_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.STRIPPED_NUMEN_LOG, 1600);

		FuelRegistry.INSTANCE.add(ModBlocks.UMBRA_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.UMBRA_RESIN_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.STRIPPED_UMBRA_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.UMBRA_PLANKS, 300);

		FuelRegistry.INSTANCE.add(ModBlocks.LUMEN_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.LUMEN_RESIN_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.STRIPPED_LUMEN_LOG, 1600);
		FuelRegistry.INSTANCE.add(ModBlocks.LUMEN_PLANKS, 300);

		LumenEnergySystem.register();
		ResearchEvents.register();
		ResearchUseHandler.register();

		// ==============================
		// 📦 ГРУППЫ
		// ==============================

		ModItemGroups.registerItemGroups();


		// ==========================================
		// 💧 ЛОГИКА ДРОПА СМОЛЫ ПРИ СРУБАНИИ ДЕРЕВА
		// ==========================================
		net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			// Проверяем только на сервере, только для смолянистого бревна и только если игрок не в креативе
			if (!world.isClient() && state.isOf(ModBlocks.NUMEN_RESIN_LOG) && !player.isCreative()) {

				// Проверяем наличие листвы над бревном (сканируем до 5 блоков вверх)
				boolean isWildTree = false;
				for (int i = 1; i <= 5; i++) {
					BlockState upperState = world.getBlockState(pos.up(i));
					if (upperState.isOf(ModBlocks.NUMEN_LEAVES)) {
						isWildTree = true;
						break;
					}
				}

				// Если сверху обнаружена родная листва — это настоящее живое дерево!
				if (isWildTree) {
					net.minecraft.util.math.random.Random random = world.getRandom();

					// Шанс 25% (0.25F)
					if (random.nextFloat() < 0.25F) {
						int count = random.nextInt(5) + 2; // Случайно от 2 до 6 штук

						ItemEntity resinEntity = new ItemEntity(world,
								pos.getX() + 0.5,
								pos.getY() + 0.5,
								pos.getZ() + 0.5,
								new ItemStack(ModItems.NUMEN_RESIN, count)
						);
						world.spawnEntity(resinEntity);
					}
				}
			}
		});


		// ==============================
		// 📜 КОМАНДЫ
		// ==============================

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			NumenCommand.register(dispatcher);
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				KnowledgeManager.tick(player, player.getWorld().getTime());
			}
		});

		// ==============================
// 💾 NBT SAVE / LOAD
// ==============================

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.player;
			KnowledgeManager.load(player);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.player;
			KnowledgeManager.save(player);
		});

	}
}