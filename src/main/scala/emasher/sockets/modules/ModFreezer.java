package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.sockets.SocketsMod;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;

public class ModFreezer extends SocketModule
{
	private static final FluidStack waterStack = new FluidStack(FluidRegistry.WATER, 1000);
	
	public ModFreezer(int id)
	{
		super(id, "sockets:freezer");
	}

	@Override
	public String getLocalizedName()
	{
		return "Freezer";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Turns water from an internal");
		l.add("tank into ice blocks");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Input tank");
		l.add(SocketsMod.PREF_GREEN + "Output inventory");
		l.add(SocketsMod.PREF_AQUA + "Requires 160 RF/operation");
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "wpw", " b ", Character.valueOf('w'), Items.bucket, Character.valueOf('p'), EmasherCore.psu, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		config.meta++;
		
		if(config.meta >= 80)
		{
			config.meta = 0;
			
			if(ts.getEnergyStored() > 160)
			{
				if(config.tank >= 0 && config.tank < 3)
				{
					FluidStack taken = ts.drainInternal(config.tank, 1000, false);
					
					if(taken != null && taken.amount >= 1000 && taken.isFluidEqual(waterStack))
					{
						if(config.inventory >= 0 && config.inventory < 3)
						{
							if(ts.addItemInternal(new ItemStack(Blocks.ice), false, config.inventory) == 1)
							{
								ts.drainInternal(config.tank, 1000, true);
								ts.addItemInternal(new ItemStack(Blocks.ice), true, config.inventory);
								ts.useEnergy(160, false);
							}
						}
					}
				}
			}
		}
	}
}
