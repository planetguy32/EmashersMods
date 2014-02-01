package emasher.defense;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.effect.EntityWeatherEffect;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDeflector extends BlockPane
{
	public static Icon shieldHigh;
	public static Icon shieldLow;
	public static Random rand;

	protected BlockDeflector(int id)
	{
		super(id, "emasherdefense:deflector_edge", "emasherdefense:deflector_edge", Material.circuits, false);
		rand = new Random(System.nanoTime());
		Block.opaqueCubeLookup[id] = true;
		this.setTickRandomly(true);
		this.setCreativeTab(null);
	}
	
	@Override
	public void registerIcons(IconRegister register)
	{
		super.registerIcons(register);
		this.blockIcon = register.registerIcon("emasherdefense:deflector_edge");
		this.shieldHigh = register.registerIcon("emasherdefense:deflector_high");
		this.shieldLow = register.registerIcon("emasherdefense:deflector_low");
	}
	
	@Override
	public Icon getIcon(int side, int meta)
	{
		Icon result = shieldLow;
		
		if((meta & 8) == 8)
		{
			result = shieldHigh;
		}
		
		return result;
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
	public void onNeighborBlockChange(World world, int x, int y, int z, int id)
	{
		int lower = world.getBlockId(x, y - 1, z);
		
		if(lower == this.blockID || lower == EmasherDefense.deflectorBase.blockID)
		{
			if(lower == this.blockID)
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
