package emasher.gas.worldgen;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import emasher.gas.EmasherGas;
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

public class WorldGenGasVent implements IWorldGenerator
{
    public WorldGenGasVent()
    {
    	
    }
    
    
    public void generate(Random random, int i, int k, World world, IChunkProvider provider, IChunkProvider provider2)
    {
    	int x = i * 16;
		int z = k * 16;
		
		
		try
		{
	    	if(world.getBlockId(x + 4, 0, z) == Block.bedrock.blockID && world.getWorldInfo().getTerrainType() != WorldType.FLAT)
	    	{
	    		if(world.getBlockMetadata(x + 4, 0, z) == 0)
	    		{
			    	for(int it = 0; it<1; it++)
			    	{	
			    		
			    		world.setBlockMetadataWithNotify(x + 4, 0, z, 0x1, 2);
			    		
			    		x = i * 16;
			    		z = k * 16;
			    		
			    		int y = EmasherGas.flatBedrockTop;
				    	if(! EmasherGas.flatBedrock) y = random.nextInt(2) + 3;
				    	
				    	x += random.nextInt(16);
				    	z += random.nextInt(16);
				        
				    	if(world.provider.dimensionId != -1)
				    	{
					        if(random.nextInt(8) == 0)
					        {
					        	world.setBlock(x, y, z, EmasherGas.shaleResource.blockID, 0, 2);
					        	//System.out.println("Gen: " + x + ", " + y + ", " + z);
					        	
					        }
					        else if(Loader.isModLoaded("BuildCraft|Energy") && random.nextInt(12) == 0)
					        {
					        	world.setBlock(x, y, z, EmasherGas.shaleResource.blockID, 1, 2);
					        }
				    	}
				    	else
				    	{
				    		if(random.nextInt(6) == 0) world.setBlock(x, y, z, EmasherGas.shaleResource.blockID, 2, 2);
				    	}
				        
				        
			    	}
			    	
			    	
	    		}
	    	}
		}
		catch(Exception e)
		{
			System.out.println("[GasCraft] Error generating shale resorces for chunk @" + i + ", " + k);
		}
    	
    	
        
    }
}