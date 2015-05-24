package emasher.api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public abstract class SocketTileAccess extends TileEntity
{
	/*
	 * SocketTileAccess is the super class of the socket's tile entity. It's designed as an interface for modules to interact
	 * with the socket they are attached to as the full tile entity cannot be included as part of this API.
	 * 
	 * Please keep in mind that because SocketTile access extends TileEntity, you have access to the worldObj, xCoord, yCoord, zCoord
	 * fields as well as all other public fields in TileEntity through an instance of this class.
	 * 
	 * If there is something missing in this class that you need to make a module work, feel free to either ask me to add it
	 * or submit a pull request on GitHub.
	 */
	
	//Used for machines to hold an ItemStack temporarily
	//Use only the slot side.ordinal() or you will create problems
	public InventoryBasic sideInventory;

    public boolean dead = false;
	
	/**
	 * Get a texture that was registered for a particular module
	 * 
	 * @param texture the texture index
	 * @param moduleID the ID of the module
	 * @return Returns an Icon from the standard block texture sheet representing the texture you requested
	 */
	@SideOnly(Side.CLIENT)
	public abstract IIcon getTexture(int texture, int moduleID);
	
	/**
	 * Notify the client that fields in a SideConfig have changed
	 * @param side the side of the socket that you want to notify the client about
	 */
	public abstract void sendClientSideState(int side);
	
	/**
	 * Notify the client that the contents of an internal inventory have changed
	 * @param inv the inventory that you want to notify the client about
	 */
	public abstract void sendClientInventorySlot(int inv);
	
	/**
	 * Notify the client that the contents of an internal tank have changed
	 * @param tank the tank that you want to notify the client about
	 */
	public abstract void sendClientTankSlot(int tank);
	
	/**
	 * Tell an adjacent block that the socket has updated in some way (probably for a redstone signal change)
	 * @param side the block to notify is on
	 */
	public abstract void updateAdj(ForgeDirection side);
	
	/**
	 * Increment the setting blue indicator for a module
	 * @param side the side of the socket to increment the setting on
	 */
	public abstract void nextTank(int side);
	
	/**
	 * Increment the setting greens indicator for a module
	 * @param side the side of the socket to increment the setting on
	 */
	public abstract void nextInventory(int side);
	
	/**
	 * Increment the setting red indicator for a module
	 * @param side the side of the socket to increment the setting on
	 */
	public abstract void nextRS(int side);
	
	/**
	 * Increment the setting purple indicator for a module
	 * @param side the side of the socket to increment the setting on
	 */
	public abstract void nextLatch(int side);
	
	/**
	 * Get the SocketModule instance for the module on a specific side of the socket
	 * @param direction the side of the socket you want to know about
	 * @return the SocketModule in question
	 */
	public abstract SocketModule getSide(ForgeDirection direction);
	
	/**
	 * Get the SideConfig for a specific side of the socket
	 * @param direction the side of the socket you want to know about
	 * @return the SideConfig in question
	 */
	public abstract SideConfig getConfigForSide(ForgeDirection direction);
	
	
	/**
	 * Get the current state of an internal redstone channel
	 * @param channel the channel in question
	 * @return true iff the chanel is on
	 */
	public abstract boolean getRSControl(int channel);
	
	/**
	 * Change the current state of an internal redstone channel
	 * Be sure to call it again if another module turns off the channel when it should still be on according to your module
	 * @param channel the channel to modify
	 * @param on should be true if you want to turn the channel on, false if you want to turn it off
	 */
	public abstract void modifyRS(int channel, boolean on);
	
	/**
	 * Get the current state of an internal redstone latch
	 * @param channel the latch in question
	 * @return true iff the latch is on
	 */
	public abstract boolean getRSLatch(int channel);
	
	/**
	 * Change the current state of an internal redstone latch
	 * @param channel the latch to modify
	 * @param on should be true if you want to turn the channel on, false if you want to turn it off
	 */
	public abstract void modifyLatch(int channel, boolean on);
	
	/**
	 * Check to see if the socket is recieving a restone signal from a particular side
	 * @param side the side in question
	 * @return true iff the socket is recieving a restone signal from the direction specified
	 */
	public abstract boolean getSideRS(ForgeDirection side);
	
	
	/**
	 * Add an ItemStack to an internal inventory
	 * 
	 * @param stack the ItemStack to add
	 * @param doAdd should be true iff you want the items to actually be added
	 * @param inv the inventory to add the items to 
	 * @return the number of items accepted
	 */
	public abstract int addItemInternal(ItemStack stack, boolean doAdd, int inv);
	
	/**
	 * Extract an ItemStack from an internal inventory
	 * 
	 * @param doRemove should be true iff you want the items to actually be extracted
	 * @param inv the inventory you want to extract items from
	 * @param maxItemCount the maximum number of items to extract
	 * @return an ItemStack representing what was extracted, or null if the inventory was empty
	 */
	public abstract ItemStack extractItemInternal(boolean doRemove, int inv, int maxItemCount);
	
	/**
	 * Get the ItemStack in a particular inventory
	 * Keep in mind this will return a pointer to the actual content, not a copy
	 * 
	 * @param inv the inventory in question
	 * @return an ItemStack representing the contents of a particular inventory
	 */
	public abstract ItemStack getStackInInventorySlot(int inv);
	
	/**
	 * Directly set the contents of an internal inventory
	 * @param inventory the inventory in question
	 * @param stack the new contents of the inventory in question
	 */
	public abstract void setInventoryStack(int inventory, ItemStack stack);
	
	/**
	 * Interface to ISpecialInventory's addItem. You will almost never have to use this
	 */
	public abstract int addItem(ItemStack stack, boolean doAdd, ForgeDirection direction);
	
	/**
	 * Try to insert an ItemStack of size 1 into an adjacent inventory if one exists
	 * Supports IInventory, ISidedInventory, and ISpecialInventory
	 * 
	 * @param stack the ItemStack to insert
	 * @param side the side the adjacent inventory is on
	 * @return true iff the item was inserted
	 */
	public abstract boolean tryInsertItem(ItemStack stack, ForgeDirection side);
	
	/**
	 * Try to extract an item from an adjacent inventory if one exists
	 * Supports IInventory, ISidedInventory, and ISpecialInventory
	 * 
	 * @param side the side to attempt to extract from
	 * @param doPull true iff you actually want to extract the item
	 * @return an ItemStack representing the item extracted, null if none was found
	 */
	public abstract ItemStack pullItem(ForgeDirection side, boolean doPull);
	
	/**
	 * Attempt to output items with the first available Machine Output module
	 * @param stack the ItemStack to output
	 * @param doOutput should the item actually be output
	 * @return the number of items accepted
	 */
	public abstract int forceOutputItem(ItemStack stack, boolean doOutput);
	public int forceOutputItem(ItemStack stack) { return forceOutputItem(stack, true); }
	
	/**
	 * Attempt to insert fluid into an internal tank
	 * 
	 * @param tank the tank in question
	 * @param resource the FluidStack to insert
	 * @param doFill true iff you actually want to insert the fluid
	 * @return the amount of fluid accepted
	 */
	public abstract int fillInternal(int tank, FluidStack resource, boolean doFill);
	
	/**
	 * Attempt to drain fluid from an internal tank
	 * 
	 * @param tank the tank in question
	 * @param maxDrain the maximum amount of fluid to drain in mB
	 * @param doDrain true iff you actually want to drain the fluid
	 * @return a FluidStack representing the fluid that was drained
	 */
	public abstract FluidStack drainInternal(int tank, int maxDrain, boolean doDrain);
	
	
	public abstract void tryExtractFluid(int tank, ForgeDirection side, int volume);
	public abstract void tryInsertFluid(int tank, ForgeDirection side);
	public abstract FluidStack getFluidInTank(int tank);
	public abstract int forceOutputFluid(FluidStack stack, boolean doOutput);
	public int forceOutputFluid(FluidStack stack) { return forceOutputFluid(stack, true); } 
	
	//Energy
	
	public abstract void outputEnergy(int maxEnergy, ForgeDirection side);
	public abstract int getMaxEnergyStored();
	public abstract int getEnergyStored();
	public abstract void setMaxEnergyStored(int newMax);
	public abstract int useEnergy(int amnt, boolean simulate);
	public abstract int addEnergy(int amnt, boolean simulate);

    public World getWorldObj()
    {
        return worldObj;
    }
}
