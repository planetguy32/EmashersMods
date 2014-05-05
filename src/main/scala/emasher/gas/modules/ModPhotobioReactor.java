package emasher.gas.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Loader;
import emasher.api.PhotobioReactorRecipeRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.api.PhotobioReactorRecipeRegistry.PhotobioReactorRecipe;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModPhotobioReactor extends SocketModule
{
	public ModPhotobioReactor(int id)
	{
		super(id, "gascraft:photobioReactor", "gascraft:photobioReactorActive");
	}

	@Override
	public String getLocalizedName()
	{
		return "Photobioreactor";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Converts fluids to other fluids when combined with");
		l.add("items that are sensitive to light");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Input tank");
		l.add(SocketsMod.PREF_GREEN + "Input inventory");
		l.add(SocketsMod.PREF_YELLOW + "Outputs to Machine Output");
		l.add("Cannot be installed on a socket with other machines");
		l.add("Requires sunlight to operate");
	}
	
	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ggg", "u u", " b ", Character.valueOf('h'), Block.hopperBlock, Character.valueOf('u'), Item.bucketEmpty,
				Character.valueOf('g'), Block.thinGlass, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public void onRemoved(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.sideInventory.setInventorySlotContents(side.ordinal(), null);
	}
	
	@Override
	public boolean isMachine() { return true; }
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side != ForgeDirection.UP) return false;
		
		for(int i = 0; i < 6; i++)
		{
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(i));
			if(m != null && m.isMachine()) return false;
		}
		
		return true;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		boolean updateClient = false;
		if(config.tank >= 0 && config.tank <= 2 && config.inventory >= 0 && config.inventory <= 2 && side == ForgeDirection.UP)
		{
			if(ts.sideInventory.getStackInSlot(side.ordinal()) == null)
			{
				if(ts.getFluidInTank(config.tank) != null && ts.getStackInInventorySlot(config.inventory) != null)
				{
					FluidStack toIntakeFluid = ts.getFluidInTank(config.tank);
					ItemStack toIntake = ts.getStackInInventorySlot(config.inventory);
					FluidStack product = null;
					
					PhotobioReactorRecipe r = PhotobioReactorRecipeRegistry.getRecipe(toIntake, toIntakeFluid);
					if(r != null) product = r.getOutput();
					
					
					if(product != null && r.getFluidInput().amount <= toIntakeFluid.amount)
					{
						ts.extractItemInternal(true, config.inventory, 1);
						ts.drainInternal(config.tank, r.getFluidInput().amount, true);
						
						ts.sideInventory.setInventorySlotContents(side.ordinal(), fluidToItem(product));
						config.meta = 400;
						config.rsControl[0] = false;
						updateClient = true;
					}
				}
			}
			else if(ts.worldObj.getBlockLightValue(ts.xCoord, ts.yCoord + 1, ts.zCoord) > 14 && config.meta > 0)
			{
				config.meta--;
				if(config.meta == 0) updateClient = true;
				if(! config.rsControl[0] && config.meta > 0)
				{
					config.rsControl[0] = true;
					updateClient = true;
				}
			}
			else
			{
				if(config.rsControl[0])
				{
					config.rsControl[0] = false;
					updateClient = true;
				}
			}
			
			if(config.meta == 0 && ts.sideInventory.getStackInSlot(side.ordinal()) != null)
			{
				
				FluidStack f = itemToFluid(ts.sideInventory.getStackInSlot(side.ordinal()));
				int num = ts.forceOutputFluid(f);
				if(num < f.amount)
				{
					ts.sideInventory.setInventorySlotContents(side.ordinal(), new ItemStack(f.fluidID, 1, f.amount - num));
				}
				else
				{
					ts.sideInventory.setInventorySlotContents(side.ordinal(), null);
				}
				
			}
			if(updateClient) ts.sendClientSideState(side.ordinal());
		}
	}
	
	@Override
	public int getCurrentTexture(SideConfig config)
	{
		if(config.meta == 0 || ! config.rsControl[0]) return 0;
		return 1;
	}
	
	private ItemStack fluidToItem(FluidStack f)
	{
		return new ItemStack(f.fluidID, 1, f.amount);
	}
	
	private FluidStack itemToFluid(ItemStack i)
	{
		return new FluidStack(i.itemID, i.getItemDamage());
	}
}
