package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModMachineOutput extends SocketModule
{

	public ModMachineOutput(int id)
	{
		super(id, "sockets:machineOutput", "sockets:machineOutputEject");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "d", "b", Character.valueOf('d'), Block.dispenser, Character.valueOf('h'), Block.hopperBlock,
				Character.valueOf('u'), Block.trapdoor, Character.valueOf('b'), SocketsMod.blankSide);
	}

	@Override
	public String getLocalizedName()
	{
		return "Machine Output";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Recieves item and fluid");
		l.add("output from certain machines");
		l.add("and places it in selected");
		l.add("inventory and/or tank");
		l.add("Outputs items and fluids into");
		l.add("adjacent tanks/inventories/pipes");
		l.add("as well as ejects items");
		l.add("into the world when configured");
		l.add("to do so");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Output tank");
		l.add(SocketsMod.PREF_GREEN + "Output inventory");
		l.add(SocketsMod.PREF_WHITE + "Configure if output ejects into the world");
		l.add("Only one can be installed per socket");
	}
	
	@Override
	public int getCurrentTexture(SideConfig config) { return config.meta; }
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		for(int i = 0; i < 6; i++)
		{
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(i));
			if(m.moduleID == this.moduleID) return false;
		}
		
		return true;
	}
	
	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		config.meta++;
		if(config.meta == 2) config.meta = 0;
		ts.sendClientSideState(side.ordinal());
		ts.updateAdj(side);
	}
	
	@Override
	public boolean isFluidInterface() { return true; }
	
	@Override
	public boolean canExtractFluid() { return true; }
	
	@Override
	public boolean isItemInterface() { return true; }
	
	@Override
	public boolean canExtractItems() { return true; }
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{		
			FluidInsert(ts, config, side);
			ItemInsert(ts, config, side);
			
			if(config.meta == 1)
			{
				if(config.inventory < 0 || config.inventory > 2) return;
				if(ts.getStackInInventorySlot(config.inventory) ==  null) return;
				int xo = ts.xCoord + side.offsetX;
				int yo = ts.yCoord + side.offsetY;
				int zo = ts.zCoord + side.offsetZ;
				int id = ts.worldObj.getBlockId(xo, yo, zo);
				if(! ts.worldObj.isAirBlock(xo, yo, zo)) return;
				
				dropItemsOnSide(ts, config, side, xo, yo, zo, ts.getStackInInventorySlot(config.inventory));
			}
	}
	
	private void FluidInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.tank < 0 || config.tank > 2) return;
		ts.tryInsertFluid(config.tank, side);
	}
	
	private void ItemInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory < 0 || config.inventory > 2) return;
		if(ts.tryInsertItem(ts.getStackInInventorySlot(config.inventory), side)) ts.extractItemInternal(true, config.inventory, 1);
	}
	
	public void dropItemsOnSide(SocketTileAccess ts, SideConfig config, ForgeDirection side, int xo, int yo, int zo, ItemStack stack)
	{
		if (! ts.worldObj.isRemote)
        {
            float f = 0.7F;
            double d0 = (double)(ts.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(ts.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(ts.worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(ts.worldObj, (double)xo + d0, (double)yo + d1, (double)zo + d2, stack.copy());
            entityitem.delayBeforeCanPickup = 1;
            ts.worldObj.spawnEntityInWorld(entityitem);
            ts.extractItemInternal(true, config.inventory, ts.getStackInInventorySlot(config.inventory).stackSize);
            //ts.inventory.setInventorySlotContents(config.inventory, null);
        }
	}
	
	@Override
	public boolean canDirectlyExtractItems(SideConfig config, SocketTileAccess ts)
	{
		if(config.inventory < 0 || config.inventory > 2) return false;
		
		boolean allOff = true;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i])
			{
				if(ts.getRSControl(i))
				{
					return true;
				}
				allOff = false;
			}
			
			if(config.rsLatch[i])
			{
				if(ts.getRSLatch(i))
				{
					return true;
				}
				allOff = false;
			}
		}
		
		return allOff;
	}

}
