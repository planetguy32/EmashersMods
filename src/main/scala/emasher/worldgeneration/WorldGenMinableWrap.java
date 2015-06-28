package emasher.worldgeneration;

import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class WorldGenMinableWrap {
	public WorldGenMinableSafe gen;
	public int num;
	public int min;
	public int max;
	public ArrayList<BiomeDictionary.Type> biomes = new ArrayList<BiomeDictionary.Type>();
	
	public WorldGenMinableWrap( WorldGenMinableSafe g, int n, int mi, int ma ) {
		gen = g;
		num = n;
		min = mi;
		max = ma;
	}
	
	public WorldGenMinableWrap add( BiomeDictionary.Type theBiome ) {
		biomes.add( theBiome );
		return this;
	}
}
