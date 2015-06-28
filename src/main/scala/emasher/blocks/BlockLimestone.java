package emasher.blocks;

import emasher.EngineersToolbox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockLimestone extends Block {
	public BlockLimestone() {
		super( Material.rock );
		this.setCreativeTab( EngineersToolbox.tabBlocks() );

	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		this.blockIcon = par1IconRegister.registerIcon( "eng_toolbox:limestone" );
	}
}
