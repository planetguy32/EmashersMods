package emasher.tileentities;

import emasher.api.IGasReceptor;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TileDuct extends TileEntity implements IGasReceptor {
	
	private FluidTank tank;
	
	public TileDuct() {
		tank = new FluidTank( 4000 );
	}
	
	public FluidStack getFluid() {
		return tank.getFluid();
	}
	
	@Override
	public void updateEntity() {
		if( !worldObj.isRemote ) {
			if( tank.getFluid() != null && tank.getFluid().amount >= 4000 && worldObj.getBlock( xCoord, yCoord + 1, zCoord ) == Blocks.stone_slab ) {
				outputGas();
			} else if( tank.getFluid() != null && tank.getFluid().amount > 0 && worldObj.getTileEntity( xCoord, yCoord + 1, zCoord ) != null && worldObj.getTileEntity( xCoord, yCoord + 1, zCoord ) instanceof IGasReceptor ) {
				int temp = ( ( IGasReceptor ) worldObj.getTileEntity( xCoord, yCoord + 1, zCoord ) ).recieveGas( tank.getFluid(), ForgeDirection.DOWN, true );
				tank.drain( temp, true );
			}

			//TODO Check if it is suposed to be lit_furnace or just idle_furnace
			//if(worldObj.getBlock(xCoord, yCoord - 1,  zCoord) == Block.furnaceBurning.blockID && worldObj.rand.nextInt(256) == 0)
			if( worldObj.getBlock( xCoord, yCoord - 1, zCoord ) == Blocks.lit_furnace && worldObj.rand.nextInt( 256 ) == 0 ) {
				tank.fill( new FluidStack( emasher.fluids.Fluids.fluidSmoke(), 4000 ), true );
			}
		}
	}
	
	public void outputGas() {
		if( !worldObj.isRemote ) {
			int x = xCoord;
			int y = yCoord + 2;
			int z = zCoord;
			
			if( worldObj.getBlock( x, y, z ) == Blocks.air ) {
				if( tank.getFluid() != null && tank.getFluid().amount >= 4000 ) {
					worldObj.setBlock( x, y, z, tank.getFluid().getFluid().getBlock() );
					tank.drain( 4000, true );
				}
			}
		}
	}

	@Override
	public int recieveGas( FluidStack gas, ForgeDirection direction, boolean doFill ) {
		return tank.fill( gas, doFill );
	}
	
	@Override
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );
		tank.setFluid( FluidStack.loadFluidStackFromNBT( data ) );

	}

	@Override
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		if( tank.getFluid() != null ) tank.getFluid().writeToNBT( data );
		
	}
	
}
