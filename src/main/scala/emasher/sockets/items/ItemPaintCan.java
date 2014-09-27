package emasher.sockets.items;

import emasher.sockets.PacketHandler;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

//import emasher.sockets.PacketHandler;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import emasher.sockets.pipes.*;

public class ItemPaintCan extends Item
{
	private int paintColour;
	
	private static final double[][] colourTransforms = new double[][]{{0, 0, 0}, {1, 0, 0}, {0, 0.5, 0}, {0.5, 0.25, 0}, {0, 0, 1}, {0.5, 0, 1}, {0, 0.5, 1}, {0.8, 0.8, 0.8}, {1, 0.75, 1}, {0, 1, 0}, {1, 1, 0}, {0, 1, 1}, {1, 0, 1}, {1, 0.5, 0}, {1, 1, 1}};
	
	public ItemPaintCan(int paintColour)
	{
		super();
		this.paintColour = paintColour;
		this.setMaxDamage(64);
		//this.setIconIndex(32 + paintColour);
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setUnlocalizedName("paintCan");
		this.setHasSubtypes(true);
		this.setMaxStackSize(1);
	}
	
	public void registerIcons(IIconRegister registry)
	{
		this.itemIcon = registry.registerIcon("sockets:item" + (32 + paintColour));
	}
	
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
		return false;
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		Random rand = new Random(System.nanoTime());
		
		float var4 = 1.0F;
        double var5 = par3EntityPlayer.prevPosX + (par3EntityPlayer.posX - par3EntityPlayer.prevPosX) * (double)var4;
        double var7 = par3EntityPlayer.prevPosY + (par3EntityPlayer.posY - par3EntityPlayer.prevPosY) * (double)var4 + 1.62D - (double)par3EntityPlayer.yOffset;
        double var9 = par3EntityPlayer.prevPosZ + (par3EntityPlayer.posZ - par3EntityPlayer.prevPosZ) * (double)var4;
        MovingObjectPosition var12 = this.getMovingObjectPositionFromPlayer(par2World, par3EntityPlayer, true);
        
