package emasher.blocks;

import emasher.EngineersToolbox;
import emasher.tileentities.TileGas;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.Random;

public abstract class BlockGasGeneric extends BlockContainer implements IFluidBlock {
	private static final int MIN_PRESSURE = 3;
	public boolean canExplode;
	public boolean canBurn;
	public boolean isDestructive;
	public Fluid blocksFluid;
	
	public BlockGasGeneric( int lightOpacity, boolean canExplode ) {
		super( Material.circuits );
		
		setLightOpacity( lightOpacity );
		this.canExplode = canExplode;
		this.canBurn = false;
		this.isDestructive = false;
		this.setCreativeTab( EngineersToolbox.tabBlocks() );
	}
	
	public BlockGasGeneric( int lightOpacity, boolean canExplode, boolean canBurn, boolean isDestructive ) {
		this( lightOpacity, canExplode );
		this.canBurn = canBurn;
		this.isDestructive = isDestructive;
	}
	
	@Override
	public TileEntity createNewTileEntity( World var1, int metadata ) {
		return new TileGas( this.getFluid() );
	}
	
	@Override
	public boolean hasTileEntity( int metadata ) {
		return true;
	}
	
	@Override
	public void onNeighborBlockChange( World world, int x, int y, int z, Block block ) {
		if( block == Blocks.fire || ( block == Blocks.torch && world.difficultySetting == EnumDifficulty.HARD ) ) {
			contactFire( world, x, y, z );
		}
	}
	
	
	public void contactFire( World world, int x, int y, int z ) {
		if( canExplode && emasher.util.Config.gasBlocksCanExplode() ) {
			world.setBlockToAir( x, y, z );
			world.removeTileEntity( x, y, z );
			world.createExplosion( null, x, y, z, 4, true );
		} else if( canBurn ) {
			int tempY = y - 1;
			while( world.getBlock( x, tempY, z ) == this || world.getBlock( x, tempY, z ) == Blocks.air || world.getBlock( x, tempY, z ).isReplaceable( world, x, tempY, z ) ) {
				tempY--;
			}
			
			tempY++;
			
			world.setBlock( x, y, z, Blocks.air );
			world.removeTileEntity( x, y, z );
			world.setBlock( x, tempY, z, Blocks.fire );
		}
	}
	
	
	@Override
	public boolean isReplaceable( IBlockAccess world, int i, int j, int k ) {
		return true;
	}
	
	
	@Override
	public void onBlockAdded( World world, int x, int y, int z ) {
		super.onBlockAdded( world, x, y, z );

		if( !emasher.util.Config.gasBlocksInWorld() ) {
			world.setBlockToAir( x, y, z );
			return;
		}
		
		TileEntity t = world.getTileEntity( x, y, z );
		
		if( t != null && t instanceof TileGas ) {
			TileGas tg = ( TileGas ) t;
			
			int vol = tg.getGasAmount();
			int meta = ( vol * 15 ) / TileGas.VOLUME;
			world.setBlockMetadataWithNotify( x, y, z, meta, 4 );
		}
	}
	
	@Override
	public void onBlockDestroyedByExplosion( World world, int x, int y, int z, Explosion explosion ) {
		contactFire( world, x, y, z );
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k ) {
		return null;
	}
	
	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped( Random par1Random ) {
		return 0;
	}

	/**
	 * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
	 */
	@Override
	public int getRenderBlockPass() {
		return 1;
	}

	/**
	 * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
	 * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
	 */
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	/**
	 * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
	 */
//	@Override
//    public boolean renderAsNormalBlock()
//    {
//        return false;
//    }
	@Override
	public Fluid getFluid() {
		return blocksFluid;
	}

	@Override
	public FluidStack drain( World world, int x, int y, int z, boolean doDrain ) {
		int amnt = 0;
		TileEntity te = world.getTileEntity( x, y, z );
		if( te != null && te instanceof TileGas ) {
			TileGas tg = ( TileGas ) te;
			
			FluidStack result = tg.getGas();
			
			
			if( doDrain ) {
				world.setBlockToAir( x, y, z );
				world.removeTileEntity( x, y, z );
			}
			
			return result;
		}
		
		
		return null;
	}

	@Override
	public boolean canDrain( World world, int x, int y, int z ) {
		TileEntity te = world.getTileEntity( x, y, z );
		if( te != null && te instanceof TileGas ) {
			return true;
		}
		
		return false;
	}

	public boolean canDestroyBlock( Block block, int x, int y, int z, World world ) {
		return false;
	}

	@Override
	public float getFilledPercentage( World world, int x, int y, int z ) {
		return 1;
	}
}
