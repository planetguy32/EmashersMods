package emasher.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.ModuleRegistry;
import emasher.api.SocketModule;
import emasher.tileentities.TileSocket;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ItemBlockSocket extends ItemBlock {
	public ItemBlockSocket( Block block ) {
		super( block );
		setHasSubtypes( false );
	}
	
	@Override
	public int getMetadata( int damage ) {
		return 0;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public boolean hasEffect( ItemStack par1ItemStack, int pass ) {
		return par1ItemStack.getTagCompound() != null && pass == 0;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
	
	/*@Override
	@SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack par1ItemStack, int pass)
    {
        return par1ItemStack.getTagCompound() != null;
    }*/
	
	@Override
	public void addInformation( ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4 ) {
		NBTTagCompound data = par1ItemStack.getTagCompound();
		
		if( data != null ) for( int i = 0; i < 6; i++ ) {
			if( data.hasKey( "side" + i ) ) {
				int index = data.getInteger( "side" + i );
				if( index != 0 ) {
					SocketModule m = ModuleRegistry.getModule( index );

					par3List.add( EnumChatFormatting.AQUA + "- " + m.getLocalizedName() );
				}
			}
		}
	}
	
	@Override
	public boolean onItemUse( ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10 ) {
		Block b1 = par3World.getBlock( par4, par5, par6 );

		if( b1 == Blocks.snow_layer && ( par3World.getBlockMetadata( par4, par5, par6 ) & 7 ) < 1 ) {
			par7 = 1;
		} else if( b1 != Blocks.vine && b1 != Blocks.tallgrass && b1 != Blocks.deadbush
				&& !b1.isReplaceable( par3World, par4, par5, par6 ) ) {
			if( par7 == 0 ) {
				--par5;
			}

			if( par7 == 1 ) {
				++par5;
			}

			if( par7 == 2 ) {
				--par6;
			}

			if( par7 == 3 ) {
				++par6;
			}

			if( par7 == 4 ) {
				--par4;
			}

			if( par7 == 5 ) {
				++par4;
			}
		}

		if( par1ItemStack.stackSize == 0 ) {
			return false;
		} else if( !par2EntityPlayer.canPlayerEdit( par4, par5, par6, par7, par1ItemStack ) ) {
			return false;
		} else if( par5 == 255 && emasher.blocks.Blocks.socket().getMaterial().isSolid() ) {
			return false;
		} else if( par3World.canPlaceEntityOnSide( emasher.blocks.Blocks.socket(), par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack ) ) {
			Block block = emasher.blocks.Blocks.socket();
			int j1 = this.getMetadata( par1ItemStack.getItemDamage() );
			int k1 = emasher.blocks.Blocks.socket().onBlockPlaced( par3World, par4, par5, par6, par7, par8, par9, par10, j1 );

			if( placeBlockAt( par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1 ) ) {
				par3World.playSoundEffect( ( double ) ( ( float ) par4 + 0.5F ), ( double ) ( ( float ) par5 + 0.5F ), ( double ) ( ( float ) par6 + 0.5F ), block.stepSound.getBreakSound(), ( block.stepSound.getVolume() + 1.0F ) / 2.0F, block.stepSound.getPitch() * 0.8F );
				--par1ItemStack.stackSize;

				TileEntity t = par3World.getTileEntity( par4, par5, par6 );

				if( t != null && t instanceof TileSocket ) {
					TileSocket ts = ( TileSocket ) t;
					if( par1ItemStack.getTagCompound() != null ) ts.readFromNBT( par1ItemStack.getTagCompound() );
					ts.xCoord = par4;
					ts.yCoord = par5;
					ts.zCoord = par6;
					ts.setWorldObj( par3World );

					for( int i = 0; i < 6; i++ ) {
						SocketModule m = ts.getSide( ForgeDirection.getOrientation( i ) );
						m.onSocketPlaced( ts.getConfigForSide( ForgeDirection.getOrientation( i ) ), ts, ForgeDirection.getOrientation( i ) );
					}

				}
			}

			par3World.notifyBlockChange( par4, par5, par6, emasher.blocks.Blocks.socket() );

			return true;
		} else {
			return false;
		}
	}

}
