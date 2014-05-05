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

public class ModRSOutput extends RSGateModule
{

	public ModRSOutput(int id)
	{
		super(id, "sockets:RSOUT_0", "sockets:RSOUT_1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Redstone Output";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Outputs an external redstone signal when");
		l.add("When one or more of it's configured RS control");
		l.add("channels or latches is activated");
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
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "r", "b", Character.valueOf('i'), Item.ingotIron, Character.valueOf('r'), Item.redstoneRepeater,
				Character.valueOf('b'), SocketsMod.blankSide);
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
