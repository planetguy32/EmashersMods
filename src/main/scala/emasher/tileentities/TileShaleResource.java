package emasher.tileentities;

import cpw.mods.fml.common.Loader;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileShaleResource extends TileEntity {
	FluidStack theFluid;
	boolean init;
	
	public TileShaleResource() {
		init = false;
	}

	public FluidStack getFluid() {
		return theFluid;
	}
	
	public void setFluid( Fluid f ) {
		theFluid = new FluidStack( f, FluidContainerRegistry.BUCKET_VOLUME *
				worldObj.rand.nextInt( emasher.util.Config.maxGasInVent() - emasher.util.Config.minGasInVent() ) + emasher.util.Config.minGasInVent() );
	}
	
	@Override
	public void updateEntity() {
		if( !init && worldObj != null && worldObj.blockExists( xCoord, yCoord, zCoord ) && !worldObj.isRemote ) {
			int meta = worldObj.getBlockMetadata( xCoord, yCoord, zCoord );
			
			init = true;
			
			if( meta == 0 ) {
				setFluid( emasher.fluids.Fluids.fluidNaturalGas() );
			} else if( Loader.isModLoaded( "BuildCraft|Core" ) && FluidRegistry.getFluid( "oil" ) != null && meta == 1 ) {
				Fluid oil = FluidRegistry.getFluid( "oil" );
				setFluid( oil );
			} else if( meta == 2 ) {
				setFluid( emasher.fluids.Fluids.fluidPlasma() );
			} else {
				worldObj.setBlockMetadataWithNotify( xCoord, yCoord, zCoord, 0, 3 );
				setFluid( emasher.fluids.Fluids.fluidNaturalGas() );
			}
		}
	}

	
	public FluidStack drain( int maxDrain, boolean doDrain ) {
		FluidStack result = null;
		if( theFluid == null ) {
			worldObj.setBlock( xCoord, yCoord, zCoord, Blocks.bedrock );
			worldObj.removeTileEntity( xCoord, yCoord, zCoord );
		} else if( theFluid.amount <= maxDrain && !emasher.util.Config.infiniteGasInVent() ) {
			result = new FluidStack( theFluid.getFluid(), theFluid.amount );
			if( doDrain ) {
				theFluid.amount = 0;
			}
			worldObj.setBlock( xCoord, yCoord, zCoord, Blocks.bedrock );
			worldObj.removeTileEntity( xCoord, yCoord, zCoord );
		} else {
			result = new FluidStack( theFluid.getFluid(), maxDrain );
			if( doDrain ) {
				theFluid.amount -= maxDrain;
			}
		}
		
		return result;
	}
	
	@Override
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );
		theFluid = FluidStack.loadFluidStackFromNBT( data );
		if( data.hasKey( "init-sr" ) ) {
			init = data.getBoolean( "init-sr" );
		}
	}

	@Override
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		if( theFluid != null ) theFluid.writeToNBT( data );
		data.setBoolean( "init-sr", init );
	}

}
