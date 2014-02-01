package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSPulseModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModPressurePlate extends RSPulseModule
{

	public ModPressurePlate(int id)
	{
		super(id, "sockets:pressurePlate", "sockets:pressurePlateActive");
	}

	@Override
	public String getLocalizedName()
	{
		return "Pressure Plate";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Creates an internal redstone");
		l.add("pulse when stepped on");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS channels to pulse");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS latches to toggle");
		l.add("Can only be placed on the top of a socket");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "i", "b", Character.valueOf('i'), Block.pressurePlateStone, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public boolean isOutputingRedstone(SideConfig config, SocketTileAccess ts)
	{
		return false;
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side != ForgeDirection.UP) return false;
		return true;
	}
	
	@Override
	public void onEntityWalkOn(SocketTileAccess ts, SideConfig config, ForgeDirection side, Entity entitiy)
	{
		if(config.meta == 0)
		{
			config.meta = 1;
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					ts.modifyRS(i, true);
				}
				
				if(config.rsLatch[i])
				{	
					ts.modifyLatch(i, ! ts.getRSLatch(i));
				}
			}
		}
	}
	
}
