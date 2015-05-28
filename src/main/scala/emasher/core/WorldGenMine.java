package emasher.core;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Random;


public class WorldGenMine implements IWorldGenerator {
	
	public ArrayList<WorldGenMinableWrap> lst = new ArrayList<WorldGenMinableWrap>();
	public int chunk_X;
	public int chunk_Z;
	public Random randomGenerator;
	public World currentWorld;

	@Override
	public void generate( Random random, int chunkX, int chunkZ, World world,
						  IChunkProvider chunkGenerator, IChunkProvider chunkProvider ) {
		
		currentWorld = world;
		chunk_X = chunkX * 16;
		chunk_Z = chunkZ * 16;
		randomGenerator = random;
		
		for( WorldGenMinableWrap m : lst ) {
			BiomeGenBase biome = world.getBiomeGenForCoords( chunkX * 16, chunkZ * 16 );
			
			if( m.biomes.size() == 0 ) genStandardOre( m.num, m.gen, m.min, m.max );
			else for( BiomeDictionary.Type t : m.biomes ) {
				if( BiomeDictionary.isBiomeOfType( biome, t ) ) {
					genStandardOre( m.num, m.gen, m.min, m.max );
					
					break;
				}
			}
		}
		
		this.randomGenerator = null;
		this.currentWorld = null;
	}
	
	private void genStandardOre( int par1, WorldGenerator par2WorldGenerator, int par3, int par4 ) {
		//System.out.println("gen");
		
		for( int l = 0; l < par1; ++l ) {
			int i1 = this.chunk_X + this.randomGenerator.nextInt( 16 );
			int j1 = this.randomGenerator.nextInt( par4 - par3 ) + par3;
			int k1 = this.chunk_Z + this.randomGenerator.nextInt( 16 );
			par2WorldGenerator.generate( this.currentWorld, this.randomGenerator, i1, j1, k1 );
		}
	}
	
	public void add( WorldGenMinableWrap data ) {
		lst.add( data );
	}

}
