package emasher.gas.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import emasher.gas.CommonProxy;
import emasher.gas.EmasherGas;
import emasher.gas.EntitySmokeBomb;
import net.minecraft.client.renderer.entity.RenderSnowball;

public class ClientProxy extends CommonProxy {
	@Override
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler( EntitySmokeBomb.class, new RenderSnowball( EmasherGas.smokeGrenade ) );
	}
}
