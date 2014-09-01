package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
//import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModRSOR extends RSGateModule
{

	public ModRSOR(int id)
	{
		super(id, "sockets:OR_0", "sockets:OR_1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Redstone OR";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Outputs an external redstone signal");
		l.add("when the OR function is satisfied");
		l.add("based on its internal inputs");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS control inputs");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS latche inputs");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new ItemStack(SocketsMod.module, 1, 17));
	}
	
	@Override
	public void updateOutput(SocketTileAccess ts, SideConfig config)
	{
		int meta = 0;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) meta = 1;
			if(config.rsLatch[i] && ts.getRSLatch(i)) meta = 1;
		}
		
		config.meta = meta;
		
	}

}