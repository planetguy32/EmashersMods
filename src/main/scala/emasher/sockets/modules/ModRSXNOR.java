package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
//import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModRSXNOR extends RSGateModule
{

	public ModRSXNOR(int id)
	{
		super(id, "sockets:XNOR_0");
	}

	@Override
	public String getLocalizedName()
	{
		return "Redstone XNOR";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Outputs an external redstone signal");
		l.add("when the XNOR function is satisfied");
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
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "rtr", "trt", " b ", Character.valueOf('t'), Blocks.redstone_torch, Character.valueOf('r'), Items.redstone,
				Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 17));
	}
	
	@Override
	public void updateOutput(SocketTileAccess ts, SideConfig config)
	{
		int meta = 0;
		boolean done = false;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					if(meta == 1) done = true;
					meta = 1;
				}
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					if(meta == 1) done = true;
					meta = 1;
				}
			}
		}
		
		if(done) meta = 0;
		
		if(meta == 0) meta = 1;
		else meta = 0;
		
		config.meta = meta;
		
	}

}
