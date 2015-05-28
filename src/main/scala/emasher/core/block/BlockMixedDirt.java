package emasher.core.block;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockMixedDirt extends Block {
	public BlockMixedDirt( Material par2Material ) {
		super( par2Material );
		this.setCreativeTab( EmasherCore.tabEmasher );

	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		this.blockIcon = par1IconRegister.registerIcon( "emashercore:mixedDirt" );
	}
	
	
}
