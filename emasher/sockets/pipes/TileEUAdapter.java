package emasher.sockets.pipes;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;

public class TileEUAdapter extends TileEntity implements IEnergySource, IEnergySink, IEnergyHandler
{
	public EnergyStorage capacitor;
	private boolean init = false;
	private boolean addedToEnergyNet = false;
	
	public TileEUAdapter()
	{
		capacitor = new EnergyStorage(2500);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		
		capacitor.writeToNBT(data);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		
		capacitor.readFromNBT(data);
	}
	
	@Override
	public void updateEntity()
	{	
		if(! init && worldObj != null)
		{
			if(! worldObj.isRemote)
			{
				 EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
                 MinecraftForge.EVENT_BUS.post(loadEvent);
                 this.addedToEnergyNet = true;
			}
			
			init = true;
			
		}
		
		ForgeDirection d;
		if(! worldObj.isRemote) for(int i = 0; i < 6; i++)
		{
			d = ForgeDirection.getOrientation(i);
			int xo = xCoord + d.offsetX;
			int yo = yCoord + d.offsetY;
			int zo = zCoord + d.offsetZ;
			
			TileEntity te = worldObj.getBlockTileEntity(xo, yo, zo);
			
			if(te != null)
			{
				if(te instanceof IEnergyHandler)
				{
					IEnergyHandler ieh = (IEnergyHandler)te;
					
					int amnt = ieh.receiveEnergy(d.getOpposite(), capacitor.extractEnergy(2500, true), false);
					capacitor.extractEnergy(amnt, false);
				}
			}
		}
	}
	
	@Override
    public void invalidate()
    {
            super.invalidate();
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
    }
	
	@Override
    public void onChunkUnload()
    {
            super.onChunkUnload();
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            addedToEnergyNet = false;
    }
	
	// IEnergyHandler
	
	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	{
		return capacitor.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	{
		return capacitor.extractEnergy(maxExtract, simulate);
	}

	@Override
	public boolean canInterface(ForgeDirection from)
	{
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from)
	{
		return capacitor.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from)
	{
		return capacitor.getMaxEnergyStored();
	}
	
	//EU
	
	@Override
	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
	{
		return true;
	}

	@Override
	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
	{
		return true;
	}

	@Override
	public double demandedEnergyUnits()
	{
		return (capacitor.getMaxEnergyStored() - capacitor.getEnergyStored()) / 4;
	}

	@Override
	public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
	{
		return capacitor.receiveEnergy((int)(amount * 4), false);
	}

	@Override
	public int getMaxSafeInput()
	{
		return Integer.MAX_VALUE;
	}

	@Override
	public double getOfferedEnergy()
	{
		return Math.min(capacitor.getEnergyStored()/4, 512);
	}

	@Override
	public void drawEnergy(double amount)
	{
		capacitor.extractEnergy((int)amount * 4, false);
	}

}
