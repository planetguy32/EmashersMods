package emasher.worldgeneration;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Random;

import static net.minecraftforge.common.BiomeDictionary.Type;

public class WorldGenHemp implements IWorldGenerator {
	public WorldGenHemp() {

	}


	public void generate( Random random, int i, int k, World world, IChunkProvider provider, IChunkProvider provider2 ) {
		i *= 16;
		k *= 16;

		i += random.nextInt( 16 );
		k += random.nextInt( 16 );

		BiomeGenBase biome = world.getWorldChunkManager().getBiomeGenAt( i, k );

		if( BiomeDictionary.isBiomeOfType( biome, Type.SWAMP ) || BiomeDictionary.isBiomeOfType( biome, Type.FOREST ) || BiomeDictionary.isBiomeOfType( biome, Type.JUNGLE ) || BiomeDictionary.isBiomeOfType( biome, Type.PLAINS ) ) {
			if( !BiomeDictionary.isBiomeOfType( biome, Type.COLD ) ) {
				if( emasher.blocks.Blocks.hemp().canBlockStay( world, i, world.getHeightValue( i, k ), k ) && random.nextInt( 100 ) == 0 ) {
					world.setBlock( i, world.getHeightValue( i, k ), k, emasher.blocks.Blocks.hemp(), 0, 2 );
				}
			}
		}
	}
}