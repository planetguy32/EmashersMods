package emasher.sockets.modules;

import java.util.List;

import ic2.api.energy.tile.IEnergyAcceptor;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;
import buildcraft.api.power.IPowerReceptor;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModMultiOutput extends SocketModule
{

	public ModMultiOutput(int id)
	{
		super(id, "sockets:multiOutput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Multi Output";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Combined Fluid, Item, and Energy output modules");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Tank to output from");
		l.add(SocketsMod.PREF_GREEN + "Inventory to output from");
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new ItemStack(SocketsMod.module, 1, 2), new ItemStack(SocketsMod.module, 1, 5), new ItemStack(SocketsMod.module, 1, 8));
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public boolean isEnergyInterface(SideConfig config) { return true; }
	
	@Override
	public boolean isFluidInterface() { return true; }
	
	@Override
	public boolean canExtractFluid() { return true; }
	
	@Override
	public boolean isItemInterface() { return true; }
	
	@Override
	public boolean canExtractItems() { return true; }
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{		
			EnergyInsert(ts, config, side);
			FluidInsert(ts, config, side);
			ItemInsert(ts, config, side);
	}
	
	private void EnergyInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		boolean allOff = true;
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					ts.outputEnergy(1000, side);
					return;
				}
				allOff = false;
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					ts.outputEnergy(1000, side);
					return;
				}
				allOff = false;
			}
		}
		
		if(allOff)
		{
			ts.outputEnergy(1000, side);
			
		}
	}
	
	private void FluidInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.tank < 0 || config.tank > 2) return;
		
		boolean allOff = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					ts.tryInsertFluid(config.tank, side);
					return;
				}
				allOff = false;
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					ts.tryInsertFluid(config.tank, side);
					return;
				}
				allOff = false;
			}
		}
		
		if(allOff) ts.tryInsertFluid(config.tank, side);
	}
	
	private void ItemInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory < 0 || config.inventory > 2) return;
		
		boolean allOff = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					if(ts.tryInsertItem(ts.getStackInInventorySlot(config.inventory), side)) ts.extractItemInternal(true, config.inventory, 1);
					return;
				}
				allOff = false;
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					if(ts.tryInsertItem(ts.getStackInInventorySlot(config.inventory), side)) ts.extractItemInternal(true, config.inventory, 1);
					return;
				}
				allOff = false;
			}
		}
		
		if(allOff) if(ts.tryInsertItem(ts.getStackInInventorySlot(config.inventory), side)) ts.extractItemInternal(true, config.inventory, 1);
	}
	
	@Override
	public FluidStack fluidExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts)
	{
		if(config.tank != -1) return ts.drainInternal(config.tank, amount, doExtract);
		return null;
	}
	
	@Override
	public ItemStack itemExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts)
	{
		if(config.inventory != -1) return ts.extractItemInternal(doExtract, config.inventory, amount);
		return null;
	}
	
	@Override
	public int extractEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
	{
		boolean allOff = true;
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						return ts.useEnergy(amount, simulate);
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						return ts.useEnergy(amount, simulate);
					}
					allOff = false;
				}
			}
			
			if(allOff)
			{
				return ts.addEnergy(amount, simulate);
				
			}
		
		return 0;
	}
	
	@Override
	public boolean canDirectlyExtractItems(SideConfig config, SocketTileAccess ts)
	{
		if(config.inventory < 0 || config.inventory > 2) return false;
		
		boolean allOff = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					return true;
				}
				allOff = false;
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					return true;
				}
				allOff = false;
			}
		}
		
		return allOff;
	}

}
