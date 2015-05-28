package emasher.sockets;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public class TileTempRS extends TileEntity {
	int timer;
	
	public TileTempRS() {
		timer = 0;
	}
	
	@Override
	public void updateEntity() {
		if( !worldObj.isRemote ) {
			timer++;
			if( timer >= 25 ) {
				worldObj.setBlock( xCoord, yCoord, zCoord, Blocks.air );
				worldObj.removeTileEntity( xCoord, yCoord, zCoord );
			}
		}
	}
}
