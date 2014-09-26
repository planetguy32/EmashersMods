package emasher.gas.worldgen;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import emasher.gas.EmasherGas;
import net.minecraft.init.Blocks;
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

public class WorldGenGas implements IWorldGenerator
{
    public WorldGenGas()
    {
    	
    }
    
    
    public void generate(Random random, int i, int k, World world, IChunkProvider provider, IChunkProvider provider2)
    {
    	if(random.nextInt(8) == 0)
    	{
	    	int y = random.nextInt(20) + 5;
	    	int x = i * 16;
	    	int z = k * 16;
	    	
	    	x += random.nextInt(16);
	    	z += random.nextInt(16);
	    	
	    	if(world.provider.dimensionId != -1)
	    	{
	    		if(world.getBlock(x, y, z) == Blocks.stone) world.setBlock(x, y, z, EmasherGas.gasPocket);
	    	}
	    	else
	    	{
	    		if(world.getBlock(x, y, z) == Blocks.netherrack) world.setBlock(x, y, z, EmasherGas.plasmaPocket);
	    	}
	    		
    	}
        
    }
}