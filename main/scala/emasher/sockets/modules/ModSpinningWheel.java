package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModSpinningWheel extends SocketModule
{

	public ModSpinningWheel(int id)
	{
		super(id, "sockets:spinningWheel", "sockets:spinningWheelActive");
	}

	@Override
	public String getLocalizedName()
	{
		return "Spinning Wheel";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Spins wool into string");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Input inventory");
		l.add(SocketsMod.PREF_YELLOW + "Outputs to Machine Output");
		l.add(SocketsMod.PREF_AQUA + "Requires 10 RF/t");
		l.add("Cannot be installed on a socket with other machines");
	}
	
	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " hs", "sws", "sb ", Character.valueOf('s'), Item.stick, Character.valueOf('h'), EmasherCore.psu,
				Character.valueOf('w'), "plankWood", Character.valueOf('b'), SocketsMod.blankSide));
	}
	
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
		if(config.inventory >= 0 && config.inventory <= 2)
		{
			if(ts.sideInventory.getStackInSlot(side.ordinal()) == null)
			{
				if(ts.getStackInInventorySlot(config.inventory) != null)
				{
					ItemStack toIntake = ts.getStackInInventorySlot(config.inventory);
					
					ItemStack product = null;
					if(toIntake.itemID == Block.cloth.blockID)product = new ItemStack(Item.silk, 4, 0);
					
					if(product != null)
					{
						ts.extractItemInternal(true, config.inventory, 1);
						ts.sideInventory.setInventorySlotContents(side.ordinal(), product.copy());
						config.meta = 180;
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
				int num = ts.forceOutputItem(ts.sideInventory.getStackInSlot(side.ordinal()));
				ts.sideInventory.getStackInSlot(side.ordinal()).stackSize -= num;
				if(ts.sideInventory.getStackInSlot(side.ordinal()).stackSize <= 0) ts.sideInventory.setInventorySlotContents(side.ordinal(), null);
				
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

}
