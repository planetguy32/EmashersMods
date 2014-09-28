package emasher.sockets.modules;

import java.util.List;

import cofh.api.energy.IEnergyContainerItem;
//import ic2.api.item.ElectricItem;
//import ic2.api.item.IElectricItem;
//import ic2.api.item.IElectricItemManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
//import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;
import emasher.sockets.BlockSocket;
import emasher.sockets.SocketsMod;

public class ModCharger extends SocketModule
{
	public ModCharger(int id)
	{
		super(id, "sockets:charger", "sockets:discharger");
	}
	
	@Override
	public String getLocalizedName()
	{
		return "Charger";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Used to charge electric items");
		l.add("Compatible with RF and EU");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Inventory containing item to charge");
		l.add(SocketsMod.PREF_AQUA + "Item dependent power requirement");
	}

    @SideOnly(Side.CLIENT)
    public String getInternalTexture(SocketTileAccess ts, SideConfig config, ForgeDirection side) { return "sockets:inner_charger"; }

    @SideOnly(Side.CLIENT)
    public String[] getAllInternalTextures() { return new String[] {"sockets:inner_charger"}; }

	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ggg", "slr", " b ", Character.valueOf('g'), Blocks.glass_pane, Character.valueOf('s'), Items.glowstone_dust,
				Character.valueOf('l'), "dyeLime", Character.valueOf('r'), Items.gold_ingot, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasInventoryIndicator() { return true; }
	
	@Override
	public int getCurrentTexture(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.tank == -1) return 0;
		else return 1;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon[] getAdditionalOverlays(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		return new IIcon[] {((BlockSocket)SocketsMod.socket).chargeInd[config.meta]};
	}
	
	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.tank == -1) config.tank = 0;
		else config.tank = -1;
		
		ts.sendClientSideState(side.ordinal());
	}
	
	@SideOnly(Side.CLIENT)
	public ItemStack getItemToRender(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory != -1) return ts.getStackInInventorySlot(config.inventory);
		return null;
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(config.inventory != -1 && ts.getStackInInventorySlot(config.inventory) != null)
		{
			ItemStack is = ts.getStackInInventorySlot(config.inventory);
			
			if(is.getItem() instanceof IEnergyContainerItem)
			{
				IEnergyContainerItem ieci = (IEnergyContainerItem)is.getItem();
				if(config.tank == -1)
				{
					int amnt = ieci.receiveEnergy(is, ts.useEnergy(100, true), false);
					ts.useEnergy(amnt, false);
				}
				else
				{
					int amnt = ts.addEnergy(ieci.extractEnergy(is, 100, true), false);
					ieci.extractEnergy(is, amnt, false);
				}
			}
//			else if(is.getItem() instanceof IElectricItem)
//			{
//				IElectricItem iei = (IElectricItem)is.getItem();
//				if(config.tank == -1)
//				{
//					int used = (int)ElectricItem.manager.charge(is, (ts.getEnergyStored() / 4), 3, false, false);
//					ts.useEnergy(used * 4, false);
//				}
//				else
//				{
//					int used = (int)ElectricItem.manager.discharge(is, ((ts.getMaxEnergyStored() - ts.getEnergyStored()) / 4), 3, false, false, false);
//					ts.addEnergy(used * 4, false);
//				}
//				updateMeta(ts, config, side);
//			}
		}
	}
	
	@Override
	public void onInventoryChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add)
	{
		if(index == config.inventory)
		{
			ts.sendClientInventorySlot(index);
			updateMeta(ts, config, side);
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
	
	public void updateMeta(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory != -1 && ts.getStackInInventorySlot(config.inventory) != null)
		{
			ItemStack is = ts.getStackInInventorySlot(config.inventory);
//			if(is.getItem() instanceof IElectricItem)
//			{
//				IElectricItem iei = (IElectricItem)is.getItem();
//				int maxCharge = (int)iei.getMaxCharge(is);
//				int currCharge = (int)ElectricItem.manager.getCharge(is);
//
//				int oldMeta = config.meta;
//				int newMeta = (int)(((float)currCharge/(float)maxCharge) * 12);
//				config.meta = newMeta;
//				if(oldMeta != newMeta) ts.sendClientSideState(side.ordinal());
//				return;
//			}
//			else
            if(is.getItem() instanceof IEnergyContainerItem)
			{
				IEnergyContainerItem ieci = (IEnergyContainerItem)is.getItem();
				int maxCharge = ieci.getMaxEnergyStored(is);
				int currCharge = ieci.getEnergyStored(is);
				int oldMeta = config.meta;
				int newMeta = (int)(((float)currCharge/(float)maxCharge) * 12);
				config.meta = newMeta;
				if(oldMeta != newMeta) ts.sendClientSideState(side.ordinal());
				return;
			}
		}
		boolean wasZero = config.meta == 0;
		config.meta = 0;
		if(! wasZero) ts.sendClientSideState(side.ordinal());
	}
	
	@Override
	public void indicatorUpdated(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.inventory != -1)
		{
			ts.sendClientInventorySlot(config.inventory);
		}
	}

	
}
