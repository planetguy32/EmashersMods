package emasher.core.block;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockMachine extends Block {

	public BlockMachine() {
		super( Material.iron );
		this.setCreativeTab( EmasherCore.tabEmasher );
	}
	
	@Override
	public void registerBlockIcons( IIconRegister register ) {
		this.blockIcon = register.registerIcon( "emashercore:machine" );
	}
	
}
