package emasher.sockets.modules;

import java.util.List;

import buildcraft.api.inventory.ISpecialInventory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModItemExtractor extends SocketModule
{

	public ModItemExtractor(int id)
	{
		super(id, "sockets:itemExtractor");
	}
	
	@Override
	public String getLocalizedName()
	{
		return "Item Extractor";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Pulls items from adjacent");
		l.add("inventories/machines/etc.");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Inventory to input to");
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "u", "b", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('h'), Blocks.hopper,
				Character.valueOf('u'), Blocks.trapdoor, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
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
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		int xo = ts.xCoord + side.offsetX;
		int yo = ts.yCoord + side.offsetY;
		int zo = ts.zCoord + side.offsetZ;
		
		TileEntity t = ts.getWorldObj().getTileEntity(xo, yo, zo);
		
		if(t != null && ! (t instanceof TileEntityHopper) && t instanceof IInventory && config.inventory != -1)
		{
			boolean allOff = true;
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i]) allOff = false;
				if(config.rsLatch[i]) allOff = false;
			}
			
			if(allOff)
			{
				ItemStack pulled = ts.pullItem(side, false);
				if(pulled != null)
				{
					int added = ts.addItemInternal(pulled, true, config.inventory);
					if(added > 0) ts.pullItem(side, true);
				}
				
				
			}
			
		}
	}
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsControl[index] && config.inventory != -1)
		{
			ItemStack pulled = ts.pullItem(side, false);
			if(pulled != null)
			{
				int added = ts.addItemInternal(pulled, true, config.inventory);
				if(added > 0) ts.pullItem(side, true);
			}
		}
	}
	
	@Override
	public void onRSLatchChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsLatch[index] && config.inventory != -1)
		{
			ItemStack pulled = ts.pullItem(side, false);
			if(pulled != null)
			{
				int added = ts.addItemInternal(pulled, true, config.inventory);
				if(added >0) ts.pullItem(side, true);
			}
		}
	}


    @SideOnly(Side.CLIENT)
    public ItemStack getItemToRender(SocketTileAccess ts, SideConfig config, ForgeDirection side)
    {
        if(config.inventory != -1) return ts.getStackInInventorySlot(config.inventory);
        return null;
    }

    @Override
    public void onInventoryChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add)
    {
        if(index == config.inventory)
        {
            ts.sendClientInventorySlot(index);
        }
    }
}
