package emasher.sockets.modules;

import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
//import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ModItemDisplay extends SocketModule
{

	public ModItemDisplay(int id)
	{
		super(id, "sockets:itemDisplay", "sockets:itemDisplayFull", "sockets:itemDisplayStack", "sockets:itemDisplayFullStack");
	}

	@Override
	public String getLocalizedName()
	{
		return "Inventory Interface";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Displays and allows for manual");
		l.add("interaction with an internal");
		l.add("inventory");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Inventory to display");
		l.add(SocketsMod.PREF_WHITE + "Toggle stack mode");
	}
	
	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ggg", "slr", " b ", Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('s'), Items.glowstone_dust,
				Character.valueOf('l'), "dyeLime", Character.valueOf('r'), Items.redstone, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public int getCurrentTexture(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.inventory != -1 && ts.getStackInInventorySlot(config.inventory) != null)
		{
			if(config.meta == 0) return 1;
			return 3;
		}
		if(config.meta ==  0) return 0;
		return 2;
	}
	
	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.meta == 0) config.meta = 1;
		else config.meta = 0;
		
		ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public String getTextToRender(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory == -1)
		{
			return null;
		}
		
		if(ts.getStackInInventorySlot(config.inventory) != null)
		{
			return ts.getStackInInventorySlot(config.inventory).stackSize + "";
		}
		
		return null;
	}
	
	/*@Override
	@SideOnly(Side.CLIENT)
	public Icon[] getAdditionalItemOverlays(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory != -1)
		{
			ItemStack is = ts.inventory.getStackInSlot(config.inventory);
			if(is != null)
			{
				return new Icon[] { is.getItem().getIcon(is, 0) };
			}
			else
			{
				return new Icon[] {};
			}
		}
		else
		{
			return new Icon[] {};
		}
	}*/
	
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
	
	@Override
	public void onSideActivated(SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player)
	{
		ItemStack is = player.getCurrentEquippedItem();
		int added = 0;
		boolean flag = false;
		if(config.inventory != -1)
		{
			if(is != null)
			{
				added = ts.addItemInternal(is, true, config.inventory);
				is.stackSize -= added;
			}
			
			ItemStack stack = ts.getStackInInventorySlot(config.inventory);
			
			if(stack != null && is != null)
			{
				flag = ! stack.isItemEqual(is);
			}
			else
			{
				flag = stack != null;
			}
			
			if(added == 0 && stack != null && flag)
			{
				int xo = ts.xCoord + side.offsetX;
				int yo = ts.yCoord + side.offsetY;
				int zo = ts.zCoord + side.offsetZ;
				
				int dropped = 0;
				
				if(config.meta == 1)
				{
					dropped = Math.min(stack.stackSize, stack.getMaxStackSize());
				}
				else
				{
					dropped = 1;
				}
				
				ItemStack dropStack = stack.copy();
				dropStack.stackSize = dropped;
				
				dropItemsOnSide(ts, config, side, xo, yo, zo, dropStack);
				ts.extractItemInternal(true, config.inventory, dropped);
				//stack.stackSize -= dropped;
				//if(stack.stackSize <= 0) ts.inventory.setInventorySlotContents(config.inventory, null);
			}
			ts.sendClientInventorySlot(config.inventory);
		}
	}
	
	public void dropItemsOnSide(SocketTileAccess ts, SideConfig config, ForgeDirection side, int xo, int yo, int zo, ItemStack stack)
	{
		if (! ts.getWorldObj().isRemote)
        {
            float f = 0.7F;
            double d0 = (double)(ts.getWorldObj().rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(ts.getWorldObj().rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(ts.getWorldObj().rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(ts.getWorldObj(), (double)xo + d0, (double)yo + d1, (double)zo + d2, stack);
            entityitem.delayBeforeCanPickup = 1;
            ts.getWorldObj().spawnEntityInWorld(entityitem);
        }
	}
	
	@Override
	public void indicatorUpdated(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory != -1) ts.sendClientInventorySlot(config.inventory);
	}
	
}
