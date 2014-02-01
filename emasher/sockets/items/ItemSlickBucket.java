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

	
	/*@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
        double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
        double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
        MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);
        
        if (var12 == null)
        {
            return par1ItemStack;
        }
        else if (var12.typeOfHit == EnumMovingObjectType.TILE)
        {
        	int var13 = var12.blockX;
            int var14 = var12.blockY;
            int var15 = var12.blockZ;
            
            int BlockID = par2World.getBlockId(var13, var14, var15);
            
            
        	if (var12.sideHit == 0)
            {
                --var14;
            }

            if (var12.sideHit == 1)
            {
                ++var14;
            }

            if (var12.sideHit == 2)
            {
                --var15;
            }

            if (var12.sideHit == 3)
            {
                ++var15;
            }

            if (var12.sideHit == 4)
            {
                --var13;
            }

            if (var12.sideHit == 5)
            {
                ++var13;
            }
            
            int i = par2World.getBlockId(var13, var14, var15);

            if ((i == 0 || (Block.blocksList[i] != null && Block.blocksList[i].isBlockReplaceable(par2World, var13, var14, var15))) &&  !(Block.blocksList[BlockID] instanceof IFluidHandler))
            {
                //par2World.playSoundEffect((double)var13 + 0.5D, (double)var14 + 0.5D, (double)var15 + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
                par2World.setBlock(var13, var14, var15, SocketsMod.blockSlickwater.blockID);
                
                if(! par3EntityPlayer.capabilities.isCreativeMode)
                {
                	par1ItemStack.stackSize--;
                	return (new ItemStack(Item.bucketEmpty, 1, 0));
                }
                
            }
            
        }
        
        return par1ItemStack;
	}*/

	
}