package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.RSGateModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModRSAND extends RSGateModule
{

	public ModRSAND(int id)
	{
		super(id, "sockets:AND_0", "sockets:AND_1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Redstone AND";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Outputs an external redstone signal");
		l.add("when the AND function is satisfied");
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
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " t ", "trt", " b ", Character.valueOf('t'), Block.torchRedstoneActive, Character.valueOf('r'), Item.redstone,
				Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 17));
	}
	
	@Override
	public void updateOutput(SocketTileAccess ts, SideConfig config)
	{
		int meta = 1;
		boolean none = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				none = false;
				if(! ts.getRSControl(i)) meta = 0;
			}
			
			if(config.rsLatch[i])
			{
				none = false;
				if(! ts.getRSLatch(i)) meta = 0;
			}
		}
		
		if(none) meta = 0;
		config.meta = meta;
		
	}

}
