package emasher.sockets.modules;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;

public class ModItemEjector extends SocketModule
{
	public ModItemEjector(int id)
	{
		super(id, "sockets:itemEjector");
	}

	@Override
	public String getLocalizedName()
	{
		return "Item Ejector";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Ejects items into the world");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Inventory to pull from");
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "d", "b", Character.valueOf('d'), Block.dispenser, Character.valueOf('h'), Block.trapdoor,
				Character.valueOf('u'), Block.trapdoor, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	@Override
	public void onAdjChange(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		onInventoryChange(config, config.inventory, ts, side, true);
	}
	
	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		onInventoryChange(config, config.inventory, ts, side, true);
	}
	
	@Override
	public void indicatorUpdated(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		onInventoryChange(config, config.inventory, ts, side, true);
	}
	
	@Override
	public void onInventoryChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add)
	{	
		if(add)
		{
			if(config.inventory != index) return;
			if(config.inventory < 0 || config.inventory > 2) return;
			if(ts.getStackInInventorySlot(config.inventory) ==  null) return;
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			int id = ts.worldObj.getBlockId(xo, yo, zo);
			if(! ts.worldObj.isAirBlock(xo, yo, zo)) return;
			
			boolean allOff = true;
			
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						dropItemsOnSide(ts, config, side, xo, yo, zo, ts.getStackInInventorySlot(config.inventory));
						return;
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						dropItemsOnSide(ts, config, side, xo, yo, zo, ts.getStackInInventorySlot(config.inventory));
						return;
					}
					allOff = false;
				}
			}
			
			if(allOff) dropItemsOnSide(ts, config, side, xo, yo, zo, ts.getStackInInventorySlot(config.inventory));
		}
	}
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
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
}
