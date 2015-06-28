package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModEnergyInput extends SocketModule {

	public ModEnergyInput( int id ) {
		super( id, "eng_toolbox:energyInput" );
	}

	@Override
	public String getLocalizedName() {
		return "Energy Input";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Accepts Redstone Flux Energy" );
		l.add( "from adjacent cables/generators/etc." );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "g g", " b ", Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'p' ), emasher.items.Items.psu(),
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "g g", " b ", Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'p' ), emasher.items.Items.psu(),
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}
	
	
	@Override
	public boolean hasRSIndicator() {
		return true;
	}
	
	@Override
	public boolean hasLatchIndicator() {
		return true;
	}
	
	@Override
	public boolean isEnergyInterface( SideConfig config ) {
		return true;
	}
	
	@Override
	public int receiveEnergy( int amount, boolean simulate, SideConfig config, SocketTileAccess ts ) {
		boolean allOff = true;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					return ts.addEnergy( amount, simulate );
				}
				allOff = false;
			}

			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					return ts.addEnergy( amount, simulate );
				}
				allOff = false;
			}
		}

		if( allOff ) {
			return ts.addEnergy( amount, simulate );

		}
		
		return 0;
	}
	
	
}
