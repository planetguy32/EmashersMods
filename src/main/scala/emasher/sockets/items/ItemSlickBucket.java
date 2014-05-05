package emasher.sockets.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraft.block.material.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.*;

public class ItemSlickBucket extends ItemBucket
{
	
	public ItemSlickBucket(int id) 
	{
		super(id, SocketsMod.fluidSlickwater.getBlockID());
		
		setCreativeTab(SocketsMod.tabSockets);
		setMaxStackSize(1);
		setUnlocalizedName("slickwaterBucket");
		this.setContainerItem(Item.bucketEmpty);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		itemIcon = ir.registerIcon("sockets:slickbucket");
	}

}