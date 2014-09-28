package emasher.sockets;

import java.util.List;

import cpw.mods.fml.common.*;
import cpw.mods.fml.relauncher.*;
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
import java.util.*;

import emasher.core.EmasherCore;

public class BlockPaintedWood extends Block
{
	private static final int NUM_BLOCKS = 16;
	private IIcon[] textures = new IIcon[16];

	public BlockPaintedWood(int par2, Material par4Material)
	{
		super(par4Material);
		this.setCreativeTab(SocketsMod.tabSockets);
	}
	
	public IIcon getIcon(int par1, int par2)
    {
		return textures[par2];
    }
	
	@Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
		for(int i = 0; i < 16; i++)
		{
			textures[i] = par1IconRegister.registerIcon("sockets:tile" + (16 + i));
		}
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 0; var4 < NUM_BLOCKS; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
	
	@Override
	public int damageDropped(int meta)
	{
		return meta;
	}
}
