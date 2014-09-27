package emasher.gas.worldgen;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.gas.EmasherGas;

public class WorldGenerationUpdater
{
	
	@SubscribeEvent
	public void Load(ChunkEvent event)
	{
		Chunk chunk = event.getChunk();
		
		if(chunk.isChunkLoaded)
		{
			EmasherGas.gasVentGenerator.generate(new Random(System.nanoTime()), chunk.xPosition, chunk.zPosition, chunk.worldObj, null, null);
		}
	}

}
