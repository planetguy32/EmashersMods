package emasher.sockets.pipes;

import emasher.sockets.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileAdapterBase extends TileEntity
{
	public boolean[] outputs = new boolean[]{false, false, false, false, false, false};
	
	public void toggleOutput(int side)
	{
		outputs[side] = ! outputs[side];
		PacketHandler.instance.sendClientAdapterSide(this, side);
	}
	
	@Override
	public void validate()
	{
		super.validate();
		if(this.worldObj.isRemote)
		{
			emasher.sockets.client.ClientPacketHandler.instance.requestAdapterOutputData(this);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		
		for(int i = 0; i < 6; i++)
		{
			data.setBoolean("outputs" + i, outputs[i]);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		
		for(int i = 0; i < 6; i++)
		{
			if(data.hasKey("outputs" + i)) outputs[i] = data.getBoolean("outputs" + i);
		}
	}
}
