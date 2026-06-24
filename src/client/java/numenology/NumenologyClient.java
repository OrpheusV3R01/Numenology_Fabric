package numenology.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

import numenology.block.ModBlockEntities;
import numenology.nodes.NumenNodeRenderer;

public class NumenologyClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		BlockEntityRendererFactories.register(
				ModBlockEntities.NUMEN_NODE,
				NumenNodeRenderer::new
		);
	}
}