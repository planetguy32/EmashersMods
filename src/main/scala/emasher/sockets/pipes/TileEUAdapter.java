//package emasher.sockets.pipes;
//
//import cofh.api.energy.EnergyStorage;
//import cofh.api.energy.IEnergyHandler;
//import emasher.sockets.SocketsMod;
//import ic2.api.energy.EnergyNet;
//import ic2.api.energy.event.EnergyTileLoadEvent;
//import ic2.api.energy.event.EnergyTileUnloadEvent;
//import ic2.api.energy.tile.IEnergySink;
//import ic2.api.energy.tile.IEnergySource;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.tileentity.TileEntity;
//import net.minecraftforge.common.util.ForgeDirection;
//import net.minecraftforge.common.MinecraftForge;
//
//public class TileEUAdapter extends TileAdapterBase implements IEnergySource, IEnergySink, IEnergyHandler
//{
//	public EnergyStorage capacitor;
//	private boolean init = false;
//	private boolean addedToEnergyNet = false;
//
//	public TileEUAdapter()
//	{
//		capacitor = new EnergyStorage(2500);
//	}
//
//	@Override
//	public void writeToNBT(NBTTagCompound data)
//	{
//		super.writeToNBT(data);
//
//		capacitor.writeToNBT(data);
//	}
//
//	@Override
//	public void readFromNBT(NBTTagCompound data)
//	{
//		super.readFromNBT(data);
//
//		capacitor.readFromNBT(data);
//	}
//
//	@Override
//	public void updateEntity()
//	{
//		if(! init && worldObj != null)
//		{
//			if(! worldObj.isRemote)
//			{
//				 EnergyTileLoadEvent loadEvent = new EnergyTileLoadEvent(this);
//                 MinecraftForge.EVENT_BUS.post(loadEvent);
//                 this.addedToEnergyNet = true;
//			}
//
//			init = true;
//
//		}
//
//		ForgeDirection d;
//		if(! worldObj.isRemote) for(int i = 0; i < 6; i++) if(outputs[i])
//		{
//			d = ForgeDirection.getOrientation(i);
//			int xo = xCoord + d.offsetX;
//			int yo = yCoord + d.offsetY;
//			int zo = zCoord + d.offsetZ;
//
//			TileEntity te = worldObj.getTileEntity(xo, yo, zo);
//
//			if(te != null)
//			{
//				if(te instanceof IEnergyHandler)
//				{
//					IEnergyHandler ieh = (IEnergyHandler)te;
//
//					int amnt = ieh.receiveEnergy(d.getOpposite(), capacitor.extractEnergy(2500, true), false);
//					capacitor.extractEnergy(amnt, false);
//				}
//			}
//		}
//	}
//
//	@Override
//    public void invalidate()
//    {
//            super.invalidate();
//            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
//            addedToEnergyNet = false;
//    }
//
//	@Override
//    public void onChunkUnload()
//    {
//            super.onChunkUnload();
//            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
//            addedToEnergyNet = false;
//    }
//
//	// IEnergyHandler
//
//	@Override
//	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
//	{
//		if(! outputs[from.ordinal()]) return capacitor.receiveEnergy(maxReceive, simulate);
//		return 0;
//	}
//
//	@Override
//	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
//	{
//		if(outputs[from.ordinal()]) return capacitor.extractEnergy(maxExtract, simulate);
//		return 0;
//	}
//
//	@Override
//	public boolean canConnectEnergy(ForgeDirection from)
//	{
//		return true;
//	}
//
//	@Override
//	public int getEnergyStored(ForgeDirection from)
//	{
//		return capacitor.getEnergyStored();
//	}
//
//	@Override
//	public int getMaxEnergyStored(ForgeDirection from)
//	{
//		return capacitor.getMaxEnergyStored();
//	}
//
//	//EU
//
//	@Override
//	public boolean emitsEnergyTo(TileEntity receiver, ForgeDirection direction)
//	{
//		return true;
//	}
//
//	@Override
//	public boolean acceptsEnergyFrom(TileEntity emitter, ForgeDirection direction)
//	{
//		return true;
//	}
//
//	/*@Override
//	public double demandedEnergyUnits()
//	{
//		return (capacitor.getMaxEnergyStored() - capacitor.getEnergyStored()) / SocketsMod.RFperEU;
//	}
//
//	@Override
//	public double injectEnergyUnits(ForgeDirection directionFrom, double amount)
//	{
//		return capacitor.receiveEnergy((int)(amount * SocketsMod.RFperEU), false);
//	}
//
//	@Override
//	public int getMaxSafeInput()
//	{
//		return Integer.MAX_VALUE;
//	}*/
//
//	@Override
//	public double getOfferedEnergy()
//	{
//		return Math.min(capacitor.getEnergyStored()/SocketsMod.RFperEU, 512);
//	}
//
//	@Override
//	public void drawEnergy(double amount)
//	{
//		capacitor.extractEnergy((int)amount * SocketsMod.RFperEU, false);
//	}
//
//    @Override
//    public int getSourceTier() {
//        return 2;//TODO Check which tier to use
//    }
//
//    @Override
//    public double getDemandedEnergy() {
//        return (capacitor.getMaxEnergyStored() - capacitor.getEnergyStored()) / SocketsMod.RFperEU;
//    }
//
//    @Override
//    public int getSinkTier() {
//        return Integer.MAX_VALUE;
//    }
//
//    @Override
//    public double injectEnergy(ForgeDirection forgeDirection, double amount, double v2) {
//        return capacitor.receiveEnergy((int)(amount * SocketsMod.RFperEU), false);
//    }
//}
