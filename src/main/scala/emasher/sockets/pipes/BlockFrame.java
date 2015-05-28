package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockFrame extends BlockAdapterBase {
	public BlockFrame() {
		super( Material.iron );
		this.setCreativeTab( SocketsMod.tabSockets );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata ) {
		return new TileFrame();
	}

	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "sockets:frame" );
		this.outputIcon = ir.registerIcon( "sockets:frameMagnet" );
	}
}
