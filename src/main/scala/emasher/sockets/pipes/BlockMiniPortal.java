package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class BlockMiniPortal extends BlockAdapterBase
{

	public BlockMiniPortal()
	{
		super(Material.rock);
		setCreativeTab(SocketsMod.tabSockets);
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileMiniPortal();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir)
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
					if(nether.getTileEntity(x / 8, y, z / 8) == null)
					{
						nether.setBlock(x / 8, y, z / 8, this);
						setTEPartners(world, x, y, z);
					}
					else
					{
						this.dropBlockAsItem(world, x, y, z, new ItemStack(this));
						world.setBlock(x, y, z, Blocks.air);
					}
				}
			}
		}
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
		if(MinecraftServer.getServer() == null || ! MinecraftServer.getServer().getAllowNether()) return false;
		if(world.provider.dimensionId == 0)
		{
			World nether = MinecraftServer.getServer().worldServerForDimension(-1);
			if(nether.getTileEntity(x / 8, y, z / 8) == null)
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
				TileEntity partner = nether.getTileEntity(x / 8, y, z / 8);
				if(partner != null && partner instanceof TileMiniPortal)
				{
					((TileMiniPortal)partner).setPartner(x, y, z);
					
					TileEntity te = world.getTileEntity(x, y, z);
					if(te != null && te instanceof TileMiniPortal)
					{
						((TileMiniPortal)te).setPartner(x / 8, y, z / 8);
					}
				}
			}
		}
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		if(world.provider.dimensionId == 0)
		{
			if(MinecraftServer.getServer().getAllowNether())
			{
				World nether = MinecraftServer.getServer().worldServerForDimension(-1);
				if(nether.getBlock(x / 8, y, z / 8) instanceof BlockMiniPortal)
				{
					nether.setBlock(x / 8, y, z / 8, Blocks.air);
					nether.removeTileEntity(x / 8, y, z / 8);
				}
			}
		}
		else if(world.provider.dimensionId == -1)
		{
			TileEntity te = world.getTileEntity(x, y, z);
			if(te != null && te instanceof TileMiniPortal)
			{
				int oX = ((TileMiniPortal)te).partnerX;
				int oY = ((TileMiniPortal)te).partnerY;
				int oZ = ((TileMiniPortal)te).partnerZ;
				
				World over = MinecraftServer.getServer().worldServerForDimension(0);
				if(over.getBlock(oX, oY, oZ) instanceof BlockMiniPortal)
				{
					over.setBlock(oX, oY, oZ, Blocks.air);
					over.removeTileEntity(oX, oY, oZ);
				}
			}
		}
	}
	
}
