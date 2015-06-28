package emasher.blocks;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDeadAlgae extends BlockPondScum {

	public static final int GROWTH_TIME_DEAD = 3;

	public BlockDeadAlgae() {
		super();
	}

	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		this.blockIcon = par1IconRegister.registerIcon( "eng_toolbox:deadAlgae" );
	}

	@Override
	public void updateTick( World world, int x, int y, int z, Random random ) {
		int meta = world.getBlockMetadata( x, y, z );

		if( meta >= GROWTH_TIME_DEAD ) {
			world.setBlockToAir( x, y, z );
		} else {
			world.setBlockMetadataWithNotify( x, y, z, meta + 1, 2 );
		}

	}

}
