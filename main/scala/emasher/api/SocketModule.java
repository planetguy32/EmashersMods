package emasher.api;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.client.renderer.*;
import net.minecraft.util.Icon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class SocketModule
{
	/*
	 * The class SocketModule should be thought of similarly to classes such as Block, Item, and Fluid. It's mostly a collection of
	 * hook methods that define the behavior of a specific module. Many of the hooks are quite obscure, so in most cases, you will only
	 * have to override a few of them in order to define the module's desired behavior.
	 * 
	 * You will almost always only have one instance of this class, similar to how you almost always only have one instance of a Block
	 * class. Because of this, you will never store data specific to a given module on a given socket in this class.
	 * That data should be stored in the SideConfig instance, which you will have access to in methods which it is needed.
	 * 
	 * You will see that many of the methods have 3 common parameters, a SocketTileAccess object, which allows you to access certain
	 * methods in the tile entity of the socket that a module is currently attached to (See SocketTileAccess.java),
	 * a ForgeDirection object specifying what side of the socket the module is attacked to, as well as a SideConfig object which
	 * stores what individual indicators are set to for a given side of a socket, as well as 32 bits of metadata. The SideConfig object
	 * is reset by the socket itself when module is removed or replaced so you do not have to worry about this.
	 * 
	 * You should also keep in mind if you need to store more data (for instance, if the module is a processing machine)
	 * that a socket has an ItemStack independent of the 3 internal inventories for each of the six sides. You can access this
	 * ItemStack through the field sideInventory in SocketTile Access. Just be sure that you're using the correct slot for the side
	 * of the socket you're on. They are indexed by Minecraft standard direction indexes, so ForgeDirection.ordinal() is useful
	 * for determining this. Keep in mind that ItemStacks can have NBT data, so you could potentially store quite a bit of data this way.
	 * However, if you have data that needs to be accessed FAST then you will most likely be better off mapping it to the 32 bits in
	 * SideConfig.meta. If you want to store small amounts of data, you can also try storing it in the fields of SideConfig that specify
	 * indicator states. You should, however, only do this if the module does not make that indicator visible (the socket remote only
	 * works if an indicator is visible, so you know it won't get screwed up that way).
	 * 
	 * Also keep in mind that, many of these hooks are only called server side (with a few obvious exceptions).
	 * If you need certain data sent to the client for rendering, look at the packet handling methods in SocketTileAccess.
	 */
	
	//The unique ID of the module
	public int moduleID;
	
	//String representations of each of the textures that must be loaded
	public String[] textureFiles;
	
	/**
	 * Constructor for class SocketModule
	 * 
	 * @param id 
	 * 		The Module's Unique ID
	 * @param textureFiles 
	 * 		A list of string representations of texture files that must be loaded
	 */
	public SocketModule(int id, String ... textureFiles)
	{
		moduleID = id;
		this.textureFiles = textureFiles;
	}
	
	/**
	 * Called during init so that a recipe can be added for this module
	 */
	public void addRecipe() {}
	
	/**
	 * Get the US English localized name for this module
	 * 
	 * @return a String representing the name
	 */
	public abstract String getLocalizedName();
	
	/**
	 * Used to add a tooltip to a module
	 */
	public void getToolTip(List<Object> l) {}
	
	/**
	 * Used to add configuration instructions
	 */
	
	public void getIndicatorKey(List<Object> l) {}
	
	/**
	 * 
	 * @return true if the module has the stated indicator, false otherwise
	 */
	public boolean hasTankIndicator() { return false; }
	public boolean hasInventoryIndicator() { return false; }
	public boolean hasRSIndicator() { return false; }
	public boolean hasLatchIndicator() { return false; }
	
	/**
	 * Internal use only
	 */
	public final boolean hasIndicator(int index)
	{
		switch(index)
		{
		case 0: return hasTankIndicator();
		case 1: return hasInventoryIndicator();
		case 2: return hasRSIndicator();
		case 3: return hasLatchIndicator();
		}
		
		return false;
	}
	
	/**
	 * This is called server side EVERY tick (so be careful what you do here).
	 */
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side) {}
	
	/**
	 * Returns an integer specifying which texture (out of the ones specified by the constructor, in that order, starting at 0) you want
	 * rendered on the side of the socket based on the current state.
	 */
	public int getCurrentTexture(SideConfig config) { return 0; }
	public int getCurrentTexture(SideConfig config, SocketTileAccess ts, ForgeDirection side) { return getCurrentTexture(config); }
	
	/**
	 * Used to render overlays on top of the module's texture (such as the timer's progress bar) SocketTileAccess.getTexture() may
	 * come in handy here.
	 */
	@SideOnly(Side.CLIENT)
	public Icon[] getAdditionalOverlays(SocketTileAccess ts, SideConfig config, ForgeDirection side) { return new Icon[] {}; }
	
	/**
	 * For some reason Minecraft flips the textures on the bottom of blocks. Because certain additional overlays would be displayed
	 * incorrectly because of this, if this method returns true, overlays on the bottom will be flipped as well.
	 */
	@SideOnly(Side.CLIENT)
	public boolean flipBottomOverlay() { return false; }
	
	/**
	 * With some modules, such as the Inventory Interface, it may be necessary to render a String on a module.
	 * Override this method if you need to do so. You should however return null rather than an empty string
	 * if there is no text to render.
	 */
	@SideOnly(Side.CLIENT)
	public String getTextToRender(SocketTileAccess ts, SideConfig config, ForgeDirection side) { return null; }
	
	/**
	 * Specifies an ItemStack to be rendered on a module (used by the Inventory Interface and Charger)
	 */
	@SideOnly(Side.CLIENT)
	public ItemStack getItemToRender(SocketTileAccess ts, SideConfig config, ForgeDirection side) { return null; }
	
	/**
	 * Specifies an internal tank to render fluid from (this is used by the Tank Interface).
	 * You should only return numbers in the range [-1, 2]. With -1 specifying to do nothing, and {0, 1, 2} specifying
	 * that the appropriate tank should be rendered.
	 */
	@SideOnly(Side.CLIENT)
	public int getTankToRender(SocketTileAccess ts, SideConfig config, ForgeDirection side) { return -1; }
	
	/**
	 * This gives you full access to the socket's renderer.
	 */
	@SideOnly(Side.CLIENT)
	public void doCustomRendering(SocketTileAccess ts, SideConfig config, ForgeDirection side, Tessellator tess, RenderBlocks bRenderer) {}
	
	/**
	 * Returns true iff the module can be placed on the socket on a given side
	 */
	
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side) { return true; }
	
	/**
	 * Returns true iff this module is considered a machine
	 */
	
	public boolean isMachine() { return false; }
	
	/**
	 * Called when a module is placed on a socket
	 */
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side) {}
	
	/**
	 * Called when a module is removed from a socket
	 */
	public void onRemoved(SocketTileAccess ts, SideConfig config, ForgeDirection side) {}
	
	/**
	 * Called when a player uses a socket remote to configure the side that THIS module is installed on
	 */
	public void indicatorUpdated(SocketTileAccess ts, SideConfig config, ForgeDirection side) {}
	
	/**
	 * Called when there's a block update on any side of the socket
	 */
	public void onAdjChange(SocketTileAccess ts, SideConfig config, ForgeDirection side) {}
	
	/**
	 * Called when there's a block update on THIS side of the socket
	 */
	public void onAdjChangeSide(SocketTileAccess ts, SideConfig config, ForgeDirection side) {}
	
	/**
	 * Called only on the server when a player right clicks on this side of the socket
	 */
	public void onSideActivated(SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player) {}
	public void onSideActivated(SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player,
			float subX, float subY, float subZ)
	{
		onSideActivated(ts, config, side, player);
	}
	
	/**
	 * Called both server and client side when a player right clicks on this side of the socket
	 * Keep in mind that the config may not have been synchronized
	 * This is useful if you want to show a GUI
	 */
	public void onSideActivatedClient(SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player) {}
	public void onSideActivatedClient(SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player,
			float subX, float subY, float subZ)
	{
		onSideActivatedClient(ts, config, side, player);
	}
	
	/**
	 * Called when a player right clicks on this side of the socket while holding a socket remote set to its 'white' generic mode
	 */
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side) {}
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side,
			float subX, float subY, float subZ)
	{
		onGenericRemoteSignal(ts, config, side);
	}
	
	/**
	 * If a player right clicks on this side of the socket while holding a dye, the dye will be used up, and this method will be called
	 * Note that this IS fully compatible with the OreDictionary
	 */
	public void changeColour(int colour, SideConfig config, SocketTileAccess ts, ForgeDirection side) {}
	
	/**
	 * Called when an entity walks on the socket, regardless of where this module is placed on it.
	 */
	public void onEntityWalkOn(SocketTileAccess ts, SideConfig config, ForgeDirection side, Entity entitiy) {}
	
	/**
	 * Return true if Forge compatible fluid pipes can connect to this side of the socket
	 */
	public boolean isFluidInterface() { return false; }
	
	/**
	 * Returns true if under certain circumstances this module accepts fluids
	 */
	public boolean canInsertFluid() { return false; }
	
	/**
	 * Returns true if under certain circumstances fluid can be extracted from this module
	 */
	public boolean canExtractFluid() { return false; }
	
	/**
	 * Called when automation attempts to insert fluid into this module. Return the amount of fluid in mB accepted
	 * doFill will be false when the caller only wants to know how much fluid would be inserted rather than actually inserting it,
	 * it will be true otherwise.
	 */
	public int fluidFill(FluidStack fluid, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side) { return 0; }
	
	/**
	 * Return a FluidStack specifying what fluid is extracted when an attempt to extract fluid is made
	 * amount is the maximum amount of fluid to extract
	 * doExtract will be false when the caller only wants to know what fluid would be extracted rather than actually extracting it,
	 * it will be true otherwise.
	 */
	public FluidStack fluidExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts) { return null; }
	
	/**
	 * Called when the contents of any internal tank change
	 * index refers to the tank index and add will be true iff fluid was added to the tank
	 */
	public void onTankChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add) {}
	
	/**
	 * Should return true if this module can receive GasCraft gas from chimneys and other blocks that only work with
	 * GasCraft gas
	 */
	public boolean isGasInterface() { return false; }
	
	/**
	 * Similar to fluidFill but for GasCraft chimneys and other blocks that only work with GasCraft gas.
	 * Returns the amount of gas used.
	 */
	public int gasFill(FluidStack gas, SocketTileAccess ts) { return 0; }
	
	/**
	 * Returns true iff this module can interact with pipes and other item automation.
	 */
	public boolean isItemInterface() { return false; }
	
	/**
	 * Returns true iff under certain circumstances items can be inserted into this module via automation
	 */
	public boolean canInsertItems() { return false; }
	
	/**
	 * Returns true iff under certain circumstances items can be extracted from this module via automation
	 */
	public boolean canExtractItems() { return true; }
	
	/**
	 * Returns true iff given a specific configuration, items can be inserted directly into an internal inventory via automation
	 */
	public boolean canDirectlyInsertItems(SideConfig config, SocketTileAccess ts) { return false; }
	
	/**
	 * Returns true iff given a specific configuration, items can be extracted directly from an internal inventory via automation
	 */
	public boolean canDirectlyExtractItems(SideConfig config, SocketTileAccess ts) { return false; }
	
	/**
	 * Called when automation attempts to insert items into this module. Returns the number of items accepted.
	 * doFill will be false when the caller only wants to know how many items would be accepted rather than actually inserting them,
	 * it will be true otherwise.
	 */
	public int itemFill(ItemStack item, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side) { return 0; }
	
	/**
	 * Called when automation attempts to extract items from this module. Returns an ItemStack representing what was extracted.
	 * doExtract will be false when the caller only wants to know what items would be extracted rather than actually extracting them,
	 * it will be true otherwise.
	 */
	public ItemStack itemExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts) { return null; }
	
	/**
	 * Called whenever the contents of any internal inventory change.
	 * index refers to the inventory that was changed, while add is true iff items were added to said inventory
	 */
	public void onInventoryChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add) {}
	
	
	/**
	 * Returns true iff this module can connect to power cables, pipes, and conduits.
	 */
	public boolean isEnergyInterface(SideConfig config) { return false; }
	
	/**
	 *
	 * @param simulate true iff only a simulation
	 * @return the amount of energy used
	 */
	public int receiveEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts) { return 0; }
	
	/**
	 *
	 * @param maxExtract energy requested
	 * @param simulate true iff only a simulation
	 * @return the amount of energy offered
	 */
	public int extractEnergy(int maxExtract, boolean simulate, SideConfig config, SocketTileAccess ts) { return 0; }
	
	/**
	 * Returns true iff this module can connect to redstone compatible blocks
	 */
	public boolean isRedstoneInterface() { return false; }
	
	/**
	 * Called whenever, due to other blocks in the world, the redstone state of the adjacent block either turns off or on
	 * on is true iff the redstone signal turned on
	 */
	public void updateRestone(boolean on, SideConfig config, SocketTileAccess ts) {}
	
	/**
	 * Returns true iff this module is currently outputing a restone signal to the world.
	 */
	public boolean isOutputingRedstone(SideConfig config, SocketTileAccess ts) { return false; }
	
	/**
	 * Called whenever an internal redstone channel changes.  index refers the redstone channel that changed,
	 * on is true iff the signal changed from off to on.
	 */
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on) {}
	
	/**
	 * Called whenever an internal redstone latch changes state.  index refers the redstone latch channel that changed,
	 * on is true iff the signal changed from off to on.
	 */
	public void onRSLatchChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on) {}
	
	/**
	 * Called whenever an already configured socket is placed
	 */
	public void onSocketPlaced(SideConfig config, SocketTileAccess ts, ForgeDirection side) {}

    public boolean canModuleBeDyed() { return false; }
}
