package emasher.gas.modules;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.gas.EmasherGas;
import emasher.gas.tileentity.TileShaleResource;
import emasher.sockets.SocketsMod;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.*;

public class ModFracker extends SocketModule
{
	public ModFracker(int id)
	{
		super(id, "gascraft:fracker");
	}

	@Override
	public String getLocalizedName()
	{
		return "Fracker";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Inputs shale resource fluid into its output tank");
		l.add("when water or slickwater is pumped into its input tank");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Input tank");
		l.add(SocketsMod.PREF_GREEN + "Output tank");
		l.add("Must be placed on the bottom of a socket");
		l.add("Only works if the socket is directly above");
		l.add("a shale resource block");
		l.add("Slickwater doubles the output");
	}
	
	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "hih", "cdu", " b ", Character.valueOf('h'), Blocks.hopper, Character.valueOf('u'), Items.bucket,
				Character.valueOf('i'), Items.iron_ingot, Character.valueOf('c'), EmasherCore.circuit, Character.valueOf('d'), Items.diamond, Character.valueOf('b'), SocketsMod.blankSide));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "hih", "cdu", " b ", Character.valueOf('h'), Blocks.hopper, Character.valueOf('u'), Items.bucket,
				Character.valueOf('i'), "ingotAluminum", Character.valueOf('c'), EmasherCore.circuit, Character.valueOf('d'), Items.diamond, Character.valueOf('b'), SocketsMod.blankSide));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "hih", "cdu", " b ", Character.valueOf('h'), Blocks.hopper, Character.valueOf('u'), Items.bucket,
				Character.valueOf('i'), "ingotTin", Character.valueOf('c'), EmasherCore.circuit, Character.valueOf('d'), Items.diamond, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side == ForgeDirection.DOWN) return true;
		return false;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		config.meta++;
		if(config.meta >= 10)
		{
			config.meta = 0;
			
			if(config.tank >= 0 && config.tank < 3 && ts.getFluidInTank(config.tank) != null)
			{
				onTankChange(config, config.tank, ts, side, true);
			}
		}
	}
	
	@Override
	public void onTankChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add)
	{
		if(index == config.tank && add && side == ForgeDirection.DOWN && config.inventory >= 0 && config.inventory < 3)
		{
			FluidStack f = ts.getFluidInTank(index);
			
			if(f.isFluidEqual(new FluidStack(SocketsMod.fluidSlickwater, 1000)) || f.isFluidEqual(new FluidStack(FluidRegistry.WATER, 1000)))
			{
				
				int amntToDraw;
				boolean sw = false;
				
				if(f.isFluidEqual((new FluidStack(SocketsMod.fluidSlickwater, 1000))))
				{
					amntToDraw = 1000;
					sw = true;
				}
				else
				{
					amntToDraw = 200;
				}
				
				
				if(ts.getWorldObj().getBlock(ts.xCoord, ts.yCoord - 1, ts.zCoord) == EmasherGas.shaleResource && ts.getWorldObj().getBlockMetadata(ts.xCoord, ts.yCoord -1, ts.zCoord) != 2)
				{
					TileEntity te = ts.getWorldObj().getTileEntity(ts.xCoord, ts.yCoord - 1, ts.zCoord);
					if(te != null && te instanceof TileShaleResource)
					{
						TileShaleResource tsr = (TileShaleResource)te;
						
						FluidStack fs = ts.getFluidInTank(index);
						if(fs.amount >= 1000 && ts.fillInternal(config.inventory, tsr.drain(amntToDraw, false), false) == amntToDraw)
						{
							ts.drainInternal(index, 1000, true);
							FluidStack ext;
							if(sw)
							{
								ext = tsr.drain(amntToDraw, true);
							}
							else
							{
								ext = tsr.drain(amntToDraw * 2, true);
								if(ext != null) ext.amount = Math.min(ext.amount, 200);
							}
							
							if(ext != null) ts.fillInternal(config.inventory, ext, true);
						}
					}
				}
			}
			else if(f.isFluidEqual(new FluidStack(FluidRegistry.LAVA, 1000)))
			{
				int amntToDraw = 1000;
				
				if(ts.getWorldObj().getBlock(ts.xCoord, ts.yCoord -1, ts.zCoord) == EmasherGas.shaleResource && ts.getWorldObj().getBlockMetadata(ts.xCoord, ts.yCoord -1, ts.zCoord) == 2)
				{
					TileEntity te = ts.getWorldObj().getTileEntity(ts.xCoord, ts.yCoord - 1, ts.zCoord);
					if(te != null && te instanceof TileShaleResource)
					{
						TileShaleResource tsr = (TileShaleResource)te;
						
						FluidStack fs = ts.getFluidInTank(index);
						if(fs.amount >= 1000 && ts.fillInternal(config.inventory, tsr.drain(amntToDraw, false), false) == amntToDraw)
						{
							ts.drainInternal(index, 1000, true);
							FluidStack ext;
							ext = tsr.drain(amntToDraw, true);
							
							
							if(ext != null) ts.fillInternal(config.inventory, ext, true);
						}
					}
				}
				
			}
		}
	}
}
