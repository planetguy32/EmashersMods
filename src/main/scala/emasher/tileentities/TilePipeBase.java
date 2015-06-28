package emasher.tileentities;

import emasher.EngineersToolbox;
import emasher.packethandling.RequestInfoFromServerMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TilePipeBase extends TileEntity {
	public int colour = -1;
	
	@Override
	public void validate() {
		super.validate();
		
		if( worldObj.isRemote ) {
			for( int i = 0; i < 6; i++ ) {
				EngineersToolbox.network().sendToServer( new RequestInfoFromServerMessage( this, ( byte ) i, ( byte ) 3 ) );
			}
		}
	}
	
	@Override
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		data.setInteger( "colour", colour );
	}
	
	@Override
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );
		if( data.hasKey( "colour" ) ) {
			colour = data.getInteger( "colour" );
		}
	}
}
