package emasher.worldgeneration;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

import static net.minecraftforge.common.BiomeDictionary.Type;

public class WorldGenPondScum implements IWorldGenerator {
	@Override
	public void generate( Random var2, int var3, int var5, World var1, IChunkProvider provider, IChunkProvider provider2 ) {
		var3 *= 16;
		var5 *= 16;
		
		int startX, startY, startZ;
		startX = var3 + var2.nextInt( 16 );
		startZ = var5 + var2.nextInt( 16 );
		BiomeGenBase biome = var1.getWorldChunkManager().getBiomeGenAt( startX, startZ );
		
		boolean generate = false;
		
		generate = emasher.util.Config.spawnAlgae() && ( BiomeDictionary.isBiomeOfType( biome, Type.SWAMP ) || BiomeDictionary.isBiomeOfType( biome, Type.JUNGLE ) );
		
		if( generate ) {
			for( int i = 0; i < 20; i++ ) {
				
				startY = var1.getHeightValue( startX, startZ ) - 1;
				
				genScum( var1, var2, startX, startY, startZ, 0 );
			}
		}
		
		
	}
	
	private void genScum( World world, Random gen, int x, int y, int z, int depth ) {
		Block startId = world.getBlock( x, y, z );
		
		
		if( depth < 500 && startId == Blocks.water && world.isAirBlock( x, y + 1, z ) ) {

			world.setBlock( x, y + 1, z, emasher.blocks.Blocks.algae(), 0, 2 );

			
			if( gen.nextInt( 4 ) != 0 ) for( int i = 0; i < 3; i++ ) {
				int incX = gen.nextInt( 3 ) - 1;
				int incZ = gen.nextInt( 3 ) - 1;
				
				genScum( world, gen, x + incX, y, z + incZ, depth + 1 );
			}
		}
	}
}
