package emasher.sockets.modules;

import java.util.List;

import buildcraft.api.power.PowerHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;

public class ModSolar extends SocketModule
{

	public ModSolar(int id)
	{
		super(id, "sockets:solarPanel");
	}

	@Override
	public String getLocalizedName()
	{
		return "Solar Panel";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Generates power when exposed to sunlight");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_AQUA + "Generates 5 RF/t");
		l.add("Can only be placed on the top of a socket");
		l.add("Requires sunlight to operate");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "sss", "sps", "sbs", Character.valueOf('s'), Block.daylightSensor, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('u'), Block.blockDiamond, Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 7));
	}
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side != ForgeDirection.UP) return false;
		return true;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(ts.worldObj.getBlockLightValue(ts.xCoord, ts.yCoord + 1, ts.zCoord) > 14 && ts.getMaxEnergyStored() - ts.getEnergyStored() >= 5)
		{
				
			if(side == ForgeDirection.UP) ts.addEnergy(5, false);
		}
	}

}
