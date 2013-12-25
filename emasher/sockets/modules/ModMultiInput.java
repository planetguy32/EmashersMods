package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockHopper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModMultiInput extends SocketModule
{

	public ModMultiInput(int id)
	{
		super(id, "sockets:multiInput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Multi Input";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Combined Fluid, Item, and Energy input modules");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Tank to input to");
		l.add(SocketsMod.PREF_GREEN + "Inventory to input to");
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new ItemStack(SocketsMod.module, 1, 1), new ItemStack(SocketsMod.module, 1, 4), new ItemStack(SocketsMod.module, 1, 7));
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
	public int receiveEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
	{
		boolean allOff = true;
		
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						return ts.addEnergy(amount, simulate);
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						return ts.addEnergy(amount, simulate);
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
	public boolean isFluidInterface() { return true; }
	
	@Override
	public boolean canInsertFluid() { return true; }
	
	
	@Override
	public int fluidFill(FluidStack fluid, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		boolean canIntake = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
			if(config.rsLatch[i] && ts.getRSLatch(i)) canIntake = false;
		}
		
		if(canIntake)
		{
			if(config.tank != -1) return ts.fillInternal(config.tank, fluid, doFill);
		}
		
		return 0;
	}
	
	@Override
	public boolean isItemInterface() { return true; }
	
	@Override
	public boolean canInsertItems() { return true; }
	
	@Override
	public boolean canDirectlyInsertItems(SideConfig config, SocketTileAccess ts)
	{
		if(config.inventory < 0 || config.inventory > 2) return false;
		
		boolean canIntake = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
			if(config.rsLatch[i] && ts.getRSLatch(i)) canIntake = false;
		}
		
		return canIntake;
	}
	
	@Override
	public int itemFill(ItemStack item, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		boolean canIntake = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
			if(config.rsLatch[i] && ts.getRSLatch(i)) canIntake = false;
		}
		
		if(canIntake)
		{
			if(config.inventory != -1) return ts.addItemInternal(item, doFill, config.inventory);
		}
		
		return 0;
	}
	
	

}
