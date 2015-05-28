package emasher.core.hemp;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

public class BlockHemp extends Block {
	private static final int GROWTH_TIME = 15;
	
	public BlockHemp() {
		super( Material.plants );
		float f = 0.375F;
		setBlockBounds( 0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f );
		setTickRandomly( true );
	}
	
	@Override
	public void registerBlockIcons( IIconRegister par1IconRegister ) {
		this.blockIcon = par1IconRegister.registerIcon( "emashercore:hemp" );
	}
	
	@Override
	public IIcon getIcon( int side, int meta ) {
		return this.blockIcon;
	}
	
	@Override
	public Item getItemDropped( int i, Random random, int j ) {
		return EmasherCore.hempPlant;
	}
	
	@Override
	public int quantityDropped( Random random ) {
		return 1;
	}
	
	@Override
	public void onNeighborBlockChange( World world, int i, int j, int k, Block l ) {
		checkBlockCoordValid( world, i, j, k );
	}

	protected final void checkBlockCoordValid( World world, int i, int j, int k ) {
		if( !canBlockStay( world, i, j, k ) ) {
			dropBlockAsItem( world, i, j, k, world.getBlockMetadata( i, j, k ), 0 );
			world.setBlockToAir( i, j, k );
		}
	}
	
	@Override
	public boolean canBlockStay( World world, int i, int j, int k ) {
		return canPlaceBlockAt( world, i, j, k );
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k ) {
		return null;
	}
	
	@Override
	public boolean canPlaceBlockAt( World world, int i, int j, int k ) {
		boolean toReturn = false;
		Block l = world.getBlock( i, j - 1, k );
		
		if( l == Blocks.grass || l == Blocks.dirt || l == Blocks.gravel || l == Blocks.sand || l == EmasherCore.mixedDirt || l == EmasherCore.mixedSand || l == this ) {
			toReturn = true;
		}
		
		return toReturn;
	}
	
	@Override
	public void updateTick( World world, int i, int j, int k, Random random ) {
		if( world.isAirBlock( i, j + 1, k ) ) {
			int l;
			for( l = 1; world.getBlock( i, j - l, k ) == this; l++ ) {
			}
			if( l < 3 ) {
				int i1 = world.getBlockMetadata( i, j, k );
				if( i1 == GROWTH_TIME ) {
					world.setBlock( i, j + 1, k, this, 0, 2 );
					world.setBlockMetadataWithNotify( i, j, k, 0, 2 );
				} else if( world.getBlockLightValue( i, j + 1, k ) >= 9 && nearWater( world, i, j, k ) ) {
					world.setBlockMetadataWithNotify( i, j, k, i1 + 1, 2 );
				}
			}
		}
	}
	
	public boolean nearWater( World world, int i, int j, int k ) {
		boolean toReturn = false;
		
		if( world.getBlock( i, j - 1, k ) == this && nearWater( world, i, j - 1, k ) ) {
			toReturn = true;
		} else if( world.getBlock( i - 1, j - 1, k ).getMaterial() == Material.water ) {
			toReturn = true;
		} else if( world.getBlock( i + 1, j - 1, k ).getMaterial() == Material.water ) {
			toReturn = true;
		} else if( world.getBlock( i, j - 1, k - 1 ).getMaterial() == Material.water ) {
			toReturn = true;
		} else if( world.getBlock( i, j - 1, k + 1 ).getMaterial() == Material.water ) {
			toReturn = true;
		}
		
		return toReturn;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return 1;
	}
}