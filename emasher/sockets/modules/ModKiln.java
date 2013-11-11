package emasher.sockets.modules;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModKiln extends SocketModule
{

	public ModKiln(int id)
	{
		super(id, "sockets:kiln");
	}

	@Override
	public String getLocalizedName()
	{
		return "Kiln";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Turns Ground Limestone into Quicklime");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_AQUA + "Requires 10 MJ/t");
		l.add("The multi-block structure is 3x7x3");
		l.add("See wiki for further instructions...");
	}
	
	@Override
	public void addRecipe()
	{	
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "nln", "rrr", "pbp", Character.valueOf('n'), "ingotNickel", Character.valueOf('l'), "blockNickel",
				Character.valueOf('r'), Item.blazeRod, Character.valueOf('p'), EmasherCore.psu, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		if(side == ForgeDirection.UP || side == ForgeDirection.DOWN) return false;
		return true;
	}
	
	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		config.meta = 0;
		ts.sideInventory.setInventorySlotContents(side.ordinal(), new ItemStack(1, 1, 0));
	}
	
	@Override
	public void onRemoved(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		ts.sideInventory.setInventorySlotContents(side.ordinal(), null);
		int xo = ts.xCoord + side.offsetX;
		int yo = ts.yCoord;
		int zo = ts.zCoord + side.offsetZ;
		if(ts.worldObj.getBlockId(xo, yo, zo) == SocketsMod.groundLimestone.blockID)
		{
			ts.worldObj.setBlockMetadataWithNotify(xo, yo, zo, 0, 3);
		}
	}
	
	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		config.meta++;
		
		if(config.meta % 10 == 0 && isValidKiln(ts, side))
		{
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord;
			int zo = ts.zCoord + side.offsetZ;
			if(ts.worldObj.getBlockId(xo, yo, zo) == SocketsMod.groundLimestone.blockID)
			{
				if(ts.getEnergyStored() >= 1000)
				{
					ItemStack stack = ts.sideInventory.getStackInSlot(side.ordinal());
					ts.worldObj.setBlockMetadataWithNotify(xo, yo, zo, 1, 3);
					if(stack.stackSize >= 32)
					{
						ts.worldObj.setBlockToAir(xo, yo, zo);
						this.dropItemsOnSide(ts, side, xo, yo, zo, new ItemStack(SocketsMod.dusts, ts.worldObj.rand.nextInt(3) + 1, 0));
						stack.stackSize = 1;
					}
					else
					{
						stack.stackSize++;
					}
					
					ts.useEnergy(1000, false);
				}
				else
				{
					ts.worldObj.setBlockMetadataWithNotify(xo, yo, zo, 0, 3);
				}
			}
		}
		
		if(config.meta >= 400)
		{
			config.meta = 0;
			//System.out.println("check");
			
			int y = ts.yCoord + 4;
			
			if(checkMultiBlock(config, ts, side))
			{
				this.setMeta(ts.worldObj, ts, side, 5);
				ItemStack stack = ts.sideInventory.getStackInSlot(side.ordinal());
				stack.setItemDamage(1);
			}
			else
			{
				this.setMeta(ts.worldObj, ts, side, 2);
				ItemStack stack = ts.sideInventory.getStackInSlot(side.ordinal());
				stack.setItemDamage(0);
			}
		}
	}
	
	private boolean checkMultiBlock(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		if(side == ForgeDirection.DOWN || side == ForgeDirection.UP) return false;
		int y = ts.yCoord;
		int x = ts.xCoord;
		int z = ts.zCoord;
		World w = ts.worldObj;
		
		if(! checkLayer(config, ts, side, y - 1)) return false;
		
		if(side.offsetX == 0)
		{
			if(! checkBlock(w, x - 1, y, z)) return false;
			if(! checkBlock(w, x + 1, y, z)) return false;
			if(! checkBlock(w, x - 1, y, z + side.offsetZ + side.offsetZ)) return false;
			if(! checkBlock(w, x + 1, y, z + side.offsetZ + side.offsetZ)) return false;
		}
		else
		{
			if(! checkBlock(w, x, y, z - 1)) return false;
			if(! checkBlock(w, x, y, z + 1)) return false;
			if(! checkBlock(w, x + side.offsetX + side.offsetX, y, z - 1)) return false;
			if(! checkBlock(w, x + side.offsetX + side.offsetX, y, z + 1)) return false;
		}
		
		y++;
		
		for(int i = 0; i < 5; i++)
		{
			if(! checkLayer(config, ts, side, y + i)) return false;
		}
		
		
		return true;
	}
	
	private boolean checkLayer(SideConfig config, SocketTileAccess ts, ForgeDirection side, int y)
	{
		int x = ts.xCoord;
		int z = ts.zCoord;
		World w = ts.worldObj;
		int meta;
		
		if(side.offsetX == 0)
		{
			int zo = z;
			for(int i = x - 1; i <= x + 1; i++)
			{
				meta = w.getBlockMetadata(i, y, zo);
				if(w.getBlockId(i, y, zo) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			}
			
			zo = z + side.offsetZ;
			
			meta = w.getBlockMetadata(x - 1, y, zo);
			if(w.getBlockId(x - 1, y, zo) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			meta = w.getBlockMetadata(x + 1, y, zo);
			if(w.getBlockId(x + 1, y, zo) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			
			zo += side.offsetZ;
			
			for(int i = x - 1; i <= x + 1; i++)
			{
				meta = w.getBlockMetadata(i, y, zo);
				if(w.getBlockId(i, y, zo) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			}
		}
		else
		{
			int xo = x;
			for(int i = z - 1; i <= z + 1; i++)
			{
				meta = w.getBlockMetadata(xo, y, i);
				if(w.getBlockId(xo, y, i) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			}
			
			xo = x + side.offsetX;
			
			meta = w.getBlockMetadata(xo, y, z - 1);
			if(w.getBlockId(xo, y, z - 1) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			meta = w.getBlockMetadata(xo, y, z + 1);
			if(w.getBlockId(xo, y, z + 1) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			
			xo += side.offsetX;
			
			for(int i = z - 1; i <= z + 1; i++)
			{
				meta = w.getBlockMetadata(xo, y, i);
				if(w.getBlockId(xo, y, i) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
			}
		}
		
		return true;
	}
	
	private boolean checkBlock(World w, int x, int y, int z)
	{
		int meta = w.getBlockMetadata(x, y, z);
		if(w.getBlockId(x, y, z) != EmasherCore.normalCube.blockID || (meta != 2 && meta != 5)) return false;
		
		return true;
	}
	
	private void setMeta(World w, SocketTileAccess ts, ForgeDirection side, int newMeta)
	{
		int x = ts.xCoord;
		int z = ts.zCoord;
		int y = ts.yCoord + 4;
		int meta;
		int oldMeta;
		if(newMeta == 2) oldMeta = 5;
		else oldMeta = 2;
		
		if(side.offsetX == 0)
		{
			int zo = z;
			for(int i = x - 1; i <= x + 1; i++)
			{
				meta = w.getBlockMetadata(i, y, zo);
				if(w.getBlockId(i, y, zo) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(i, y, zo, newMeta, 3);
			}
			
			zo = z + side.offsetZ;
			
			meta = w.getBlockMetadata(x - 1, y, zo);
			if(w.getBlockId(x - 1, y, zo) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(x - 1, y, zo, newMeta, 3);
			meta = w.getBlockMetadata(x + 1, y, zo);
			if(w.getBlockId(x + 1, y, zo) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(x + 1, y, zo, newMeta, 3);
			
			zo += side.offsetZ;
			
			for(int i = x - 1; i <= x + 1; i++)
			{
				meta = w.getBlockMetadata(i, y, zo);
				if(w.getBlockId(i, y, zo) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(i, y, zo, newMeta, 3);
			}
		}
		else
		{
			int xo = x;
			for(int i = z - 1; i <= z + 1; i++)
			{
				meta = w.getBlockMetadata(xo, y, i);
				if(w.getBlockId(xo, y, i) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(xo, y, i, newMeta, 3);
			}
			
			xo = x + side.offsetX;
			
			meta = w.getBlockMetadata(xo, y, z - 1);
			if(w.getBlockId(xo, y, z - 1) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(xo, y, z - 1, newMeta, 3);
			meta = w.getBlockMetadata(xo, y, z + 1);
			if(w.getBlockId(xo, y, z + 1) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(xo, y, z + 1, newMeta, 3);
			
			xo += side.offsetX;
			
			for(int i = z - 1; i <= z + 1; i++)
			{
				meta = w.getBlockMetadata(xo, y, i);
				if(w.getBlockId(xo, y, i) == EmasherCore.normalCube.blockID && meta == oldMeta) w.setBlockMetadataWithNotify(xo, y, i, newMeta, 3);
			}
		}
	}
	
	private boolean isValidKiln(SocketTileAccess ts, ForgeDirection side)
	{
		ItemStack stack = ts.sideInventory.getStackInSlot(side.ordinal());
		if(stack.getItemDamage() == 0) return false;
		return true;
	}
	
	public void dropItemsOnSide(SocketTileAccess ts, ForgeDirection side, int xo, int yo, int zo, ItemStack stack)
	{
		World worldObj = ts.worldObj;
		
		if (! worldObj.isRemote)
        {
            float f = 0.7F;
            double d0 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d1 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double d2 = (double)(worldObj.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            EntityItem entityitem = new EntityItem(worldObj, (double)xo + d0, (double)yo + d1, (double)zo + d2, stack);
            entityitem.delayBeforeCanPickup = 1;
            worldObj.spawnEntityInWorld(entityitem);
            //ts.extractItemInternal(true, config.inventory, ts.getStackInInventorySlot(config.inventory).stackSize);
            //ts.inventory.setInventorySlotContents(config.inventory, null);
        }
	}

}
