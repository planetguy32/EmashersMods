package emasher.sockets.pipes;

import buildcraft.api.tools.IToolWrench;
import emasher.sockets.SocketsMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockEUAdapter extends BlockAdapterBase
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
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileEUAdapter();
	}
	
	@Override
	public void registerBlockIcons(IIconRegister ir)
	{
		this.blockIcon = ir.registerIcon("sockets:euConverter");
		this.outputIcon = ir.registerIcon("sockets:euAdapterOut");
	}

}

