package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Facing;
import net.minecraftforge.common.util.ForgeDirection;
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
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "b", Character.valueOf('i'), Items.iron_ingot, Character.valueOf('h'), Blocks.trapdoor,
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
