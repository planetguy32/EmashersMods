package emasher.gas.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Facing;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.*;
import emasher.api.IGasReceptor;
import emasher.gas.EmasherGas;

public class TileDuct extends TileEntity implements IGasReceptor
{	
	
	private FluidTank tank;
	
	public TileDuct()
	{
		tank = new FluidTank(4000);
	}
	
	public FluidStack getFluid()
	{
		return tank.getFluid();
	}
	
	@Override
	public void updateEntity()
	{
		if(! worldObj.isRemote)
		{
			if(tank.getFluid() != null && tank.getFluid().amount >= 4000 && worldObj.getBlockId(xCoord, yCoord + 1,  zCoord) == Block.stoneSingleSlab.blockID)
			{
				outputGas();
			}
			else if(tank.getFluid() != null && tank.getFluid().amount > 0 && worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord) != null && worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord) instanceof IGasReceptor)
			{
				int temp = ((IGasReceptor)worldObj.getBlockTileEntity(xCoord, yCoord + 1, zCoord)).recieveGas(tank.getFluid(), ForgeDirection.DOWN, true);
				tank.drain(temp, true);
			}
			
			if(worldObj.getBlockId(xCoord, yCoord - 1,  zCoord) == Block.furnaceBurning.blockID && worldObj.rand.nextInt(256) == 0)
			{
				tank.fill(new FluidStack(EmasherGas.fluidSmoke, 4000), true);
			}
		}
	}
	
	public void outputGas()
	{
		if(! worldObj.isRemote)
		{
			int x = xCoord;
			int y = yCoord + 2;
			int z = zCoord;
			
			if(worldObj.getBlockId(x, y, z) == 0)
			{
				if(tank.getFluid() != null && tank.getFluid().amount >= 4000)
				{
					worldObj.setBlock(x, y, z, tank.getFluid().getFluid().getBlockID());
					tank.drain(4000, true);
				}
			}
		}
	}

	@Override
	public int recieveGas(FluidStack gas, ForgeDirection direction, boolean doFill) 
	{
		return tank.fill(gas, doFill);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data)
	{
		super.readFromNBT(data);
	    tank.setFluid(FluidStack.loadFluidStackFromNBT(data));
	    
	}
	    
	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		if(tank.getFluid() != null) tank.getFluid().writeToNBT(data);
		
	}
	
}
