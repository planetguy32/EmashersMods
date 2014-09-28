package emasher.core.block;

import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.minecraft.block.Block;

import java.util.Random;

public class BlockSuperAlgae extends BlockPondScum
{
    public BlockSuperAlgae()
    {
        super();
    }

    @Override
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon =  par1IconRegister.registerIcon("emashercore:superAlgae");
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        int meta = world.getBlockMetadata(x, y, z);
        Block underID = world.getBlock(x, y - 1, z);
        if(world.getBlockLightValue(x, y, z) >= 14)
        {
            if(meta >= GROWTH_TIME || (underID == EmasherCore.nutrientWater && meta >= GROWTH_TIME / 2))
            {
                int xInc, zInc;
                boolean canPlace = false;
                int tries = 0;

                do
                {
                    xInc = random.nextInt(3) - 1;
                    zInc = random.nextInt(3) - 1;
                    canPlace = world.isAirBlock(x + xInc, y, z + zInc) && canPlaceBlockOn(world.getBlock(x + xInc, y - 1, z + zInc));

                    tries++;
                }
                while(!(xInc == 0 && zInc == 0) && !canPlace && tries <= 9);

                int tx = x + xInc;
                int tz = z + zInc;

                int ty = y - 1;
                int i = 0;

                while(canPlaceBlockOn(world.getBlock(tx, ty, tz)) && i < EmasherCore.algaeDepth)
                {
                    ty--;
                    i++;
                }

                if(i >= EmasherCore.algaeDepth)
                {
                    canPlace = false;
                }

                if(canPlace)
                {
                    Block toPlaceId = world.getBlock(x + xInc, y - 1, z + zInc);
                    if(toPlaceId == EmasherCore.nutrientWater)
                    {
                        if(world.rand.nextInt(100) < 98) world.setBlock(x + xInc, y, z + zInc, EmasherCore.superAlgae, 0, 3);
                        else world.setBlock(x + xInc, y, z + zInc, EmasherCore.algae, 0, 3);
                    }
                    else
                    {
                        if(world.rand.nextInt(100) < 75) world.setBlock(x + xInc, y, z + zInc, EmasherCore.superAlgae, 0, 3);
                        else world.setBlock(x + xInc, y, z + zInc, EmasherCore.algae, 0, 3);
                    }
                    world.setBlockMetadataWithNotify(x, y, z, 0, 2);
                }
            }
            else
            {
                world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
            }

        }

    }
}
