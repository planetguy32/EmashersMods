package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEUAdapter extends BlockContainer
{

	public BlockEUAdapter(int id)
	{
		super(id, Material.iron);
		this.setCreativeTab(SocketsMod.tabSockets);
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new TileEUAdapter();
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		this.blockIcon = ir.registerIcon("sockets:euConverter");
	}

}

