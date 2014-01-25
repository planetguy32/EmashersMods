package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileMiniPortal extends TileAdapterBase implements IFluidHandler
{
	public static final int CAPACITY = 8000;
	public FluidTank tank = new FluidTank(CAPACITY);
	
	public int partnerX;
	public int partnerY;
	public int partnerZ;
	
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		tank.writeToNBT(data);
		data.setInteger("partnerX", partnerX);
		data.setInteger("partnerY", partnerY);
		data.setInteger("partnerZ", partnerZ);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
		tank.readFromNBT(data);
		if(data.hasKey("partnerX")) partnerX = data.getInteger("partnerX");
		if(data.hasKey("partnerY")) partnerY = data.getInteger("partnerY");
		if(data.hasKey("partnerZ")) partnerZ = data.getInteger("partnerZ");
	}
	
	@Override
	public void updateEntity()
	{
		for(int i = 0; i < 6; i++)
		{
			if(outputs[i])
			{
				ForgeDirection d = ForgeDirection.getOrientation(i);
				int xo = xCoord + d.offsetX;
				int yo = yCoord + d.offsetY;
				int zo = zCoord + d.offsetZ;
				TileEntity te = worldObj.getBlockTileEntity(xo, yo, zo);
				if(te != null && te instanceof IFluidHandler)
				{
					int amnt = ((IFluidHandler)te).fill(d.getOpposite(), drain(d, 16000, false), true);
					drain(d, amnt, true);
				}
			}
		}
	}
	
	public World getOtherWorld()
	{
		if(worldObj.provider.dimensionId == 0)
		{
			return MinecraftServer.getServer().worldServerForDimension(-1);
		}
		else if(worldObj.provider.dimensionId == -1)
		{
			return MinecraftServer.getServer().worldServerForDimension(0);
		}
		
		return null;
	}
	
	public void setPartner(int x, int y, int z)
	{
		partnerX = x;
		partnerY = y;
		partnerZ = z;
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		int result = tank.fill(resource, doFill);
		FluidStack resource2 = resource.copy();
		resource2.amount -= result;
		if(resource2.amount > 0)
		{
			World other = getOtherWorld();
			if(other != null)
			{
				TileEntity te = other.getBlockTileEntity(partnerX, partnerY, partnerZ);
				
				if(te != null && te instanceof TileMiniPortal)
				{
					TileMiniPortal partner = (TileMiniPortal)te;
					result = partner.tank.fill(resource2, doFill);
				}
			}
			
		}
		
		return result;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		if (resource == null || !resource.isFluidEqual(tank.getFluid()))
        {
            return null;
        }
        FluidStack result = tank.drain(resource.amount, doDrain);
        
        if(result == null || (result != null && result.amount < resource.amount))
        {
        	int newAmnt;
        	if(result != null) newAmnt = resource.amount - result.amount;
        	else newAmnt = resource.amount;
        	World other = getOtherWorld();
			if(other != null)
			{
				TileEntity te = other.getBlockTileEntity(partnerX, partnerY, partnerZ);
				
				if(te != null && te instanceof TileMiniPortal)
				{
					TileMiniPortal partner = (TileMiniPortal)te;
					
					if(resource.isFluidEqual(partner.tank.getFluid()))
					{
						if(result != null) result.amount += partner.tank.drain(newAmnt, doDrain).amount;
						else result = partner.tank.drain(newAmnt, doDrain);
					}
					
				}
			}
        }
        
        return result;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
        FluidStack result = tank.drain(maxDrain, doDrain);
        
        if(result == null || (result != null &&  result.amount < maxDrain))
        {
        	int newAmnt;
        	if(result == null) newAmnt = maxDrain;
        	else newAmnt = maxDrain - result.amount;
        	
        	World other = getOtherWorld();
			if(other != null)
			{
				TileEntity te = other.getBlockTileEntity(partnerX, partnerY, partnerZ);
				
				if(te != null && te instanceof TileMiniPortal)
				{
					TileMiniPortal partner = (TileMiniPortal)te;
					
					if(result == null || (result != null && result.isFluidEqual(partner.tank.getFluid())))
					{
						if(result != null) result.amount += partner.tank.drain(newAmnt, doDrain).amount;
						else result = partner.tank.drain(newAmnt, doDrain);
					}
					
				}
			}
        }
        
        return result;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		if(fluid.getID() == FluidRegistry.LAVA.getID() && ! SocketsMod.miniPortalLava) return false;
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		return new FluidTankInfo[]{tank.getInfo()};
	}
}
