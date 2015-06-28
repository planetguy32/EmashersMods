package emasher.tileentities;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.Random;

public class TileDeflectorGen extends TileEntity {
	private int strength = 3;
	private boolean strChange = false;
	private Random random;
	
	public TileDeflectorGen() {
		super();
		random = new Random( System.nanoTime() );
	}
	
	public void setStrenght( int str ) {
		strength = Math.min( str, 7 );
		strChange = true;
	}
	
	public void updateEntity() {
		boolean stop = false;
		
		if( worldObj.getBlock( xCoord, yCoord + 1, zCoord ) != emasher.blocks.Blocks.deflectorBase() ) {
			boolean signal = worldObj.isBlockIndirectlyGettingPowered( xCoord, yCoord, zCoord );
			
			int loopCount = yCoord + strength;
			
			if( strChange ) {
				loopCount = yCoord + 7;
				strChange = false;
			}

			for( int y = yCoord + 1; y <= loopCount && !stop; y++ ) {
				if( worldObj.getBlock( xCoord, y, zCoord ) == Blocks.air && y <= yCoord + strength && !signal ) {
					worldObj.setBlock( xCoord, y, zCoord, emasher.blocks.Blocks.deflector() );
					worldObj.setBlockMetadataWithNotify( xCoord, y, zCoord, strength - 1, 2 );
				} else if( worldObj.getBlock( xCoord, y, zCoord ) == emasher.blocks.Blocks.deflector() ) {
					if( y > yCoord + strength || signal ) {
						worldObj.setBlockToAir( xCoord, y, zCoord );
					}
					
					int meta = worldObj.getBlockMetadata( xCoord, y, zCoord );
					
					if( random.nextInt( 8 ) == 0 ) {
						meta = strength;
					}
					
					
					worldObj.setBlockMetadataWithNotify( xCoord, y, zCoord, meta, 2 );
				} else {
					stop = true;
				}
			}
		}
	}
	
	public int getStrenth() {
		return strength;
	}
	
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		data.setInteger( "strength", strength );
		data.setBoolean( "strChange", strChange );
	}
	
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );
		if( data.hasKey( "strength" ) ) {
			strength = data.getInteger( "strength" );
		}
		if( data.hasKey( "strChange" ) ) {
			strChange = data.getBoolean( "strChange" );
		}
	}
}
