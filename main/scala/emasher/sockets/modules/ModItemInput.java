package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Facing;
import net.minecraftforge.common.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModItemInput extends SocketModule
{

	public ModItemInput(int id)
	{
		super(id, "sockets:itemInput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Item Input";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Accepts items from adjacent automation");
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
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "b", Character.valueOf('i'), Item.ingotIron, Character.valueOf('h'), Block.trapdoor,
				Character.valueOf('u'), Block.trapdoor, Character.valueOf('b'), SocketsMod.blankSide);
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
	
	/*@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		int xo = ts.xCoord + side.offsetX;
		int yo = ts.yCoord + side.offsetY;
		int zo = ts.zCoord + side.offsetZ;
		
		TileEntity t = ts.worldObj.getBlockTileEntity(xo, yo, zo);
		
		if(t != null && t instanceof TileEntityHopper)
		{	
			TileEntityHopper th = (TileEntityHopper)t;
			
			boolean canIntake = true;
			
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i] && ts.getRSControl(i)) canIntake = false;
				if(config.rsLatch[i] && ts.getRSLatch(i)) canIntake = false;
			}
			
			int direction = BlockHopper.getDirectionFromMetadata(ts.worldObj.getBlockMetadata(xo, yo, zo));
			if(ForgeDirection.getOrientation(direction).getOpposite() == side && canIntake)
			{
				for (int i = 0; i < th.getSizeInventory(); ++i)
	            {
	                if (th.getStackInSlot(i) != null)
	                {
	                    ItemStack itemstack = th.getStackInSlot(i).copy();
	                    itemstack.stackSize = 1;
	                    if(config.inventory != -1)
	                    {
	                    	int added = ts.addItemInternal(itemstack, true, config.inventory);
	                    	
	                    	itemstack.stackSize = th.getStackInSlot(i).stackSize - added;
	                    	if(itemstack.stackSize <= 0) itemstack = null;
	                    	
	                    	th.setInventorySlotContents(i, itemstack);
	                    }
	                }
	            }
			}
		}
	}*/
}
