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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDirectionChanger extends BlockContainer {

	public IIcon upIcon;
	public IIcon downIcon;
	public IIcon leftIcon;
	public IIcon rightIcon;

	public BlockDirectionChanger() {
		super( Material.circuits );
		setCreativeTab( SocketsMod.tabSockets );
	}

	public TileEntity createNewTileEntity( World world, int metadata ) {
		return new TileDirectionChanger();
	}

	@Override
	public boolean onBlockActivated( World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ ) {
		if( !world.isRemote ) {
			ItemStack is = player.getCurrentEquippedItem();
			Item item = null;
			if( is != null ) item = is.getItem();
			if( item != null && item instanceof IToolWrench ) {
				TileEntity te = world.getTileEntity( x, y, z );
				if( te != null && te instanceof TileDirectionChanger ) {
					( ( TileDirectionChanger ) te ).nextDirection( ForgeDirection.getOrientation( side ) );
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public IIcon getIcon( IBlockAccess world, int x, int y, int z, int side ) {
		TileEntity te = world.getTileEntity( x, y, z );
		if( te != null && te instanceof TileDirectionChanger ) {
			TileDirectionChanger td = ( TileDirectionChanger ) te;
			switch(td.directions[side]) {
				case UP:
					return upIcon;
				case DOWN:
					return downIcon;
				case NORTH:
					return upIcon;
				case SOUTH:
					return downIcon;
				case EAST:
					return rightIcon;
				case WEST:
					return leftIcon;
				default:
					return blockIcon;
			}
		}

		return blockIcon;
	}

	@Override
	public void registerBlockIcons( IIconRegister ir ) {
		blockIcon = ir.registerIcon( "sockets:dirChange" );
		upIcon = ir.registerIcon( "sockets:dirChangeUp" );
		downIcon = ir.registerIcon( "sockets:dirChangeDown" );
		rightIcon = ir.registerIcon( "sockets:dirChangeRight" );
		leftIcon = ir.registerIcon( "sockets:dirChangeLeft" );
	}

}
