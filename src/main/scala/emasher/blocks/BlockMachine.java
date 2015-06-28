package emasher.blocks;

import emasher.EngineersToolbox;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockMachine extends Block {

	public BlockMachine() {
		super( Material.iron );
		this.setCreativeTab( EngineersToolbox.tabBlocks() );
	}
	
	@Override
	public void registerBlockIcons( IIconRegister register ) {
		this.blockIcon = register.registerIcon( "eng_toolbox:machine" );
	}
	
}
