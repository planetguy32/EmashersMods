package emasher.core.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockNormalCube extends Block {
	
	private static IIcon[] textures;
	private int numBlocks;

	public BlockNormalCube( int par2, Material par4Material ) {
		super( par4Material );
		this.setCreativeTab( EmasherCore.tabEmasher );
		numBlocks = 6;
		textures = new IIcon[numBlocks];
	}
	
	@Override
	public IIcon getIcon( int side, int meta ) {
		if( meta != 5 ) return textures[meta];
		
		if( side == 0 || side == 1 ) return textures[2];
		else return textures[5];
	}
	
	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "emashercore:litchen" );
		textures[0] = blockIcon;
		textures[1] = ir.registerIcon( "emashercore:redSandstoneBrick" );
		textures[2] = ir.registerIcon( "emashercore:limestoneBrick" );
		textures[3] = ir.registerIcon( "emashercore:roadWay" );
		textures[4] = ir.registerIcon( "emashercore:dirtyCobble" );
		textures[5] = ir.registerIcon( "emashercore:kilnWall" );
	}


	@Override
	public ArrayList<ItemStack> getDrops( World world, int x, int y, int z, int metadata, int fortune ) {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		if( metadata == 0 )
			result.add( new ItemStack( Blocks.mossy_cobblestone, 1, 0 ) );
		else if( metadata == 5 ) result.add( new ItemStack( this, 1, 2 ) );
		else {
			result.add( new ItemStack( this, 1, metadata ) );
		}

		return result;
	}

	
	@SideOnly( Side.CLIENT )
	@Override
	public void getSubBlocks( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		par3List.add( new ItemStack( par1, 1, 0 ) );
		par3List.add( new ItemStack( par1, 1, 1 ) );
		par3List.add( new ItemStack( par1, 1, 2 ) );
		par3List.add( new ItemStack( par1, 1, 3 ) );
		par3List.add( new ItemStack( par1, 1, 4 ) );
	}
	
	@Override
	public ItemStack getPickBlock( MovingObjectPosition target, World world, int x, int y, int z ) {
		int meta = world.getBlockMetadata( x, y, z );
		if( meta == 5 ) return new ItemStack( this, 1, 2 );
		return new ItemStack( this, 1, meta );
	}
}
