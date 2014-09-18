package emasher.sockets.pipes;

//import emasher.sockets.client.ClientPacketHandler;
import emasher.sockets.client.ClientPacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.*;

public abstract class TilePipeBase extends TileEntity
{
	public int colour = -1;
	
	@Override
	public void validate()
	{
		super.validate();
		
		if(worldObj.isRemote)
        {
            ClientPacketHandler.instance.requestPipeColourData(this);
        }
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		data.setInteger("colour", colour);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		if(data.hasKey("colour"))
		{
			colour = data.getInteger("colour");
		}
	}
}
