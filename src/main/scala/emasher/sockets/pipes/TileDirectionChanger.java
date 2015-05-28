package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import emasher.sockets.packethandling.ChangerSideMessage;
import emasher.sockets.packethandling.RequestInfoFromServerMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileDirectionChanger extends TileEntity {
	public ForgeDirection[] directions = new ForgeDirection[] {ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN,
			ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN};

	@Override
	public void validate() {
		super.validate();
		if( this.worldObj.isRemote ) {
			for( int i = 0; i < 6; i++ ) {
				SocketsMod.network.sendToServer( new RequestInfoFromServerMessage( this, ( byte ) i, ( byte ) 5 ) );
			}
		}
	}

	public void nextDirection( ForgeDirection side ) {
		int index = side.ordinal();
		if( side == ForgeDirection.UP ) {
			if( directions[index] == ForgeDirection.UNKNOWN ) {
				directions[index] = ForgeDirection.NORTH;
			} else if( directions[index] == ForgeDirection.NORTH ) {
				directions[index] = ForgeDirection.EAST;
			} else if( directions[index] == ForgeDirection.EAST ) {
				directions[index] = ForgeDirection.SOUTH;
			} else if( directions[index] == ForgeDirection.SOUTH ) {
				directions[index] = ForgeDirection.WEST;
			} else if( directions[index] == ForgeDirection.WEST ) {
				directions[index] = ForgeDirection.UNKNOWN;
			}
		} else if( side == ForgeDirection.EAST || side == ForgeDirection.WEST || side == ForgeDirection.NORTH || side == ForgeDirection.SOUTH ) {
			if( directions[index] == ForgeDirection.UNKNOWN ) {
				directions[index] = ForgeDirection.UP;
			} else if( directions[index] == ForgeDirection.UP ) {
				directions[index] = ForgeDirection.DOWN;
			} else if( directions[index] == ForgeDirection.DOWN ) {
				directions[index] = ForgeDirection.UNKNOWN;
			}
		}

		if( side != ForgeDirection.DOWN ) {
			SocketsMod.network.sendToDimension( new ChangerSideMessage( this, ( byte ) index ), worldObj.provider.dimensionId );
		}
	}

	@Override
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		for( int i = 0; i < 6; i++ ) {
			data.setInteger( "dir" + i, directions[i].ordinal() );
		}
	}

	@Override
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );
		for( int i = 0; i < 6; i++ ) {
			if( data.hasKey( "dir" + i ) ) {
				directions[i] = ForgeDirection.getOrientation( data.getInteger( "dir" + i ) );
			} else {
				directions[i] = ForgeDirection.UNKNOWN;
			}
		}
	}
}
