package emasher.worldgeneration;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.Random;

public class WorldGenerationUpdater {
	
	@SubscribeEvent
	public void Load( ChunkEvent event ) {
		Chunk chunk = event.getChunk();
		
		if( chunk.isChunkLoaded ) {
			emasher.worldgeneration.WorldGenerators.gasVentGenerator().generate( new Random( System.nanoTime() ), chunk.xPosition, chunk.zPosition, chunk.worldObj, null, null );
		}
	}

}
