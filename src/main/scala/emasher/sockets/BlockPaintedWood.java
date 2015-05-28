package emasher.sockets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockPaintedWood extends Block {
	private static final int NUM_BLOCKS = 16;
	private IIcon[] textures = new IIcon[16];

	public BlockPaintedWood( int par2, Material par4Material ) {
		super( par4Material );
		this.setCreativeTab( SocketsMod.tabSockets );
	}
	
	public IIcon getIcon( int par1, int par2 ) {
		return textures[par2];
	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		for( int i = 0; i < 16; i++ ) {
			textures[i] = par1IconRegister.registerIcon( "sockets:tile" + ( 16 + i ) );
		}
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void getSubBlocks( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		for( int var4 = 0; var4 < NUM_BLOCKS; ++var4 ) {
			par3List.add( new ItemStack( par1, 1, var4 ) );
		}
	}
	
	@Override
	public int damageDropped( int meta ) {
		return meta;
	}
}
