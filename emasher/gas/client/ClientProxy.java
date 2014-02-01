package emasher.gas.client;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.src.ModLoader;
import net.minecraftforge.client.MinecraftForgeClient;
import emasher.gas.CommonProxy;
import emasher.gas.EmasherGas;
import emasher.gas.EntitySmokeBomb;

public class ClientProxy extends CommonProxy 
{
	@Override
	public void registerRenderers() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySmokeBomb.class, new RenderSnowball(EmasherGas.smokeGrenade));
	}
}
