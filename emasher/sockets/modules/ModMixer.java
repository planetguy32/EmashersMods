package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.Loader;
import emasher.api.MixerRecipeRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.api.MixerRecipeRegistry.MixerRecipe;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModMixer extends SocketModule
{
	public ModMixer(int id)
	{
		super(id, "sockets:mixer", "sockets:mixerActive");
	}

	@Override
	public String getLocalizedName()
	{
		return "Mixer";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Used to mix items and fluids");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Input Tank");
		l.add(SocketsMod.PREF_GREEN + "Input Inventory");
		l.add(SocketsMod.PREF_YELLOW + "Outputs to Machine Output");
		l.add(SocketsMod.PREF_AQUA + "Requires 10 f/tick");
		l.add("Cannot be installed on a socket with other machines");
	}
	
	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " h ", "udu", " b ", Character.valueOf('h'), Block.hopperBlock, Character.valueOf('u'), Item.bucketEmpty,
				Character.valueOf('d'), Block.dispenser, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public boolean isMachine() { return true; }
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		for(int i = 0; i < 6; i++)
		{
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(i));
			if(m != null && m.isMachine()) return false;
		}
		
		return true;
	}
	
	@Override
	public void onRemoved(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.sideInventory.setInventorySlotContents(side.ordinal(), null);
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		boolean updateClient = false;
		if(config.tank >= 0 && config.tank <= 2 && config.inventory >= 0 && config.inventory <= 2)
		{
			if(ts.sideInventory.getStackInSlot(side.ordinal()) == null)
			{
				if(ts.getFluidInTank(config.tank) != null && ts.getStackInInventorySlot(config.inventory) != null)
				{
					FluidStack toIntakeFluid = ts.getFluidInTank(config.tank);
					ItemStack toIntake = ts.getStackInInventorySlot(config.inventory);
					FluidStack product = null;
					
					MixerRecipe r = MixerRecipeRegistry.getRecipe(toIntake, toIntakeFluid);
					if(r != null) product = r.getOutput();
					
					
					if(product != null && r.getFluidInput().amount <= toIntakeFluid.amount)
					{
						ts.extractItemInternal(true, config.inventory, 1);
						ts.drainInternal(config.tank, r.getFluidInput().amount, true);
						
						ts.sideInventory.setInventorySlotContents(side.ordinal(), fluidToItem(product));
						config.meta = 40;
						config.rsControl[0] = false;
						updateClient = true;
					}
				}
			}
			else if(ts.useEnergy(10, true)>= 10 && config.meta > 0)
			{
				ts.useEnergy(10, false);
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
