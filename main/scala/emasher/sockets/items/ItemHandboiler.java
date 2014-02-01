package emasher.sockets.items;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import emasher.core.*;
import emasher.core.item.ItemEmasherGeneric;
import emasher.sockets.SocketsMod;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;

public class ItemHandboiler extends ItemEmasherGeneric
{
	
	public ItemHandboiler(int id, String texture, String name)
	{
		super(id, "sockets:handboiler", "handboiler");
		this.setMaxDamage(64);
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setMaxStackSize(1);
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par3World, EntityPlayer par3EntityPlayer)
    {
        float f = 1.0F;
        double d0 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)f;
        double d1 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)f + 1.62D - (double)par3EntityPlayer.yOffset;
        double d2 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)f;
        boolean flag = true;
        MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par3World, par3EntityPlayer, flag);

        if (movingobjectposition == null)
        {
            return par1ItemStack;
        }
        else
        {

            if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE)
            {
                int par4 = movingobjectposition.blockX;
                int par5 = movingobjectposition.blockY;
                int par6 = movingobjectposition.blockZ;
                boolean doDamage = false;
                
                for(int i = par4 - 2; i < par4 + 3; i++)
                {
                	for(int j = par5 - 2; j < par5 + 3; j++)
                	{
                		for(int k = par6 - 2; k < par6 + 3; k++)
                		{
                			int id = par3World.getBlockId(i, j, k);
                			
                			if(id == Block.waterStill.blockID)
                			{
                				par3World.setBlock(i, j, k, 0);
                				ItemHandboiler.fizz(par3World, i, j, k);
                                doDamage = true;
                			}
                			
                			if(id == Block.waterMoving.blockID || id == Block.snow.blockID)
                			{
                				par3World.setBlock(i, j, k, 0);
                				ItemHandboiler.fizz(par3World, i, j, k);
                                doDamage = true;
                			}
                			
                			if(id == Block.ice.blockID || id == Block.blockSnow.blockID)
                			{
                				par3World.setBlock(i, j, k, Block.waterStill.blockID);
                				ItemHandboiler.fizz(par3World, i, j, k);
                                doDamage = true;

                			}
                			
                			//The hand boiler should only smelt stuff when the player is sneaking, and should never smelt stone (red power)
                			if(id != 0 && id != Block.stone.blockID && par3EntityPlayer.isSneaking()) 
                			{
                				ItemStack is = new ItemStack(id, 1, par3World.getBlockMetadata(i, j, k));
                				
                				ItemStack product = FurnaceRecipes.smelting().getSmeltingResult(is);
                				
                				if(product != null)
                				{
                					
                					if(Item.itemsList[product.itemID] != null && ! (product.getItem() instanceof ItemBlock))
                					{
	                					product = ItemStack.copyItemStack(product);
	                						
	                					EntityItem drop = new EntityItem(par3World, i, j, k, product);
	                						
	                					if(product.hasTagCompound())
	                					{
	                						drop.getEntityItem().setTagCompound((NBTTagCompound)product.getTagCompound().copy());
	                					}
	                						
	                					if(! par3World.isRemote) par3World.spawnEntityInWorld(drop);
	                					ItemHandboiler.fizz(par3World, i, j, k);
	                					doDamage = true;
                						par3World.setBlockToAir(i, j, k);
                					}
                					else if(product.itemID < Block.blocksList.length && Block.blocksList[product.itemID] != null && Block.blocksList[product.itemID] instanceof Block)
                					{
                						if(id != Block.sand.blockID || SocketsMod.smeltSand)
                						{
                							par3World.setBlock(i, j, k, product.itemID, product.getItemDamage(), 2);
                							ItemHandboiler.fizz(par3World, i, j, k);
                        					doDamage = true;
                						}
                					}
                					
                					
                				}
                			}
                			
                			
                		}
                	}
                }
                
                if(doDamage)
                {
                	par1ItemStack.damageItem(1, par3EntityPlayer);
                }

            }
            else if (movingobjectposition.entityHit != null)
            {
                movingobjectposition.entityHit.setFire(10);
                //System.out.println("Entity Hit");
                par1ItemStack.damageItem(1, par3EntityPlayer);
            }

            return par1ItemStack;
        }
    }
	
	public static void fizz(World world, int x, int y, int z)
	{
		world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.fizz", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
		for(int i = 0; i < 10; i++)
		{
			world.spawnParticle("smoke", (double)x + itemRand.nextDouble() - 0.5, y + itemRand.nextDouble() - 0.5, z + itemRand.nextDouble() - 0.5, 0, 0, 0);
		}
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
        if (entity.worldObj.isRemote)
        {
            return false;
        }
        if (entity instanceof EntityLiving)
        {
        	entity.setFire(10);
        	stack.damageItem(1, (EntityLiving)entity);
        	fizz(entity.worldObj, (int)entity.posX, (int)entity.posY, (int)entity.posZ);
        }
        return false;
    }
}
