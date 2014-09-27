package emasher.defense;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDeflector extends BlockPane
{
	public static IIcon shieldHigh;
	public static IIcon shieldLow;
	public static Random rand;

	protected BlockDeflector(int id)
	{
		super("emasherdefense:deflector_edge", "emasherdefense:deflector_edge", Material.circuits, false);
		rand = new Random(System.nanoTime());
		this.setTickRandomly(true);
		this.setCreativeTab(null);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister register)
	{
		super.registerBlockIcons(register);
		this.blockIcon = register.registerIcon("emasherdefense:deflector_edge");
		this.shieldHigh = register.registerIcon("emasherdefense:deflector_high");
		this.shieldLow = register.registerIcon("emasherdefense:deflector_low");
	}
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		IIcon result = shieldLow;
		
		if((meta & 8) == 8)
		{
			result = shieldHigh;
		}
		
		return result;
	}

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity)
	{	
		if(entity != null && entity instanceof IProjectile)
		{
			world.removeEntity(entity);
		}
		
		if(entity != null && entity instanceof EntityMob)
		{
			EntityMob ent = (EntityMob)entity;
			ent.attackEntityFrom(DamageSource.inWall, 15);
		}
		
		
		if(entity!= null && ! (entity instanceof EntityWeatherEffect))
		{
			int meta = world.getBlockMetadata(x, y , z);
			meta |= 0x8;
			world.setBlockMetadataWithNotify(x, y, z, meta, 2);
			
			world.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "fire.ignite", 1.0F,rand.nextFloat() * 0.4F + 0.8F);
		}
	}
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
	{
		Block lower = world.getBlock(x, y - 1, z);
		
		if(lower == this || lower == EmasherDefense.deflectorBase)
		{
			if(lower == this)
			{
				int meta = world.getBlockMetadata(x, y - 1, z);
				
				if((meta & 7) <= 0)
				{
					world.setBlockToAir(x, y, z);
				}
					
			}
		}
		else
		{
			world.setBlockToAir(x, y, z);
		}
	}
	
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		int meta = world.getBlockMetadata(x, y, z);
		
		meta &= 7;
		
		world.setBlockMetadataWithNotify(x, y, z, meta, 2);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }
	
}
