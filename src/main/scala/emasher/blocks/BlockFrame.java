package emasher.blocks;

import emasher.EngineersToolbox;
import emasher.tileentities.TileFrame;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFrame extends BlockAdapterBase {
	public BlockFrame() {
		super( Material.iron );
		this.setCreativeTab( EngineersToolbox.tabBlocks() );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata ) {
		return new TileFrame();
	}

	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "eng_toolbox:frame" );
		this.outputIcon = ir.registerIcon( "eng_toolbox:frameMagnet" );
	}
}
