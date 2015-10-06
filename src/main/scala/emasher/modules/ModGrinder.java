package emasher.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.GrinderRecipeRegistry;
import emasher.api.GrinderRecipeRegistry.GrinderRecipe;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModGrinder extends SocketModule {

	public ModGrinder( int id ) {
		super( id, "eng_toolbox:grinderIdle" );
	}

	@Override
	public String getLocalizedName() {
		return "Grinder";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Grinds ores an other items" );
		l.add( "Compatible with IC^2 macerator" );
		l.add( "recipes when possible" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_GREEN() + "Input inventory" );
		l.add( emasher.util.Config.PREF_YELLOW() + "Outputs to Machine Output" );
		l.add( emasher.util.Config.PREF_AQUA() + "Requires 10 RF/t" );
		l.add( "Cannot be installed on a socket with other machines" );
	}
	
	@Override
	public void addRecipe() {
		//GameRegistry.addShapedRecipe(new ItemStack(emasher.items.Items.module(), 1, moduleID), " h ", "iui", " b ", Character.valueOf('i'), Block.blockIron, Character.valueOf('h'), emasher.items.Items.psu(),
		//Character.valueOf('u'), Block.blockDiamond, Character.valueOf('b'), emasher.items.Items.blankSide());
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ihi", "iui", "ibi", Character.valueOf( 'i' ), "gemEmery", Character.valueOf( 'h' ), emasher.items.Items.psu(),
				Character.valueOf( 'u' ), Items.diamond, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
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
		if( config.inventory >= 0 && config.inventory <= 2 ) {
			if( ts.sideInventory.getStackInSlot( side.ordinal() ) == null ) {
				if( ts.getStackInInventorySlot( config.inventory ) != null ) {
					ItemStack toIntake = ts.getStackInInventorySlot( config.inventory );
					
					ItemStack product = null;
					
					GrinderRecipe r = GrinderRecipeRegistry.getRecipe( toIntake );
					if( r != null ) {
						List<ItemStack> potentialOutputs=r.getOutput();
						if(potentialOutputs.size() != 0) {
							product = potentialOutputs.get( 0 );
						}
					}

//					if(product == null && Loader.isModLoaded("IC2"))
//					{
//						RecipeOutput o = Recipes.macerator.getOutputFor(toIntake, false);
//						if(o != null) product = o.items.get(0);
//					}
					
					if( product != null ) {
						ts.extractItemInternal( true, config.inventory, 1 );
						ts.sideInventory.setInventorySlotContents( side.ordinal(), product.copy() );
						config.meta = 180;
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
				int num = ts.forceOutputItem( ts.sideInventory.getStackInSlot( side.ordinal() ) );
				ts.sideInventory.getStackInSlot( side.ordinal() ).stackSize -= num;
				if( ts.sideInventory.getStackInSlot( side.ordinal() ).stackSize <= 0 )
					ts.sideInventory.setInventorySlotContents( side.ordinal(), null );
				
			}
			if( updateClient ) ts.sendClientSideState( side.ordinal() );
		}
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.meta == 0 || !config.rsControl[0] ) return "eng_toolbox:inner_black";
		return "eng_toolbox:inner_turbulance";
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"eng_toolbox:inner_turbulance"};
	}

}

