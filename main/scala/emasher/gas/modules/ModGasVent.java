package emasher.gas.modules;
import java.util.List;

import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.sockets.SocketsMod;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import emasher.gas.block.*;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.*;
import emasher.gas.EmasherGas;

public class ModGasVent extends SocketModule
{
	public ModGasVent(int id)
	{
		super(id, "gascraft:gasVent");
	}

	@Override
	public String getLocalizedName()
	{
		return "Vent";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Releases gas from an internal tank");
		l.add("when there are 4000 mB in the tank");
		l.add("and it recieves an internal RS pulse");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Tank to output from");
		l.add(SocketsMod.PREF_RED + "RS control circuit");
	}
	
	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "civ", " b ", Character.valueOf('c'), EmasherCore.circuit, Character.valueOf('i'), Block.fenceIron,
				Character.valueOf('v'), Block.hopperBlock, Character.valueOf('b'), new ItemStack(SocketsMod.module, 1, 5)));
	}
	
	@Override
	public boolean hasTankIndicator() {return true; }
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsControl[index] && config.tank >= 0 && config.tank < 3)
		{
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			
			if(ts.worldObj.isAirBlock(xo, yo, zo))
			{
				FluidStack f = ts.getFluidInTank(config.tank);
				if(f != null && f.amount >= 4000)
				{
					Fluid fl = f.getFluid();
					int bID = fl.getBlockID();
					if(bID > 0 && bID < 4096)
					{
						if(Block.blocksList[bID] != null && Block.blocksList[bID] instanceof BlockGasGeneric)
						{
							ts.worldObj.setBlock(xo, yo, zo, bID);
							ts.drainInternal(config.tank, 4000, true);
						}
					}
				}
			}
		}
	}
}
