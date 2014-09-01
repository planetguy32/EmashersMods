package emasher.core.block;

import java.util.ArrayList;
import java.util.List;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;

public class BlockMetal extends Block
{
	
	private static IIcon[] textures;
	private int numBlocks;

	public BlockMetal(Material par4Material)
	{
		super(par4Material);
		this.setCreativeTab(EmasherCore.tabEmasher);
		numBlocks = 9;
		textures = new IIcon[numBlocks];
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return textures[meta];
	}
	
	@Override
    public void registerBlockIcons(IIconRegister ir)
    {
		this.blockIcon = ir.registerIcon("emashercore:blockAluminium");
		textures[0] = blockIcon;
		textures[1] = ir.registerIcon("emashercore:blockBronze");
		textures[2] = ir.registerIcon("emashercore:blockCopper");
		textures[3] = ir.registerIcon("emashercore:blockLead");
		textures[4] = ir.registerIcon("emashercore:blockNickel");
		textures[5] = ir.registerIcon("emashercore:blockPlatinum");
		textures[6] = ir.registerIcon("emashercore:blockSilver");
		textures[7] = ir.registerIcon("emashercore:blockSteel");
		textures[8] = ir.registerIcon("emashercore:blockTin");
    }
	
	@Override
	public int damageDropped(int par1)
    {
        return par1;
    }
	
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{	
		for(int i = 0; i < 9; i++) par3List.add(new ItemStack(par1, 1, i));
    }
	
	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return new ItemStack(this, 1, meta);
	}

}
