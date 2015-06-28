package emasher.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.List;

public class BlockMetal extends Block {
	
	private static IIcon[] textures;
	private int numBlocks;

	public BlockMetal( Material par4Material ) {
		super( par4Material );
		this.setCreativeTab( EngineersToolbox.tabBlocks() );
		numBlocks = 9;
		textures = new IIcon[numBlocks];
	}
	
	@Override
	public IIcon getIcon( int side, int meta ) {
		return textures[meta];
	}
	
	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "eng_toolbox:blockAluminium" );
		textures[0] = blockIcon;
		textures[1] = ir.registerIcon( "eng_toolbox:blockBronze" );
		textures[2] = ir.registerIcon( "eng_toolbox:blockCopper" );
		textures[3] = ir.registerIcon( "eng_toolbox:blockLead" );
		textures[4] = ir.registerIcon( "eng_toolbox:blockNickel" );
		textures[5] = ir.registerIcon( "eng_toolbox:blockPlatinum" );
		textures[6] = ir.registerIcon( "eng_toolbox:blockSilver" );
		textures[7] = ir.registerIcon( "eng_toolbox:blockSteel" );
		textures[8] = ir.registerIcon( "eng_toolbox:blockTin" );
	}
	
	@Override
	public int damageDropped( int par1 ) {
		return par1;
	}
	
	@SideOnly( Side.CLIENT )
	public void getSubBlocks( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		for( int i = 0; i < 9; i++ ) par3List.add( new ItemStack( par1, 1, i ) );
	}
	
	@Override
	public ItemStack getPickBlock( MovingObjectPosition target, World world, int x, int y, int z ) {
		int meta = world.getBlockMetadata( x, y, z );
		return new ItemStack( this, 1, meta );
	}

}
