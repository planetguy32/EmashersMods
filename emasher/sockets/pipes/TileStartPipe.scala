package emasher.sockets.pipes

import net.minecraft.tileentity._;
import net.minecraft.nbt._;
import net.minecraftforge.fluids._;
import net.minecraftforge.common._;

class TileStartPipe extends TileEntity with IFluidHandler
{
	final var CAPACITY:Int = 1000;
	var tank:FluidTank = new FluidTank(CAPACITY);
	var meta:Integer = 0;
	var lastFrom:ForgeDirection = ForgeDirection.UNKNOWN;
	
	//Load/Save
	
	override def readFromNBT(data: NBTTagCompound)
	{
		tank.readFromNBT(data);
		if(data.hasKey("lastFrom"))
		{
			lastFrom = ForgeDirection.getOrientation(data.getInteger("lastFrom"));
		}
	}
	
	override def writeToNBT(data: NBTTagCompound)
	{
		tank.writeToNBT(data);
		data.setInteger("lastFrom", lastFrom.ordinal);
	}
	
	//Tick
	
	override def updateEntity()
	{
		meta += 1;
		if(meta >= 8)
		{
			meta = 0;
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
						var t:TileEntity = worldObj.getBlockTileEntity(xo, yo, zo);
						if(t.isInstanceOf[IFluidHandler] && ! t.isInstanceOf[TileStartPipe])
						{
							var amnt:Int = t.asInstanceOf[IFluidHandler].fill(d.getOpposite(), tank.drain(CAPACITY, false), true);
							tank.drain(amnt, true);
							if(amnt > 0) doneDrain = true;
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
					var t:TileEntity = worldObj.getBlockTileEntity(xo, yo, zo);
					
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
	
	
}