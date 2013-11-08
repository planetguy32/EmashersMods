package emasher.sockets.pipes

import net.minecraft.tileentity._;
import net.minecraft.nbt._;
import net.minecraftforge.fluids._;
import net.minecraftforge.common._;

class TileFluidPipe extends TileEntity
{
	final var CAPACITY:Int = 1000;
	var tank:FluidTank = new FluidTank(CAPACITY);
	var lastFrom:ForgeDirection = ForgeDirection.UNKNOWN;
	var meta:Integer = 0;
	
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
	
	override def updateEntity()
	{
		if(! worldObj.isRemote)
		{
			meta += 1;
			if(meta >= 8)
			{
				meta = 0;
				
				var order = randOrd;
				
				if(tank.getFluid == null) worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				
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
						d = ForgeDirection.getOrientation(order(i));
						
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
					
					if(doneDrain) lastFrom = ForgeDirection.UNKNOWN;
					
					if(doneDrain || tank.getFluid == null || tank.getFluid.amount <= 0) return;
					
					// (2) Attempt to place fluid into adjacent TileFluidPipe that isn't last insertion
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(order(i));
						
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
								if(amnt > 0) doneDrain = true;
							}
						}
					}
					
					lastFrom = ForgeDirection.UNKNOWN;
					
					//if(doneDrain || tank.getFluid == null || tank.getFluid.amount <= 0) return;
					
					// (3) If adjacent to a start pipe and only one fluid pipe, attempt to place fluid into adjacent fluid pipe
					
					/*var fPipes:Int = 0;
					var sPipe:Boolean = false;
					var lastFPipe:TileFluidPipe = null;
					var lastFPipeD: ForgeDirection = ForgeDirection.UNKNOWN;
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(i);
						
						xo = xCoord + d.offsetX;
						yo = yCoord + d.offsetY;
						zo = zCoord + d.offsetZ;
						var t:TileEntity = worldObj.getBlockTileEntity(xo, yo, zo);
						
						if(t.isInstanceOf[TileStartPipe]) sPipe = true;
						if(t.isInstanceOf[TileFluidPipe])
						{
							fPipes += 1;
							lastFPipe = t.asInstanceOf[TileFluidPipe];
							lastFPipeD = d;
						}
					}
					
					if(sPipe && fPipes == 1)
					{
						var amnt:Int = lastFPipe.fill(lastFPipeD.getOpposite(), tank.drain(CAPACITY, false), true);
						tank.drain(amnt, true);
						//doneDrain = true;
					}*/
				}
			}
		}
	}
	
	def fill(from: ForgeDirection, resource: FluidStack, doFill: Boolean)
	:Int =
	{
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
		lastFrom = from;
		tank.fill(resource, doFill);
	}
	
	def randOrd
	:Array[Int] = 
	{
		var result = new Array[Int](6);
		var temp = Array[Int](0, 1, 2, 3, 4, 5, 6);
		var i = 0;
		
		while(i < 6)
		{
			var rand = worldObj.rand.nextInt(6);
			
			if(temp(rand) != -1)
			{
				result(i) = temp(rand);
				temp(rand) = -1;
				i += 1;
			}
		}
		
		return result;
	}
}