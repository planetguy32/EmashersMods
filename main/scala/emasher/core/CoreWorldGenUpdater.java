package emasher.core;

import java.util.Random;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraft.world.*;

public class CoreWorldGenUpdater
{
	@SubscribeEvent
	public void Load(ChunkEvent event)
	{
		Chunk chunk = event.getChunk();
		
		if(chunk.isChunkLoaded)
		{
			World world = chunk.worldObj;
			int x = chunk.xPosition * 16;
			int z = chunk.zPosition * 16;
			
			try
			{
				if(world.getBlock(x + 14, 0, z + 14) == Blocks.bedrock)
		    	{
		    		if(world.getBlockMetadata(x + 14, 0, z + 14) == 0)
		    		{
		    			world.setBlockMetadataWithNotify(x + 14, 0, z + 14, 0x1, 2);
		    			EmasherCore.gen.generate(new Random(System.nanoTime()), chunk.xPosition, chunk.zPosition, chunk.worldObj, null, null);
		    		}
		    	}
			}
			catch(Exception e)
			{
				System.out.println("[EmasherCore] Error generating resorces for chunk @" + chunk.xPosition + ", " + chunk.zPosition);
			}
		}
	}
}
