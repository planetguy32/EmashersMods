package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockMiniPortal extends BlockAdapterBase
{

	public BlockMiniPortal(int id)
	{
		super(id, Material.rock);
		setCreativeTab(SocketsMod.tabSockets);
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileMiniPortal();
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		blockIcon = ir.registerIcon("sockets:miniPortal");
		outputIcon = ir.registerIcon("sockets:miniPortalOut");
	}
	
	@Override
	public void onBlockAdded(World world, int x, int y, int z)
	{
		if(! world.isRemote)
		{
			if(world.provider.dimensionId == 0)
			{
				if(MinecraftServer.getServer().getAllowNether())
				{
					World nether = MinecraftServer.getServer().worldServerForDimension(-1);
					if(nether.getBlockTileEntity(x / 16, y, z / 16) == null)
					{
						nether.setBlock(x / 16, y, z / 16, this.blockID);
						setTEPartners(world, x, y, z);
					}
					else
					{
						this.dropBlockAsItem_do(world, x, y, z, new ItemStack(this));
						world.setBlock(x, y, z, 0);
					}
				}
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		if(! MinecraftServer.getServer().getAllowNether()) return false;
		if(world.provider.dimensionId == 0)
		{
			World nether = MinecraftServer.getServer().worldServerForDimension(-1);
			if(nether.getBlockTileEntity(x / 16, y, z / 16) == null)
			{
				if(super.canPlaceBlockAt(world, x, y, z)) return true;
			}
		}
		
		return false;
	}
	
	public void setTEPartners(World world, int x, int y, int z)
	{
		if(world.provider.dimensionId == 0)
		{
			if(MinecraftServer.getServer().getAllowNether())
			{
				World nether = MinecraftServer.getServer().worldServerForDimension(-1);
				TileEntity partner = nether.getBlockTileEntity(x/16, y, z/16);
				if(partner != null && partner instanceof TileMiniPortal)
				{
					((TileMiniPortal)partner).setPartner(x, y, z);
					
					TileEntity te = world.getBlockTileEntity(x, y, z);
					if(te != null && te instanceof TileMiniPortal)
					{
						((TileMiniPortal)te).setPartner(x / 16, y, z / 16);
					}
				}
			}
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, int id, int meta)
	{
		if(world.provider.dimensionId == 0)
		{
			if(MinecraftServer.getServer().getAllowNether())
			{
				World nether = MinecraftServer.getServer().worldServerForDimension(-1);
				if(nether.getBlockId(x / 16, y, z / 16) == this.blockID)
				{
					nether.setBlock(x / 16, y, z / 16, 0);
					nether.removeBlockTileEntity(x / 16, y, z / 16);
				}
			}
		}
		else if(world.provider.dimensionId == -1)
		{
			TileEntity te = world.getBlockTileEntity(x, y, z);
			if(te != null && te instanceof TileMiniPortal)
			{
				int oX = ((TileMiniPortal)te).partnerX;
				int oY = ((TileMiniPortal)te).partnerY;
				int oZ = ((TileMiniPortal)te).partnerZ;
				
				World over = MinecraftServer.getServer().worldServerForDimension(0);
				if(over.getBlockId(oX, oY, oZ) == this.blockID)
				{
					over.setBlock(oX, oY, oZ, 0);
					over.removeBlockTileEntity(oX, oY, oZ);
				}
			}
		}
	}
	
}
