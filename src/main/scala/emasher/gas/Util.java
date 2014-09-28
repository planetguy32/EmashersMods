package emasher.gas;

import emasher.gas.tileentity.TileGas;

public class Util
{
	public static int bucketToBlock(int bucketAmount)
	{
		return (bucketAmount * 15) / 1000;
	}
	
	public static int blockToBucket(int blockAmount)
	{
		return (blockAmount * 1000) / 15;
	}
	
	public static int blockToVial(int BlockAmount)
	{
		return 16 - BlockAmount;
	}
	
	public static int vialToBlock(int vialAmount)
	{
		return 16 - vialAmount;
	}
	
	public static int entityToBlock(int entAmount)
	{
		return (entAmount * 15)/TileGas.VOLUME;
	}
}
