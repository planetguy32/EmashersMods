package emasher.gas.tileentity;

import java.util.Random;

import emasher.core.Tuple;
import emasher.gas.EmasherGas;
import emasher.gas.Util;
import emasher.gas.block.BlockGasGeneric;
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
import net.minecraft.potion.*;
import net.minecraft.nbt.*;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileGas extends TileEntity 
{
	private FluidStack gas;
	public static final int VOLUME = FluidContainerRegistry.BUCKET_VOLUME * 4;
	Random rand;
	int count;
	
	Tuple[] pos = new Tuple[]{new Tuple(0, 1), new Tuple(1, 0), new Tuple(0, -1), new Tuple(-1, 0)};
	
	public TileGas()
	{
		rand = new Random(System.nanoTime());
		count = rand.nextInt(8);
	}
	
	public TileGas(Fluid gasType)
	{
		gas = new FluidStack(gasType, VOLUME);
		rand = new Random(System.nanoTime());
		count = rand.nextInt(8);
	}
	
	@Override
	public void updateEntity()
	{
		if(! worldObj.isRemote)
		{
			if(Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)] instanceof BlockGasGeneric)
			{
				if(count == 4)
				{
					BlockGasGeneric thisBlock = (BlockGasGeneric)Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
					for(int i = xCoord - 1; i < xCoord + 2; i++)
						for(int j = yCoord - 1; j < yCoord + 2; j++)
							for(int k = zCoord - 1; k < zCoord + 2; k++)
							{
								if(worldObj.getBlockId(i, j, k) == Block.fire.blockID || (worldObj.getBlockId(i, j, k) == Block.torchWood.blockID && worldObj.difficultySetting == 3))
								{
										thisBlock.contactFire(worldObj, xCoord, yCoord, zCoord);
								}
							}
				}
				
				if(count == 8)
				{
					BlockGasGeneric thisBlock = (BlockGasGeneric)Block.blocksList[worldObj.getBlockId(xCoord, yCoord, zCoord)];
				
					
					if(gas.amount <= 8)
					{
						if(canDis(10))
						{
							worldObj.setBlockToAir(xCoord, yCoord, zCoord);
						}
						else if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord))
						{
							moveToOffset(0, 1, 0);
						}
						else
						{
							int x, z;
							boolean done = false;
							int r = rand.nextInt(4);
							
							
							for(int i = 0; i < 4 && ! done; i++)
							{
								x = pos[r].x();
								z = pos[r].y();
								
								if(worldObj.isAirBlock(xCoord + x, yCoord, zCoord + z))
								{
									moveToOffset(x, 0, z);
									done = true;
								}
								
								r++;
								if(r == 4) r = 0;
							}
						}
					}
					else if (gas.amount > 8)
					{
						if(worldObj.isAirBlock(xCoord, yCoord + 1, zCoord))
						{
							splitToOffset(0, 1, 0);
						}
						else
						{
							int x, z;
							boolean done = false;
							int r = rand.nextInt(4);
							
							
							for(int i = 0; i < 4 && ! done; i++)
							{
								x = pos[r].x();
								z = pos[r].y();
								
								if(worldObj.isAirBlock(xCoord + x, yCoord, zCoord + z))
								{
									splitToOffset(x, 0, z);
									done = true;
								}
								
								
								r++;
								if(r == 4) r = 0;
							}
							
							if(! done && worldObj.isAirBlock(xCoord, yCoord - 1, zCoord))
							{
								splitToOffset(0, -1, 0);
							}
						}
					}
					count = 0;
				}
				else
				{
					count++;
				}
			}
		}
		
	}
	
	public boolean canDis(int n)
	{
		boolean result = true;
		
		int i = 0;
		
		while(result && i < n)
		{
			result = worldObj.isAirBlock(xCoord, yCoord + i + 1, zCoord);
			
			i++;
		}
		
		return result;
	}
	
	public void moveToOffset(int x, int y, int z)
	{
		worldObj.setBlock(xCoord + x, yCoord + y, zCoord + z, gas.getFluid().getBlockID(), this.blockMetadata, 4);
		TileEntity t = worldObj.getBlockTileEntity(xCoord + x, yCoord + y, zCoord + z);
		if(t != null && t instanceof TileGas)
		{
			((TileGas)t).setGasAmount(gas.amount);
		}
		
		worldObj.setBlockToAir(xCoord, yCoord, zCoord);
		worldObj.removeBlockTileEntity(xCoord, yCoord, zCoord);
	}
	
	public void splitToOffset(int x, int y, int z)
	{
		int vol;
		int meta;
		worldObj.setBlock(xCoord + x, yCoord + y, zCoord + z, gas.getFluid().getBlockID());
		TileEntity t = worldObj.getBlockTileEntity(xCoord + x, yCoord + y, zCoord + z);
		if(t != null && t instanceof TileGas)
		{
			TileGas tg = (TileGas)t;
			
			tg.setGasAmount(gas.amount / 2);
				
			vol = tg.getGasAmount();
			meta = (vol * 15) / TileGas.VOLUME;
			worldObj.setBlockMetadataWithNotify(x + xCoord, y + yCoord, z + zCoord, meta, 4);
		}
		
		gas.amount /= 2;
		
		vol = gas.amount;
		meta = (vol * 15) / TileGas.VOLUME;
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, meta, 4);
	}
	
	public int getGasAmount()
	{
		return gas.amount;
	}
	
	public void setGasAmount(int newAmount)
	{
		gas.amount = newAmount;
	}
	
	public int getExplosionSize()
	{
		return 4;
	}
	
	public void setGasAmount(int newAmount, World world, int x, int y, int z)
	{
		gas.amount = newAmount;
		world.setBlockMetadataWithNotify(x, y, z, Util.entityToBlock(newAmount), 2);
	}
	
	public FluidStack getGas()
	{
		return gas;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound data)
    {
        super.readFromNBT(data);
        
        if(data.hasKey("Amount"))
        {
        	gas = FluidStack.loadFluidStackFromNBT(data);
        }
    }

	@Override
	public void writeToNBT(NBTTagCompound data)
	{
		super.writeToNBT(data);
		gas.writeToNBT(data);
	}
	
	
}
