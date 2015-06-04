package emasher.sockets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTempRS extends BlockContainer {

	public BlockTempRS() {
		super( Material.iron );
		this.setCreativeTab( null );
		this.setBlockName( "tempRS" );
		this.setLightLevel( 5.0F );
		setBlockBounds( 0.4f, 0.4f, 0.4f, 0.6f, 0.6f, 0.6f );
	}
	
	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		blockIcon = ir.registerIcon( "sockets:tempRS" );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata ) {
		return new TileTempRS();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool( World world, int i, int j, int k ) {
		return null;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int isProvidingStrongPower( IBlockAccess world, int x, int y, int z, int side ) {
		return 15;
	}

	@Override
	public int isProvidingWeakPower( IBlockAccess world, int x, int y, int z, int side ) {
		return 15;
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void randomDisplayTick( World par1World, int par2, int par3, int par4, Random par5Random ) {
		double d0 = ( double ) par2 + 0.5D + ( ( double ) par5Random.nextFloat() - 0.5D ) * 0.2D;
		double d1 = ( double ) ( ( float ) par3 + 0.0625F );
		double d2 = ( double ) par4 + 0.5D + ( ( double ) par5Random.nextFloat() - 0.5D ) * 0.2D;
		float f = 1.0F;

		float f1 = 0.0F;

		float f2 = f * f * 0.7F - 0.5F;
		float f3 = f * f * 0.6F - 0.7F;

		if( f2 < 0.0F ) {
			f2 = 0.0F;
		}

		if( f3 < 0.0F ) {
			f3 = 0.0F;
		}

		par1World.spawnParticle( "reddust", d0, d1 + 0.7F, d2, ( double ) f1, ( double ) f2, ( double ) f3 );
	}
	
}
