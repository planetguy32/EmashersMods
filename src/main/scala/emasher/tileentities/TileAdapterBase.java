package emasher.tileentities;

import emasher.EngineersToolbox;
import emasher.packethandling.AdapterSideMessage;
import emasher.packethandling.RequestInfoFromServerMessage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileAdapterBase extends TileEntity {
	public boolean[] outputs = new boolean[] {false, false, false, false, false, false};
	
	public void toggleOutput( int side ) {
		outputs[side] = !outputs[side];
		EngineersToolbox.network().sendToDimension( new AdapterSideMessage( this, ( byte ) side ), worldObj.provider.dimensionId );
	}
	
	@Override
	public void validate() {
		super.validate();
		if( this.worldObj.isRemote ) {
			for( int i = 0; i < 6; i++ ) {
				EngineersToolbox.network().sendToServer( new RequestInfoFromServerMessage( this, ( byte ) i, ( byte ) 4 ) );
			}
		}
	}
	
	@Override
	public void writeToNBT( NBTTagCompound data ) {
		super.writeToNBT( data );
		
		for( int i = 0; i < 6; i++ ) {
			data.setBoolean( "outputs" + i, outputs[i] );
		}
	}
	
	@Override
	public void readFromNBT( NBTTagCompound data ) {
		super.readFromNBT( data );
		
		for( int i = 0; i < 6; i++ ) {
			if( data.hasKey( "outputs" + i ) ) outputs[i] = data.getBoolean( "outputs" + i );
		}
	}
}
