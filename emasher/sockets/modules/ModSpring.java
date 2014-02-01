package emasher.sockets.modules;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModSpring extends SocketModule
{

	public ModSpring(int id)
	{
		super(id, "sockets:spring");
	}

	@Override
	public String getLocalizedName()
	{
		return "Spring";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Catapults a non-player entity adjacent to this");
		l.add("module and within one block away from");
		l.add("the socket on an internal redstone pulse");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS control channel");
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ipi", " b ", Character.valueOf('i'), Item.ingotIron,
				Character.valueOf('p'), Block.pistonBase, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsControl[index])
		{
			double x = ts.xCoord + side.offsetX;
			double y = ts.yCoord + side.offsetY;
			double z = ts.zCoord + side.offsetZ;
			
			List l = ts.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 1, z + 1));
			for(Object o : l)
			{
				if(o instanceof Entity)
				{
					Entity e = (Entity)o;
					double xm = side.offsetX;
					double ym = side.offsetY;
					double zm = side.offsetZ;
					
					e.motionX += xm;
					e.motionY += ym;
					e.motionZ += zm;
				}
			}
		}
	}

}
