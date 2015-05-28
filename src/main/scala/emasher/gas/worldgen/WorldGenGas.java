package emasher.gas.worldgen;

import cpw.mods.fml.common.IWorldGenerator;
import emasher.gas.EmasherGas;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.Random;

public class WorldGenGas implements IWorldGenerator {
	public WorldGenGas() {

	}


	public void generate( Random random, int i, int k, World world, IChunkProvider provider, IChunkProvider provider2 ) {
		if( random.nextInt( 8 ) == 0 ) {
			int y = random.nextInt( 20 ) + 5;
			int x = i * 16;
			int z = k * 16;

			x += random.nextInt( 16 );
			z += random.nextInt( 16 );

			if( world.provider.dimensionId != -1 ) {
				if( world.getBlock( x, y, z ) == Blocks.stone ) world.setBlock( x, y, z, EmasherGas.gasPocket );
			} else {
				if( world.getBlock( x, y, z ) == Blocks.netherrack ) world.setBlock( x, y, z, EmasherGas.plasmaPocket );
			}

		}

	}
}