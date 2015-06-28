package emasher.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRSWand extends Item {

	public ItemRSWand() {
		super();
		
		this.setCreativeTab( EngineersToolbox.tabItems() );
		this.setMaxStackSize( 1 );
		this.setUnlocalizedName( "redstone_wand" );
		this.setMaxDamage( 64 );
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister ir ) {
		this.itemIcon = ir.registerIcon( "eng_toolbox:redstoneStaff" );
	}
	
	@Override
	public boolean isItemTool( ItemStack par1ItemStack ) {
		return true;
	}
	
	@Override
	public boolean onItemUse( ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10 ) {
		//int i1 = par3World.getBlockId(par4, par5, par6);
		Block i1 = par3World.getBlock( par4, par5, par6 );

		if( i1 == Blocks.snow_layer && ( par3World.getBlockMetadata( par4, par5, par6 ) & 7 ) < 1 ) {
			par7 = 1;
		} else if( i1 != Blocks.vine && i1 != Blocks.tallgrass && i1 != Blocks.deadbush
				//&& (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(par3World, par4, par5, par6)))
				&& ( !Block.blockRegistry.containsId( Block.getIdFromBlock( i1 ) ) || !i1.isReplaceable( par3World, par4, par5, par6 ) ) ) {
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
		} else if( par5 == 255 && emasher.blocks.Blocks.tempRS().getMaterial().isSolid() ) {
			return false;
		} else if( par3World.canPlaceEntityOnSide( emasher.blocks.Blocks.socket(), par4, par5, par6, false, par7, par2EntityPlayer, par1ItemStack ) ) {
			Block block = emasher.blocks.Blocks.tempRS();
			int j1 = 0;
			int k1 = emasher.blocks.Blocks.tempRS().onBlockPlaced( par3World, par4, par5, par6, par7, par8, par9, par10, j1 );

			if( placeBlockAt( par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, k1 ) ) {
				par3World.playSoundEffect( ( double ) ( ( float ) par4 + 0.5F ), ( double ) ( ( float ) par5 + 0.5F ), ( double ) ( ( float ) par6 + 0.5F ), block.stepSound.getBreakSound(), ( block.stepSound.getVolume() + 1.0F ) / 2.0F, block.stepSound.getPitch() * 0.8F );
				par1ItemStack.damageItem( 1, par2EntityPlayer );
			}

			return true;
		} else {
			return false;
		}
	}
	
	public boolean placeBlockAt( ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata ) {
		if( !world.setBlock( x, y, z, emasher.blocks.Blocks.tempRS(), metadata, 3 ) ) {
			return false;
		}

		if( world.getBlock( x, y, z ) == emasher.blocks.Blocks.tempRS() ) {
			emasher.blocks.Blocks.tempRS().onBlockPlacedBy( world, x, y, z, player, stack );
			emasher.blocks.Blocks.tempRS().onPostBlockPlaced( world, x, y, z, metadata );
		}

		return true;
	}

}
