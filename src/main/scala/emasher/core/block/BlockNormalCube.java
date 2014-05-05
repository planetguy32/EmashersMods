package emasher.core.block;

import java.util.ArrayList;
import java.util.List;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.*;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;

public class BlockNormalCube extends Block
{
	
	private static Icon[] textures;
	private int numBlocks;

	public BlockNormalCube(int par1, int par2, Material par4Material) 
	{
		super(par1, par4Material);
		this.setCreativeTab(EmasherCore.tabEmasher);
		numBlocks = 6;
		textures = new Icon[numBlocks];
	}
	
	@Override
	public Icon getIcon(int side, int meta)
	{
		if(meta != 5) return textures[meta];
		
		if(side == 0 || side == 1) return textures[2];
		else return textures[5];
	}
	
	@Override
    public void registerIcons(IconRegister ir)
    {
		this.blockIcon = ir.registerIcon("emashercore:litchen");
		textures[0] = blockIcon;
		textures[1] = ir.registerIcon("emashercore:redSandstoneBrick");
		textures[2] = ir.registerIcon("emashercore:limestoneBrick");
		textures[3] = ir.registerIcon("emashercore:roadWay");
		textures[4] = ir.registerIcon("emashercore:dirtyCobble");
		textures[5] = ir.registerIcon("emashercore:kilnWall");
    }
	

	
	@Override
	 public int idDropped(int par1, Random par2Random, int par3)
	 {
		return 0;
	 }
	 
	 @Override
	 public ArrayList<ItemStack> getBlockDropped(World world, int x, int y, int z, int metadata, int fortune)
	 {
	     ArrayList<ItemStack> result = new ArrayList<ItemStack>();
	     
	     if(metadata == 0)
	     result.add(new ItemStack(Block.cobblestoneMossy, 1, 0));
	     else if(metadata == 5) result.add(new ItemStack(blockID, 1, 2));
	     else
	     {
	    	 result.add(new ItemStack(blockID, 1, metadata)); 
	     }
	     
	     return result;
	 }
	 

	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(par1, 1, 2));
		par3List.add(new ItemStack(par1, 1, 3));
		par3List.add(new ItemStack(par1, 1, 4));
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		if(meta == 5) return new ItemStack(this.blockID, 1, 2);
		return new ItemStack(this.blockID, 1, meta);
	}
}
