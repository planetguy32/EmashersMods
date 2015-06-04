package emasher.gas.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.Loader;
import emasher.gas.EmasherGas;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class WorldGenGasVent implements IWorldGenerator {
	public WorldGenGasVent() {

	}


	public void generate( Random random, int i, int k, World world, IChunkProvider provider, IChunkProvider provider2 ) {
		int x = i * 16;
		int z = k * 16;
		
		
		try {
			if( world.getBlock( x + 4, 0, z ) == Blocks.bedrock && world.getWorldInfo().getTerrainType() != WorldType.FLAT ) {
				if( world.getBlockMetadata( x + 4, 0, z ) == 0 ) {
					for( int it = 0; it < 1; it++ ) {

						world.setBlockMetadataWithNotify( x + 4, 0, z, 0x1, 2 );

						x = i * 16;
						z = k * 16;

						int y;

						if( world.provider.dimensionId == -1 ) {
							y = EmasherGas.flatNetherBedrockTop;
							if( !EmasherGas.flatNetherBedrock ) y = random.nextInt( 2 ) + 3;
						} else {
							y = EmasherGas.flatBedrockTop;
							if( !EmasherGas.flatBedrock ) y = random.nextInt( 2 ) + 3;
						}

						x += random.nextInt( 16 );
						z += random.nextInt( 16 );

						if( world.provider.dimensionId != -1 ) {
							if( random.nextInt( 8 ) == 0 ) {
								world.setBlock( x, y, z, EmasherGas.shaleResource, 0, 2 );
							} else if( Loader.isModLoaded( "BuildCraft|Energy" ) && random.nextInt( 12 ) == 0 ) {
								world.setBlock( x, y, z, EmasherGas.shaleResource, 1, 2 );
							}
						} else {
							if( random.nextInt( 6 ) == 0 ) world.setBlock( x, y, z, EmasherGas.shaleResource, 2, 2 );
						}


					}


				}
			}
		} catch( Exception e ) {
			System.out.println( "[GasCraft] Error generating shale resorces for chunk @" + i + ", " + k );
		}


	}
}