package emasher.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.blocks.BlockSocket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModTankDisplay extends SocketModule {

	public ModTankDisplay( int id ) {
		super( id, "eng_toolbox:tankDisplay", "eng_toolbox:tankOverlay" );
	}

	public static ItemStack consumeItem( ItemStack stack ) {
		if( stack.stackSize == 1 ) {
			if( stack.getItem().hasContainerItem( stack ) ) {
				return stack.getItem().getContainerItem( stack );
			} else {
				return null;
			}
		} else {
			stack.splitStack( 1 );

			return stack;
		}
	}
	
	@Override
	public String getLocalizedName() {
		return "Tank Interface";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Displays an internal tank" );
		l.add( "Allows players to fill and empty fluid" );
		l.add( "containersby right clicking" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to display" );
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ggg", "slr", " b ", Character.valueOf( 'g' ), Blocks.glass_pane, Character.valueOf( 's' ),
				Items.glowstone_dust, Character.valueOf( 'l' ), "dyeBlue", Character.valueOf( 'r' ), Items.redstone, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}
	
	@Override
	public boolean hasTankIndicator() {
		return true;
	}
	
	@Override
	public void onTankChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add ) {
		if( index == config.tank ) {
			ts.sendClientTankSlot( index );
		}
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public int getTankToRender( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		return config.tank;
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public IIcon[] getAdditionalOverlays( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		return new IIcon[] {( ( BlockSocket ) emasher.blocks.Blocks.socket() ).textures[moduleID][1]};
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public boolean flipBottomOverlay() {
		return true;
	}
	
	@Override
	public void onSideActivated( SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player ) {
		ItemStack is = player.getCurrentEquippedItem();

		if( config.tank != -1 && is != null ) {
			FluidStack inContainer = FluidContainerRegistry.getFluidForFilledItem( is );

			if( inContainer != null ) {

				int amnt = ts.fillInternal( config.tank, inContainer, false );

				if( amnt == inContainer.amount ) {
					ts.fillInternal( config.tank, inContainer, true );
					if( FluidContainerRegistry.isBucket( is ) ) {
						//TODO Check if it works this way
						// is.itemID = Item.bucketEmpty.itemID;
						is.getItem().setContainerItem( Items.bucket );
						is.setItemDamage( 0 );
						is.stackSize = 1;
						is.setTagCompound( null );
					} else {
						is.stackSize--;
					}
				}
			} else {
				FluidStack available = ts.getFluidInTank( config.tank );
				if( available != null ) {
					ItemStack filled = FluidContainerRegistry.fillFluidContainer( available, is );

					FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem( filled );

					if( liquid != null ) {
						if (!player.capabilities.isCreativeMode) {
							if( is.stackSize > 1 ) {
								if( player.inventory.addItemStackToInventory( filled ) ) {
									player.inventory.setInventorySlotContents( player.inventory.currentItem, consumeItem( is ) );
								}
							} else {
								player.inventory.setInventorySlotContents( player.inventory.currentItem, consumeItem( is ) );
								player.inventory.setInventorySlotContents( player.inventory.currentItem, filled );
							}
						}
						//tank.drain(ForgeDirection.UNKNOWN, liquid.amount, true);
						ts.drainInternal( config.tank, liquid.amount, true );
					}
				}
			}
		}
	}
	
	@Override
	public void indicatorUpdated( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.tank != -1 ) ts.sendClientTankSlot( config.tank );
	}
	
}
