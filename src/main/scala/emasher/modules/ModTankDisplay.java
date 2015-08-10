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
import net.minecraftforge.fluids.Fluid;
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
			if( FluidContainerRegistry.isEmptyContainer( is ) ) {
				FluidStack fs = ts.getFluidInTank( config.tank );
				if( fs != null && fs.amount > 0 ) {
					ItemStack fillContainer = is.copy().splitStack( 1 );
					FluidStack fillFluid = fs.copy( );
					fillFluid.amount = FluidContainerRegistry.getContainerCapacity( fillFluid, fillContainer );
					if( fillFluid.amount <= fs.amount ) {
						ItemStack result = FluidContainerRegistry.fillFluidContainer( fillFluid, fillContainer );
						if( result != null ) {
							if( ! player.capabilities.isCreativeMode ) {
								ts.drainInternal( config.tank, fillFluid.amount, true );
								is.stackSize--;
							}
							player.inventory.addItemStackToInventory( result );
						}
					}
				}
			} else if( FluidContainerRegistry.isFilledContainer( is ) ) {
				FluidStack containerStack = FluidContainerRegistry.getFluidForFilledItem( is );
				ItemStack drainedContainer = FluidContainerRegistry.drainFluidContainer( is.copy( ) );
				if( containerStack != null ) {
					if( ts.fillInternal( config.tank, containerStack, false ) == containerStack.amount ) {
						if( ! player.capabilities.isCreativeMode ) {
							is.stackSize--;
						}
						ts.fillInternal( config.tank, containerStack, true );
						if( drainedContainer != null && ! player.capabilities.isCreativeMode ) {
							player.inventory.addItemStackToInventory( drainedContainer );
						}
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
