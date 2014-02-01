package emasher.sockets;

import buildcraft.api.inventory.ISpecialInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileSocketAssembler extends TileEntity implements ISpecialInventory
{
	InventoryBasic inv;
	public static final int NUM_SLOTS = 18;
	
	public TileSocketAssembler()
	{
		inv = new InventoryBasic("Socket Assembler", true, NUM_SLOTS);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		
		NBTTagList itemList = new NBTTagList();
		
		for(int i = 0; i < NUM_SLOTS; i++)
		{
			
			if(inv.getStackInSlot(i) != null)
			{
				NBTTagCompound itemCompound = new NBTTagCompound();
				itemCompound.setInteger("slot", i);
				inv.getStackInSlot(i).writeToNBT(itemCompound);
				itemList.appendTag(itemCompound);
			}
		}
		
		data.setTag("items", itemList);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		
		NBTTagList itemList = data.getTagList("items");
		
		if(itemList != null) for(int i = 0; i < itemList.tagCount(); i++)
	    {
	    	NBTTagCompound itemCompound = (NBTTagCompound) itemList.tagAt(i);
	    	int slot = itemCompound.getInteger("slot");
	    	inv.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(itemCompound));
	    }
	}
	
	/*
	 * IInventory
	 */

	@Override
	public int getSizeInventory()
	{
		return inv.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i)
	{
		return inv.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j)
	{
		return inv.decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i)
	{
		return inv.getStackInSlotOnClosing(i);
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		inv.setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInvName()
	{
		return inv.getInvName();
	}

	@Override
	public boolean isInvNameLocalized()
	{
		return inv.isInvNameLocalized();
	}

	@Override
	public int getInventoryStackLimit()
	{
		return inv.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return inv.isUseableByPlayer(entityplayer);
	}

	@Override
	public void openChest()
	{
		inv.openChest();
	}

	@Override
	public void closeChest()
	{
		inv.closeChest();
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack)
	{
		return inv.isItemValidForSlot(i, itemstack);
	}

	@Override
	public int addItem(ItemStack stack, boolean doAdd, ForgeDirection from)
	{
		return 0;
	}

	@Override
	public ItemStack[] extractItem(boolean doRemove, ForgeDirection from, int maxItemCount) {
		return null;
	}
}
