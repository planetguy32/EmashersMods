package emasher.defense;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.src.*;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.*;
import net.minecraftforge.common.ForgeDirection;

import java.util.*;

import cpw.mods.fml.relauncher.*;
import emasher.core.EmasherCore;

public class BlockThin extends BlockPane
{
	public static Icon chainlink;
	public static Icon barb;
	public static Icon razor;
	public static Icon chainPost;
	public static Icon barbPost;
	public static Icon barbPostWood;
	
	
	private static final int NUM_BLOCKS = 6;

	public BlockThin(int par1, Material par4Material) 
	{
		super(par1, "emasherdefense:blank", "emasherdefense:blank", par4Material, true);
		this.setCreativeTab(null);
		Block.opaqueCubeLookup[par1] = true;
		this.setCreativeTab(EmasherDefense.tabDefense);
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		super.registerIcons(par1IconRegister);
		this.blockIcon = par1IconRegister.registerIcon("emasherdefense:chainlink");
		chainlink = this.blockIcon;
		barb = par1IconRegister.registerIcon("emasherdefense:barb");
		razor = par1IconRegister.registerIcon("emasherdefense:razor");
		chainPost = par1IconRegister.registerIcon("emasherdefense:chainPost");
		barbPost = par1IconRegister.registerIcon("emasherdefense:barbPost");
		barbPostWood = par1IconRegister.registerIcon("emasherdefense:barbPostWood");
    }
	
	@Override
	public Icon getIcon(int par1, int par2)
	{
		int meta = par2;
		Icon result;
		
		switch(meta)
		{
		case 0:
			result = chainlink;
			break;
		case 1:
			result = chainPost;
			break;
		case 3:
			result = barbPostWood;
			break;
		case 2:
			result = barb;
			break;
		case 4:
			result = barbPost;
			break;
		case 5:
			result = razor;
			break;
		default:
			result = chainlink;
		}
		
		return result;
	}
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < NUM_BLOCKS; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }   
    }
	
	
	@Override
    public boolean isLadder(World world, int x, int y, int z, EntityLivingBase entity)
    {
        int meta = world.getBlockMetadata(x,  y,  z);
        
        if(meta == 0 || meta == 1)
        {
        	if(entity instanceof EntityPlayer)
        	{
        		return true;
        	}
        }
        
        return false;
    }
	
	@Override
	public int damageDropped(int par1)
    {
		return par1;
    }
	
	
	
	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
	{
		super.onEntityCollidedWithBlock(par1World, par2, par3, par4, par5Entity);
		
		int damage;
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		
		switch(meta)
		{
		case 0:
			damage = 0;
			break;
		case 1:
			damage = 0;
			break;
		case 2:
			damage = 2;
			break;
		case 3:
			damage = 2;
			break;
		case 4:
			damage = 2;
			break;
		case 5:
			damage = 4;
			break;
		default:
			damage = 0;
		}
		
		
		
		if(par5Entity instanceof EntityLiving && damage != 0)
		{
			((EntityLiving)par5Entity).attackEntityFrom(DamageSource.cactus, damage);
		}
	}
	
	/*@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        //return par1World.isRemote ? true : ItemLeash.func_135066_a(par5EntityPlayer, par1World, par2, par3, par4);
        
        if(! par1World.isRemote)
        {
        	int meta = par1World.getBlockMetadata(par2, par3, par4);
        	if(meta == 1 || meta == 3 || meta == 4)
        	{
        		return ItemLeash.func_135066_a(par5EntityPlayer, par1World, par2, par3, par4);
        	}
        }
        
        return true;
    }*/
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		
        this.setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        if(meta >= 2) this.maxY = 1.5D;
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }
	

}
