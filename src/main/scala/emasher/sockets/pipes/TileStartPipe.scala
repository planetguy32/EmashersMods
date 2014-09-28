package emasher.sockets.pipes

import net.minecraft.tileentity._;
import net.minecraft.nbt._;
import net.minecraftforge.fluids._;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy._;

class TileStartPipe extends TileEntity with IFluidHandler with IEnergyHandler
{
	final var CAPACITY:Int = 8000;
	var tank:FluidTank = new FluidTank(CAPACITY);
	var capacitor:EnergyStorage = new EnergyStorage(CAPACITY);
	
	var meta:Integer = 0;
	var lastFrom:ForgeDirection = ForgeDirection.UNKNOWN;
	var lastFromEn:ForgeDirection = ForgeDirection.UNKNOWN;
	
	//Load/Save
	
	override def readFromNBT(data: NBTTagCompound)
	{
		super.readFromNBT(data);
		tank.readFromNBT(data);
		if(data.hasKey("lastFrom"))
		{
			lastFrom = ForgeDirection.getOrientation(data.getInteger("lastFrom"));
		}
		if(data.hasKey("meta"))
		{
			meta = data.getInteger("meta");
		}
	}
	
	override def writeToNBT(data: NBTTagCompound)
	{
		super.writeToNBT(data);
		tank.writeToNBT(data);
		data.setInteger("lastFrom", lastFrom.ordinal);
		data.setInteger("meta", meta);
	}
	
	//Tick
	
