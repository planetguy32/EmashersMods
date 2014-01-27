package emasher.gas.block;

import java.util.List;
import java.util.Random;

import buildcraft.BuildCraftEnergy;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import emasher.core.EmasherCore;
import emasher.gas.EmasherGas;
import emasher.gas.tileentity.TileShaleResource;

import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.liquids.*;

public class BlockShaleResource extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	public Icon[] textures;
	
	public BlockShaleResource(int par1) 
	{
		super(par1, Material.rock);
		this.setCreativeTab(EmasherGas.tabGasCraft);
		this.setBlockUnbreakable();
		this.setLightValue(0.2F);
		this.setUnlocalizedName("shaleResource");
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1)
	{
		TileShaleResource newEntity = new TileShaleResource();
		return newEntity;
	}
	
	@Override
    public void registerIcons(IconRegister par1IconRegister)
    {
		textures = new Icon[3];
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
	public Icon getIcon(int par1, int par2)
    {
        return textures[par2];
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < 3; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
	
	
}
