package emasher.core.hemp;

import emasher.core.EmasherCore;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemHempSeeds extends Item {
	private Block spawnID;

	public ItemHempSeeds( Block block ) {
		super();
		spawnID = block;
		
		this.setCreativeTab( EmasherCore.tabEmasher );
		//setIconIndex(4);
		setUnlocalizedName( "hempSeeds" );
	}
	
	public boolean onItemUse( ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10 ) {
		Block var11 = par3World.getBlock( par4, par5, par6 );

		if( var11 == Blocks.snow ) {
			par7 = 1;
		} else if( var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush ) {
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

		if( !par2EntityPlayer.canPlayerEdit( par4, par5, par6, par7, par1ItemStack ) ) {
			return false;
		} else if( par1ItemStack.stackSize == 0 ) {
			return false;
		} else {
			if( par3World.canPlaceEntityOnSide( this.spawnID, par4, par5, par6, false, par7, ( Entity ) null, par1ItemStack ) ) {
				Block var12 = spawnID;
				int var13 = var12.onBlockPlaced( par3World, par4, par5, par6, par7, par8, par9, par10, 0 );

				if( par3World.setBlock( par4, par5, par6, this.spawnID, var13, 2 ) ) {
					if( par3World.getBlock( par4, par5, par6 ) == this.spawnID ) {
						spawnID.onBlockPlacedBy( par3World, par4, par5, par6, par2EntityPlayer, par1ItemStack );
						spawnID.onPostBlockPlaced( par3World, par4, par5, par6, var13 );
					}

					par3World.playSoundEffect( ( double ) ( ( float ) par4 + 0.5F ), ( double ) ( ( float ) par5 + 0.5F ), ( double ) ( ( float ) par6 + 0.5F ), var12.stepSound.getBreakSound(), ( var12.stepSound.getVolume() + 1.0F ) / 2.0F, var12.stepSound.getPitch() * 0.8F );
					--par1ItemStack.stackSize;
				}
			}

			return true;
		}
	}
	
	@Override
	public void registerIcons( IIconRegister iconRegister ) {
		itemIcon = iconRegister.registerIcon( "emashercore:hempSeeds" );
	}
}