package emasher.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import emasher.tileentities.TileShaleResource;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class BlockShaleResource extends BlockContainer {
	@SideOnly( Side.CLIENT )
	public IIcon[] textures;
	
	public BlockShaleResource() {
		super( Material.rock );
		this.setCreativeTab( EngineersToolbox.tabBlocks() );
		this.setBlockUnbreakable();
		this.setLightLevel( 0.2F );
		this.setBlockName( "shaleResource" );
	}
	
	@Override
	public TileEntity createNewTileEntity( World var1, int metadata ) {
		TileShaleResource newEntity = new TileShaleResource();
		return newEntity;
	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		textures = new IIcon[3];
		this.blockIcon = par1IconRegister.registerIcon( "eng_toolbox:shalegas" );
		textures[0] = this.blockIcon;
		textures[1] = par1IconRegister.registerIcon( "eng_toolbox:shaleoil" );
		textures[2] = par1IconRegister.registerIcon( "eng_toolbox:shaleplasma" );
	}
	
	@Override
	public boolean hasTileEntity( int metadata ) {
		return true;
	}
	
	@Override
	public IIcon getIcon( int par1, int par2 ) {
		return textures[par2];
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void getSubBlocks( Item par1, CreativeTabs par2CreativeTabs, List par3List ) {
		for( int var4 = 0; var4 < 3; ++var4 ) {
			par3List.add( new ItemStack( par1, 1, var4 ) );
		}
	}

	
}
