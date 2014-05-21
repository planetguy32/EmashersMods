package emasher.gas.tileentity;

import net.minecraft.world.biome.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.material.*;
import net.minecraft.creativetab.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.potion.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

import cpw.mods.fml.common.Loader;
import emasher.gas.EmasherGas;

public class TileShaleResource extends TileEntity
{
	FluidStack theFluid;
	boolean init;
	
	public TileShaleResource()
	{
		init = false;
	}

	public FluidStack getFluid()
	{
		return theFluid;
	}
	
	public void setFluid(Fluid f)
	{
		theFluid = new FluidStack(f, FluidContainerRegistry.BUCKET_VOLUME * worldObj.rand.nextInt(EmasherGas.maxGasInVent - EmasherGas.minGasInVent) + EmasherGas.minGasInVent);
	}
	
	@Override
	public void updateEntity()
	{
		if(! init && worldObj != null && worldObj.blockExists(xCoord, yCoord, zCoord) && ! worldObj.isRemote)
		{
			int meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
			
			init = true;
			
			if(meta == 0)
			{
				setFluid(EmasherGas.fluidNaturalGas);
			}
			else if(Loader.isModLoaded("BuildCraft|Core") && FluidRegistry.getFluid("oil") != null && meta == 1)
			{
                Fluid oil = FluidRegistry.getFluid("oil");
				setFluid(oil);
			}
			else if(meta == 2)
			{
				setFluid(EmasherGas.fluidPlasma);
			}
			else
			{
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
				setFluid(EmasherGas.fluidNaturalGas);
			}
		}
	}

	
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		FluidStack result = null;
		if(theFluid == null)
		{
			worldObj.setBlock(xCoord, yCoord, zCoord, Block.bedrock.blockID);
			worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
		}
		else if(theFluid.amount <= maxDrain && ! EmasherGas.infiniteGasInVent)
		{
			result = new FluidStack(theFluid.getFluid(), theFluid.amount);
			if(doDrain)
			{
				theFluid.amount = 0;
			}
			worldObj.setBlock(xCoord, yCoord, zCoord, Block.bedrock.blockID);
			worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
		}
		else
		{
			result = new FluidStack(theFluid.getFluid(), maxDrain);
			if(doDrain)
			{
				theFluid.amount -= maxDrain;
			}	
		}
		
		return result;
	}
	
	 @Override
	    public void readFromNBT(NBTTagCompound data)
	    {
	        super.readFromNBT(data);
	        theFluid = FluidStack.loadFluidStackFromNBT(data);
	        if(data.hasKey("init-sr"))
	        {
	        	init = data.getBoolean("init-sr");
	        }
	    }

	@Override
    public void writeToNBT(NBTTagCompound data)
    {
        super.writeToNBT(data);
        if(theFluid != null) theFluid.writeToNBT(data);
        data.setBoolean("init-sr", init);
    }

}