        if (var12 == null)
        {
            return par1ItemStack;
        }
        else if (var12.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
        	
        	int var13 = var12.blockX;
            int var14 = var12.blockY;
            int var15 = var12.blockZ;
            
            //System.out.println(var12.blockX + ", " + var12.blockY + ", " + var12.blockZ);
            
            //int BlockID = par2World.getBlockId(var13, var14, var15);
            Block b = par2World.getBlock(var13, var14, var15);
            ItemStack stack = new ItemStack(b, par2World.getBlockMetadata(var12.blockX, var12.blockY, var12.blockZ));
            
            int oreIDReq = OreDictionary.getOreID("plankWood");
            int oreIDThis = OreDictionary.getOreID(stack);

            if (oreIDReq == oreIDThis)
            {
            	if(b != SocketsMod.paintedPlanks || (b == SocketsMod.paintedPlanks && par2World.getBlockMetadata(var13, var14, var15) != this.paintColour))
            	{
	            	par2World.playSoundEffect((double)var13 + 0.5D, (double)var14 + 0.5D, (double)var15 + 0.5D, "step.cloth", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
	                
	                for(int i = 0; i < 16; i++)
	                {
	                	int met = 15 - this.paintColour;
	                	String id = "" + Block.getIdFromBlock(Blocks.wool);
	                	par2World.spawnParticle("tilecrack_" + id + "_" + met, (double)var13 + rand.nextDouble() - 0.5, var14 + rand.nextDouble() - 0.5, var15 + rand.nextDouble() - 0.5, 0, 0, 0);
	                }
	                
	    			par2World.setBlock(var13, var14, var15, SocketsMod.paintedPlanks, paintColour, 2);
	    			par1ItemStack.damageItem(1, par3EntityPlayer);
            	}
                
            }
            
            if(b == Blocks.wool && par2World.getBlockMetadata(var13, var14, var15) != 15 - this.paintColour)
            {
            	par2World.playSoundEffect((double)var13 + 0.5D, (double)var14 + 0.5D, (double)var15 + 0.5D, "step.cloth", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            	for(int i = 0; i < 16; i++)
                {
                	int met = 15 - this.paintColour;
                	String id = "" + Block.getIdFromBlock(Blocks.wool);
                	par2World.spawnParticle("tilecrack_" + id + "_" + met, (double)var13 + rand.nextDouble() - 0.5, var14 + rand.nextDouble() - 0.5, var15 + rand.nextDouble() - 0.5, 0, 0, 0);
                }
            	//par2World.setBlock(var13, var14, var15, SocketsMod.paintedPlanks.blockID, paintColour, 2);
            	par2World.setBlockMetadataWithNotify(var13, var14, var15, 15 - this.paintColour, 2);
    			par1ItemStack.damageItem(1, par3EntityPlayer);
            }
            
            if(b == Blocks.hardened_clay || (b == Blocks.stained_hardened_clay && par2World.getBlockMetadata(var13, var14, var15) != 15 - this.paintColour))
            {
            	par2World.playSoundEffect((double)var13 + 0.5D, (double)var14 + 0.5D, (double)var15 + 0.5D, "step.cloth", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            	for(int i = 0; i < 16; i++)
                {
                	int met = 15 - this.paintColour;
                	String id = "" + Block.getIdFromBlock(Blocks.wool);
                	par2World.spawnParticle("tilecrack_" + id + "_" + met, (double)var13 + rand.nextDouble() - 0.5, var14 + rand.nextDouble() - 0.5, var15 + rand.nextDouble() - 0.5, 0, 0, 0);
                }
            	//par2World.setBlock(var13, var14, var15, SocketsMod.paintedPlanks.blockID, paintColour, 2);
            	if(b == Blocks.hardened_clay)
            	{
            		par2World.setBlock(var13, var14, var15, Blocks.stained_hardened_clay, 15 - this.paintColour, 2);
            	}
            	else
            	{
            		par2World.setBlockMetadataWithNotify(var13, var14, var15, 15 - this.paintColour, 2);
            	}
    			par1ItemStack.damageItem(1, par3EntityPlayer);
            }

            if(b instanceof BlockPipeBase && par2World.getTileEntity(var13, var14, var15) != null && par2World.getTileEntity(var13, var14, var15) instanceof TilePipeBase)
            {
            	if(! par2World.isRemote)
            	{
	            	((TilePipeBase)par2World.getTileEntity(var13, var14, var15)).colour = this.paintColour;
                    PacketHandler.instance.sendClientPipeColour((TilePipeBase)par2World.getTileEntity(var13, var14, var15));
            	}
            	par2World.playSoundEffect((double)var13 + 0.5D, (double)var14 + 0.5D, (double)var15 + 0.5D, "step.cloth", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
            	for(int i = 0; i < 16; i++)
                {
                	int met = 15 - this.paintColour;
                	String id = "" + Block.getIdFromBlock(Blocks.wool);
                	par2World.spawnParticle("tilecrack_" + id + "_" + met, (double)var13 + rand.nextDouble() - 0.5, var14 + rand.nextDouble() - 0.5, var15 + rand.nextDouble() - 0.5, 0, 0, 0);
                }
            }
        }
        
        return par1ItemStack;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) 
	{
		String name = "";
		switch(this.paintColour) 
		{
		case 0: name = "Black"; break;
		case 1: name = "Red"; break;
		case 2: name = "Green"; break;
		case 3: name = "Brown"; break;
		case 4: name = "Blue"; break;
		case 5: name = "Purple"; break;
		case 6: name = "Cyan"; break;
		case 7: name = "Light Gray"; break;
		case 8: name = "Gray"; break;
		case 9: name = "Pink"; break;
		case 10: name = "Lime"; break;
		case 11: name = "Yellow"; break;
		case 12: name = "Light Blue"; break;
		case 13: name = "Magenta"; break;
		case 14: name = "Orange"; break;
		case 15: name = "White"; break;
		}
		//System.out.println(getItemName() + "." + name);
		return getUnlocalizedName() + "." + name;
	}
	
}
