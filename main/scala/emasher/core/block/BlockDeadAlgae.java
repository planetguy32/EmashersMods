package emasher.core.block;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.world.World;

import java.util.Random;

public class BlockDeadAlgae extends BlockPondScum{

    public static final int GROWTH_TIME_DEAD = 3;

    public BlockDeadAlgae(int id)
    {
        super(id);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.blockIcon =  par1IconRegister.registerIcon("emashercore:deadAlgae");
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random)
    {
        int meta = world.getBlockMetadata(x, y, z);

        if(meta >= GROWTH_TIME_DEAD)
        {
            world.setBlockToAir(x, y, z);
        }
        else
        {
            world.setBlockMetadataWithNotify(x, y, z, meta + 1, 2);
        }

    }

}
