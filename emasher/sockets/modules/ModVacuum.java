package emasher.sockets.modules;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModVacuum extends SocketModule
{

	public ModVacuum(int id)
	{
		super(id, "sockets:vacuum");
	}

	@Override
	public String getLocalizedName()
	{
		return "Vacuum";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Sucks up items within a 3 block radius into");
		l.add("its configured inventory on an internal RS pulse");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Inventory to input items to");
		l.add(SocketsMod.PREF_RED + "RS control channel");
		l.add(SocketsMod.PREF_AQUA + "Uses 10 RF/Operation");
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " s ", "ipi", " b ", Character.valueOf('i'), Item.ingotIron, Character.valueOf('s'), Block.hopperBlock,
				Character.valueOf('p'), Block.pistonBase, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsControl[index] && config.inventory >= 0 && config.inventory <= 2 && ts.getEnergyStored() >= 10)
		{
			double x = ts.xCoord + side.offsetX;
			double y = ts.yCoord + side.offsetY;
			double z = ts.zCoord + side.offsetZ;
			
			double xMin = x;
			double yMin = y;
			double zMin = z;
			
			if(side.offsetX == 0)
			{
				xMin--;
				x++;
			}
			
			if(side.offsetY == 0)
			{
				yMin--;
				y++;
			}
			
			if(side.offsetZ == 0)
			{
				zMin--;
				z++;
			}
			
			ts.useEnergy(10, false);
			
			List l = ts.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getAABBPool().getAABB(xMin, yMin, zMin, x + 1, y + 1, z + 1));
			for(Object o : l)
			{
				if(o instanceof EntityItem)
				{
					EntityItem e = (EntityItem)o;
					ItemStack item = e.getEntityItem();
					
					int num = ts.addItemInternal(item, true, config.inventory);
					item.stackSize -= num;
					if(item.stackSize == 0)
					{
						ts.worldObj.removeEntity(e);
					}
				}
			}
		}
	}

}
