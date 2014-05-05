package emasher.gas.block;

import java.util.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import emasher.core.EmasherCore;
import emasher.gas.EmasherGas;
import emasher.gas.tileentity.TileDuct;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.liquids.LiquidStack;

public class BlockDuct extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	public Icon topTexture;
	
	public BlockDuct(int par1) 
	{
		super(par1, Material.rock);
		this.setCreativeTab(EmasherGas.tabGasCraft);
	}

	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		return new TileDuct();
	}
	
	@Override
	public boolean hasTileEntity(int metadata)
    {
        return true;
    }
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		this.blockIcon = par1IconRegister.registerIcon("brick");
		this.topTexture = par1IconRegister.registerIcon("gascraft:chimney");
    }
	
	@Override
	public Icon getIcon(int par1, int par2)
    {
		if(par1 != 0 && par1 != 1) return this.blockIcon;
		return this.topTexture;
    }
		
}
