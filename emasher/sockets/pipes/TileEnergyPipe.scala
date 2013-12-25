package emasher.sockets.pipes

import net.minecraft.tileentity._;
import net.minecraft.nbt._;
import cofh.api.energy._;
import net.minecraftforge.common._;

class TileEnergyPipe extends TilePipeBase with IEnergyHandler
{
	final var CAPACITY:Int = 1000;
	var capacitor:EnergyStorage = new EnergyStorage(CAPACITY);
	var lastFrom:ForgeDirection = ForgeDirection.UNKNOWN;
	var meta:Integer = 0;
	
	override def readFromNBT(data: NBTTagCompound)
	{
		super.readFromNBT(data);
		capacitor.readFromNBT(data);
		if(data.hasKey("lastFrom"))
		{
			lastFrom = ForgeDirection.getOrientation(data.getInteger("lastFrom"));
		}
	}
	
	override def writeToNBT(data: NBTTagCompound)
	{
		super.writeToNBT(data);
		capacitor.writeToNBT(data);
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
				
				//if(capacitor.getEnergy == null) worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				
				if(capacitor.getEnergyStored() > 0)
				{
					var doneDrain:Boolean = false;
					
					var d:ForgeDirection = null;
					var xo:Int = 0;
					var yo:Int = 0;
					var zo:Int = 0;
					
					// (1) Attempt to place energy into adjacent IEnergyHandler that isn't TileStartPipe
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(order(i));
						
						xo = xCoord + d.offsetX;
						yo = yCoord + d.offsetY;
						zo = zCoord + d.offsetZ;
						var t:TileEntity = worldObj.getBlockTileEntity(xo, yo, zo);
						if(t != null && t.isInstanceOf[IEnergyHandler] && ! t.isInstanceOf[TileStartPipe] && ! t.isInstanceOf[TileEnergyPipe])
						{
							var amnt:Int = t.asInstanceOf[IEnergyHandler].receiveEnergy(d.getOpposite, capacitor.extractEnergy(CAPACITY, true), false)
							capacitor.extractEnergy(amnt, false);
							if(amnt > 0) doneDrain = true;
						}
					}
					
					if(doneDrain) lastFrom = ForgeDirection.UNKNOWN;
					
					if(doneDrain || capacitor.getEnergyStored <= 0) return;
					
					// (2) Attempt to place energy into adjacent TileEnergyPipe that isn't last insertion
					
					for(i <- 0 to 5)
					{
						d = ForgeDirection.getOrientation(order(i));
						
						xo = xCoord + d.offsetX;
						yo = yCoord + d.offsetY;
						zo = zCoord + d.offsetZ;
						var t:TileEntity = worldObj.getBlockTileEntity(xo, yo, zo);
						
						if(d != lastFrom)
						{
							if(t != null && t.isInstanceOf[TileEnergyPipe] && (t.asInstanceOf[TileEnergyPipe].colour == -1 || colour == -1 || t.asInstanceOf[TileEnergyPipe].colour == colour))
							{
								var amnt:Int = t.asInstanceOf[TileEnergyPipe].receiveEnergy(d.getOpposite, capacitor.extractEnergy(CAPACITY, true), false);
								capacitor.extractEnergy(amnt, false);
								if(amnt > 0) doneDrain = true;
							}
						}
					}
					
					lastFrom = ForgeDirection.UNKNOWN;
				}
			}
		}
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
	
	//IEnergyHandler
	
	override def receiveEnergy(from: ForgeDirection, resource: Int, simulate: Boolean)
	:Int = 
	{
		lastFrom = from;
		capacitor.receiveEnergy(resource, simulate);
	}
	
	
	override def extractEnergy(from: ForgeDirection, amnt:Int, simulate: Boolean)
	:Int = 
	{
		capacitor.extractEnergy(amnt, simulate);
	}
	
	override def canInterface(from: ForgeDirection) = true;
	
	override def getEnergyStored(from: ForgeDirection) = capacitor.getEnergyStored();
	override def getMaxEnergyStored(from: ForgeDirection) = capacitor.getMaxEnergyStored();
	
}