	override def updateEntity()
	{
		if(! worldObj.isRemote)
		{
			meta += 1;
			if(meta >= 8)
			{
				meta = 0;
				
				//Attempt extraction
				
				var rs:Boolean = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) || worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord + 1, zCoord);
				
				if(! rs) for(i <- 0 to 5)
				{
					var opposite:ForgeDirection = ForgeDirection.getOrientation(i).getOpposite();
					var rsNum:Int = worldObj.isBlockProvidingPowerTo(xCoord + opposite.offsetX, yCoord + opposite.offsetY, zCoord + opposite.offsetZ, opposite.ordinal());
					rs = rsNum != 0;
						
					if(! rs)
					{
						rs = worldObj.isBlockIndirectlyGettingPowered(xCoord + opposite.offsetX, yCoord + opposite.offsetY, zCoord + opposite. offsetZ);
						if(! rs) rs = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
						
					}
				}
				
				
				if(rs) for(i <- 0 to 5)
				{
					
					var d:ForgeDirection = ForgeDirection.getOrientation(i);
					
					var xo:Int = xCoord + d.offsetX;
					var yo:Int = yCoord + d.offsetY;
					var zo:Int = zCoord + d.offsetZ;
					
					var t:TileEntity = worldObj.getTileEntity(xo, yo, zo);
					if(t.isInstanceOf[IFluidHandler] && ! t.isInstanceOf[TileStartPipe] && ! t.isInstanceOf[TileFluidPipe])
					{
						var fl:FluidStack = t.asInstanceOf[IFluidHandler].drain(d.getOpposite(), tank.getCapacity() - tank.getFluidAmount(), false);
						var amnt:Int = tank.fill(fl, true);
						t.asInstanceOf[IFluidHandler].drain(d.getOpposite(), amnt, true);
						
						if(amnt > 0) lastFrom = d;
					}
				}
				
				if(tank.getFluid() != null)
				{
					var doneDrain:Boolean = false;
					
					var d:ForgeDirection = null;
					var xo:Int = 0;
					var yo:Int = 0;
					var zo:Int = 0;
					
					// (1) Attempt to place fluid into adjacent IFluidHandler that isn't TileStartPipe
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(i);
						
						if(d != lastFrom)
						{
							xo = xCoord + d.offsetX;
							yo = yCoord + d.offsetY;
							zo = zCoord + d.offsetZ;
							var t:TileEntity = worldObj.getTileEntity(xo, yo, zo);
							if(t.isInstanceOf[IFluidHandler] && ! t.isInstanceOf[TileStartPipe] && ! t.isInstanceOf[TileFluidPipe])
							{
								var amnt:Int = t.asInstanceOf[IFluidHandler].fill(d.getOpposite(), tank.drain(CAPACITY, false), true);
								tank.drain(amnt, true);
								if(amnt > 0) doneDrain = true;
							}
							
							if(t.isInstanceOf[IEnergyHandler] && ! t.isInstanceOf[TileStartPipe])
							{
								var amnt:Int = t.asInstanceOf[IEnergyHandler].receiveEnergy(d.getOpposite(), capacitor.extractEnergy(CAPACITY, true), false);
								capacitor.extractEnergy(amnt, false);
							}
						}
					}
					
					
					
					if(doneDrain || tank.getFluid == null || tank.getFluid.amount <= 0) return;
					
					// (2) Attempt to place fluid into adjacent TileFluidPipe that isn't last insertion
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(i);
						
						xo = xCoord + d.offsetX;
						yo = yCoord + d.offsetY;
						zo = zCoord + d.offsetZ;
						var t:TileEntity = worldObj.getTileEntity(xo, yo, zo);
						
						if(d != lastFrom)
						{
							if(t.isInstanceOf[TileFluidPipe])
							{
								var amnt:Int = t.asInstanceOf[TileFluidPipe].fill(d.getOpposite(), tank.drain(CAPACITY, false), true);
								tank.drain(amnt, true);
								doneDrain = true;
							}
						}
					}
				}
				
				//Energy
				
				if(capacitor.getEnergyStored() > 0)
				{
					
					var d:ForgeDirection = null;
					var xo:Int = 0;
					var yo:Int = 0;
					var zo:Int = 0;
					
					// (1) Attempt to place energy into adjacent IEnergyHandler that isn't TileStartPipe
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(i);
						
						if(d != lastFromEn)
						{
							xo = xCoord + d.offsetX;
							yo = yCoord + d.offsetY;
							zo = zCoord + d.offsetZ;
							var t:TileEntity = worldObj.getTileEntity(xo, yo, zo);
							
							
							if(t.isInstanceOf[IEnergyHandler] && ! t.isInstanceOf[TileStartPipe] && ! t.isInstanceOf[TileEnergyPipe])
							{
								var amnt:Int = t.asInstanceOf[IEnergyHandler].receiveEnergy(d.getOpposite(), capacitor.extractEnergy(CAPACITY, true), false);
								capacitor.extractEnergy(amnt, false);
							}
						}
					}
					
					// (2) Attempt to place energy into adjacent TileEnergyPipe that isn't last insertion
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(i);
						
						xo = xCoord + d.offsetX;
						yo = yCoord + d.offsetY;
						zo = zCoord + d.offsetZ;
						var t:TileEntity = worldObj.getTileEntity(xo, yo, zo);
						
						if(d != lastFrom)
						{
							if(t.isInstanceOf[TileFluidPipe])
							{
								//var amnt:Int = t.asInstanceOf[TileEnergyPipe].fill(d.getOpposite(), capacitor.extractEnergy(CAPACITY, true), false);
								//capacitor.extractEnergy(amnt, false);
							}
						}
					}
				}
			}
		}
	}
	
	//IFluidHandler
	
	override def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean)
	:Int = 
	{
		lastFrom = from;
		tank.fill(resource, doFill);
	}
	
	override def drain(from: ForgeDirection, resource: FluidStack, doDrain: Boolean)
	:FluidStack = 
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        return tank.drain(resource.amount, doDrain);
	}
	
	override def drain(from: ForgeDirection, amnt:Int, doDrain: Boolean)
	:FluidStack = 
	{
		tank.drain(amnt, doDrain);
	}
	
	override def canFill(from: ForgeDirection, resource: Fluid):Boolean = true;
	
	override def canDrain(from: ForgeDirection, resource: Fluid):Boolean = true;
	
	override def getTankInfo(from: ForgeDirection):Array[FluidTankInfo] = Array[FluidTankInfo](tank.getInfo);
	
	//IEnergyHandler
	
	
	override def receiveEnergy(from: ForgeDirection, maxReceive: Int, simulate: Boolean):Int = capacitor.receiveEnergy(maxReceive, simulate);
	
	override def extractEnergy(from: ForgeDirection, maxExtract: Int, simulate: Boolean):Int = capacitor.extractEnergy(maxExtract, simulate);
	
	override def canConnectEnergy(from: ForgeDirection):Boolean = true;
	
	override def getEnergyStored(from: ForgeDirection):Int = capacitor.getEnergyStored();
	override def getMaxEnergyStored(from: ForgeDirection):Int = capacitor.getMaxEnergyStored();
	
	
}