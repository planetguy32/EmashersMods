package emasher.sockets.pipes;

import cofh.api.energy.IEnergyHandler;
import emasher.sockets.SocketsMod;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class TileMJAdapter extends TileAdapterBase implements IPowerReceptor, IEnergyHandler
{
	public PowerHandler capacitor;
	
	public TileMJAdapter()
	{
		capacitor = new PowerHandler(this, Type.STORAGE);
		capacitor.configure(0, 250, 0, 250);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		capacitor.readFromNBT(data);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		capacitor.writeToNBT(data);
	}
	
	@Override
	public void updateEntity()
	{
		ForgeDirection d;
		if(! worldObj.isRemote) for(int i = 0; i < 6; i++) if(outputs[i])
		{
			d = ForgeDirection.getOrientation(i);
			int xo = xCoord + d.offsetX;
			int yo = yCoord + d.offsetY;
			int zo = zCoord + d.offsetZ;
			
			TileEntity te = worldObj.getBlockTileEntity(xo, yo, zo);
			
			if(te != null)
			{
				int toUse = 250;
				if(capacitor.getEnergyStored() < 250) toUse = (int)capacitor.getEnergyStored();
				if(toUse <= 0) return;
				
				if(te instanceof IEnergyHandler)
				{
					IEnergyHandler ieh = (IEnergyHandler)te;
					
					int amnt = ieh.receiveEnergy(d.getOpposite(), SocketsMod.RFperMJ * (int)capacitor.useEnergy(0, toUse, false), false);
					capacitor.useEnergy(amnt, amnt, true);
				}
				else if(te instanceof IPowerReceptor)
				{
					IPowerReceptor ipr = (IPowerReceptor)te;
					PowerReceiver pr = ipr.getPowerReceiver(d.getOpposite());
					
					if(pr != null)
					{
						int amnt = (int)pr.receiveEnergy(Type.STORAGE, capacitor.useEnergy(0, toUse, false), d.getOpposite());
						capacitor.useEnergy(amnt, amnt, true);
					}
				}
			}
		}
	}
	
	// IEnergyHandler
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		if(! simulate && ! outputs[from.ordinal()])
		{
			capacitor.addEnergy(maxReceive / SocketsMod.RFperMJ);
		}
		
		if(! outputs[from.ordinal()]) return maxReceive;
		else return 0;
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		if(outputs[from.ordinal()]) return SocketsMod.RFperMJ * (int)capacitor.useEnergy(maxExtract / SocketsMod.RFperMJ, maxExtract / SocketsMod.RFperMJ, ! simulate);
		return 0;
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		return (int)capacitor.getEnergyStored() * SocketsMod.RFperMJ;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		return (int)capacitor.getMaxEnergyStored() * SocketsMod.RFperMJ;
	}

	// IPowerReceptor
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side)
	{
		return capacitor.getPowerReceiver();
	}

	@Override
	public void doWork(PowerHandler workProvider) {}

	@Override
	public World getWorld()
	{
		return worldObj;
	}
	
}
