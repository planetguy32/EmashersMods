package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModRSInput extends SocketModule
{

	public ModRSInput(int id)
	{
		super(id, "sockets:RSIN_0", "sockets:RSIN_1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Redstone Input";
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "r", "b", Character.valueOf('i'), Item.ingotIron, Character.valueOf('r'), Item.redstone,
				Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Keeps an RS control channel(s) turned on");
		l.add("while recieving an external redstone signal");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS control channels to activate");
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public int getCurrentTexture(SideConfig config) { return config.meta; }
	
	@Override
	public boolean isRedstoneInterface() { return true; }
	
	@Override
	public void updateRestone(boolean on, SideConfig config, SocketTileAccess ts)
	{
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(on)
				{
					ts.modifyRS(i, true);
					config.meta = 1;
				}
				else
				{
					ts.modifyRS(i, false);
					config.meta = 0;
				}
			}
		}
	}
	
	
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(! on)
		{
			if(config.rsControl[index] && ts.getSideRS(side))
			{
				ts.modifyRS(index, true);
			}
		}
	}
	
}
