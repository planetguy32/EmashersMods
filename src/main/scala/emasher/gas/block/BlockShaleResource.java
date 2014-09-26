package emasher.gas.block;

import java.util.List;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import emasher.core.EmasherCore;
import emasher.gas.EmasherGas;
import emasher.gas.tileentity.TileShaleResource;

import net.minecraft.world.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.*;
import net.minecraft.util.*;

public class BlockShaleResource extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	public IIcon[] textures;
	
	public BlockShaleResource(int par1) 
	{
		super(Material.rock);
		this.setCreativeTab(EmasherGas.tabGasCraft);
		this.setBlockUnbreakable();
		this.setLightLevel(0.2F);
		this.setBlockName("shaleResource");
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int metadata)
	{
		TileShaleResource newEntity = new TileShaleResource();
		return newEntity;
	}
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		textures = new IIcon[3];
		this.blockIcon = par1IconRegister.registerIcon("gascraft:shalegas");
		textures[0] = this.blockIcon;
		textures[1] = par1IconRegister.registerIcon("gascraft:shaleoil");
		textures[2] = par1IconRegister.registerIcon("gascraft:shaleplasma");
    }
	
	@Override
	public boolean hasTileEntity(int metadata)
    {
        return true;
    }
	
	@Override
	public IIcon getIcon(int par1, int par2)
    {
        return textures[par2];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 3; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
	
	
}
