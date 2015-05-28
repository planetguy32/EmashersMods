package emasher.defense;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.IBlockAccess;

public class BlockEmeryTile extends Block {

	public BlockEmeryTile( Material par2Material ) {
		super( par2Material );
		this.setCreativeTab( EmasherDefense.tabDefense );
	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		this.blockIcon = par1IconRegister.registerIcon( "emasherdefense:emeryTile" );
	}
	
	@Override
	public boolean canCreatureSpawn( EnumCreatureType type, IBlockAccess world, int x, int y, int z ) {
		return false;
	}

}
