package emasher.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.MixerRecipeRegistry;
import emasher.api.MixerRecipeRegistry.MixerRecipe;
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

public class ModMixer extends SocketModule {
	public ModMixer( int id ) {
		super( id, "eng_toolbox:mixer", "eng_toolbox:mixerActive" );
	}

	@Override
	public String getLocalizedName() {
		return "Mixer";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Used to mix items and fluids" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Input Tank" );
		l.add( emasher.util.Config.PREF_GREEN() + "Input Inventory" );
		l.add( emasher.util.Config.PREF_YELLOW() + "Outputs to Machine Output" );
		l.add( emasher.util.Config.PREF_AQUA() + "Requires 10 RF/tick" );
		l.add( "Cannot be installed on a socket with other machines" );
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), " h ", "udu", " b ", Character.valueOf( 'h' ), Blocks.hopper, Character.valueOf( 'u' ), Items.bucket,
				Character.valueOf( 'd' ), Blocks.dispenser, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
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
	public boolean isMachine() {
		return true;
	}
	
	@Override
	public boolean canBeInstalled( SocketTileAccess ts, ForgeDirection side ) {
		for( int i = 0; i < 6; i++ ) {
			SocketModule m = ts.getSide( ForgeDirection.getOrientation( i ) );
			if( m != null && m.isMachine() ) return false;
		}
		
		return true;
	}
	
	@Override
	public void onRemoved( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		boolean updateClient = false;
		if( config.tank >= 0 && config.tank <= 2 && config.inventory >= 0 && config.inventory <= 2 ) {
			if( ts.sideInventory.getStackInSlot( side.ordinal() ) == null ) {
				if( ts.getFluidInTank( config.tank ) != null && ts.getStackInInventorySlot( config.inventory ) != null ) {
					FluidStack toIntakeFluid = ts.getFluidInTank( config.tank );
					ItemStack toIntake = ts.getStackInInventorySlot( config.inventory );
					FluidStack product = null;
					
					MixerRecipe r = MixerRecipeRegistry.getRecipe( toIntake, toIntakeFluid );
					if( r != null ) product = r.getOutput();
					
					
					if( product != null && r.getFluidInput().amount <= toIntakeFluid.amount ) {
						ts.extractItemInternal( true, config.inventory, 1 );
						ts.drainInternal( config.tank, r.getFluidInput().amount, true );
						
						ts.sideInventory.setInventorySlotContents( side.ordinal(), fluidToItem( product ) );
						config.meta = 40;
						config.rsControl[0] = false;
						updateClient = true;
					}
				}
			} else if( ts.useEnergy( 10, true ) >= 10 && config.meta > 0 ) {
				ts.useEnergy( 10, false );
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
					//TODO Check Fluid things
					//ts.sideInventory.setInventorySlotContents(side.ordinal(), new ItemStack(f.fluidID, 1, f.amount - num));
					ts.sideInventory.setInventorySlotContents( side.ordinal(), new ItemStack( f.getFluid().getBlock(), 1, f.amount - num ) );
				} else {
					ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
				}
				
			}
			if( updateClient ) ts.sendClientSideState( side.ordinal() );
		}
	}
	
	private ItemStack fluidToItem( FluidStack f ) {
		//TODO Check what this is used for to see if it works this way or not
		//return new ItemStack(f.fluidID, 1, f.amount);
		return new ItemStack( f.getFluid().getBlock(), 1, f.amount );
	}
	
	private FluidStack itemToFluid( ItemStack i ) {
		//TODO Check what this is used for to see if it works this way or not
		//return new FluidStack(i.itemID, i.getItemDamage());
		Block b = Block.getBlockFromItem( i.getItem() );
		if( !( b instanceof IFluidBlock ) )
			return null;
		return new FluidStack( ( ( IFluidBlock ) b ).getFluid(), i.getItemDamage() );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.meta == 0 || !config.rsControl[0] ) return "eng_toolbox:inner_black";
		return "eng_toolbox:inner_mixer";
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		System.out.println( "inner mixer texture loaded" );
		return new String[] {"eng_toolbox:inner_mixer"};
	}
}
