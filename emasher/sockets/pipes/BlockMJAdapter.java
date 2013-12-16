package emasher.sockets.pipes;

import emasher.sockets.SocketsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockMJAdapter extends BlockAdapterBase
{	
	public BlockMJAdapter(int id)
	{
		super(id);
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
		return new TileMJAdapter();
	}
	
	@Override
	public void registerIcons(IconRegister ir)
	{
		this.blockIcon = ir.registerIcon("sockets:mjConverter");
		this.outputIcon = ir.registerIcon("sockets:mjAdapterOut");
	}

}
