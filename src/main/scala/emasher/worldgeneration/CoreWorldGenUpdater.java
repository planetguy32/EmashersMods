package emasher.worldgeneration;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.Random;

public class CoreWorldGenUpdater {
	@SubscribeEvent
	public void Load( ChunkEvent event ) {
		Chunk chunk = event.getChunk();
		
		if( chunk.isChunkLoaded ) {
			World world = chunk.worldObj;
			int x = chunk.xPosition * 16;
			int z = chunk.zPosition * 16;
			
			try {
				if( world.getBlock( x + 14, 0, z + 14 ) == Blocks.bedrock ) {
					if( world.getBlockMetadata( x + 14, 0, z + 14 ) != emasher.util.Config.oreRetrogenValue() ) {
						world.setBlockMetadataWithNotify( x + 14, 0, z + 14, emasher.util.Config.oreRetrogenValue(), 2 );
						emasher.worldgeneration.WorldGenerators.gen().generate( new Random( System.nanoTime() ), chunk.xPosition, chunk.zPosition, chunk.worldObj, null, null );
					}
				}
			} catch( Exception e ) {
				System.out.println( "[EmasherCore] Error generating resorces for chunk @" + chunk.xPosition + ", " + chunk.zPosition );
			}
		}
	}
}
