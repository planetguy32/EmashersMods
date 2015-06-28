package emasher.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.PhotobioReactorRecipeRegistry;
import emasher.api.PhotobioReactorRecipeRegistry.PhotobioReactorRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModPhotobioReactor extends SocketModule {
	public ModPhotobioReactor( int id ) {
		super( id, "eng_toolbox:photobioReactor" );
	}

	@Override
	public String getLocalizedName() {
		return "Photobioreactor";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Converts fluids to other fluids when combined with" );
		l.add( "items that are sensitive to light" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Input tank" );
		l.add( emasher.util.Config.PREF_GREEN() + "Input inventory" );
		l.add( emasher.util.Config.PREF_YELLOW() + "Outputs to Machine Output" );
		l.add( "Cannot be installed on a socket with other machines" );
		l.add( "Requires sunlight to operate" );
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ggg", "u u", " b ", Character.valueOf( 'h' ), Blocks.hopper, Character.valueOf( 'u' ), Items.bucket,
				Character.valueOf( 'g' ), Blocks.glass_pane, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}
	
	@Override
	public boolean hasTankIndicator() {
		return true;
	}
	
	@Override
	public boolean hasInventoryIndicator() {
		return true;
	}
	
	@Override
	public void onRemoved( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
	}
	
	@Override
	public boolean isMachine() {
		return true;
	}
	
	@Override
	public boolean canBeInstalled( SocketTileAccess ts, ForgeDirection side ) {
		if( side != ForgeDirection.UP ) return false;
		
		for( int i = 0; i < 6; i++ ) {
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( i ) );
			if( m != null && m.isMachine() ) return false;
		}
		
		return true;
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		boolean updateClient = false;
		if( config.tank >= 0 && config.tank <= 2 && config.inventory >= 0 && config.inventory <= 2 && side == ForgeDirection.UP ) {
			if( ts.sideInventory.getStackInSlot( side.ordinal() ) == null ) {
				if( ts.getFluidInTank( config.tank ) != null && ts.getStackInInventorySlot( config.inventory ) != null ) {
					FluidStack toIntakeFluid = ts.getFluidInTank( config.tank );
					ItemStack toIntake = ts.getStackInInventorySlot( config.inventory );
					FluidStack product = null;
					
					PhotobioReactorRecipe r = PhotobioReactorRecipeRegistry.getRecipe( toIntake, toIntakeFluid );
					if( r != null ) product = r.getOutput();
					
					if( product != null && r.getFluidInput().amount <= toIntakeFluid.amount ) {
						ts.extractItemInternal( true, config.inventory, 1 );
						ts.drainInternal( config.tank, r.getFluidInput().amount, true );
						ts.sideInventory.setInventorySlotContents( side.ordinal(), fluidToItem( product ) );
						config.meta = 400;
						config.rsControl[0] = false;
						updateClient = true;
					}
				}
			} else if( ts.getWorldObj().getBlockLightValue( ts.xCoord, ts.yCoord + 1, ts.zCoord ) > 14 && config.meta > 0 ) {
				config.meta--;
				if( config.meta == 0 ) updateClient = true;
				if( !config.rsControl[0] && config.meta > 0 ) {
					config.rsControl[0] = true;
					updateClient = true;
				}
			} else {
				if( config.rsControl[0] ) {
					config.rsControl[0] = false;
					updateClient = true;
				}
			}
			
			if( config.meta == 0 && ts.sideInventory.getStackInSlot( side.ordinal() ) != null ) {
				
				FluidStack f = itemToFluid( ts.sideInventory.getStackInSlot( side.ordinal() ) );
				int num = ts.forceOutputFluid( f );
				if( num < f.amount ) {
					ts.sideInventory.setInventorySlotContents( side.ordinal(), new ItemStack( f.getFluid().getBlock(), 1, f.amount - num ) );
				} else {
					ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
				}
				
			}
			if( updateClient ) ts.sendClientSideState( side.ordinal() );
		}
	}

	private ItemStack fluidToItem( FluidStack f ) {
		return new ItemStack( f.getFluid().getBlock(), 1, f.amount );
	}
	
	private FluidStack itemToFluid( ItemStack i ) {
		Block b = Block.getBlockFromItem( i.getItem() );
		if( !( b instanceof IFluidBlock ) )
			return null;
		return new FluidStack( ( ( IFluidBlock ) b ).getFluid(), i.getItemDamage() );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.meta == 0 || !config.rsControl[0] ) return "eng_toolbox:inner_black";
		return "eng_toolbox:inner_photobio";
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"eng_toolbox:inner_photobio"};
	}
}
