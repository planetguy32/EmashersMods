package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModEnergyOutput extends SocketModule {

	public ModEnergyOutput( int id ) {
		super( id, "sockets:energyOutput" );
	}

	@Override
	public String getLocalizedName() {
		return "Energy Output";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Outputs Redstone Flux Energy" );
		l.add( "to adjacent cables/machines/etc." );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), " g ", " b ", Character.valueOf( 'g' ), Items.gold_nugget, Character.valueOf( 'p' ), EmasherCore.psu,
				Character.valueOf( 'b' ), SocketsMod.blankSide );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), " g ", " b ", Character.valueOf( 'g' ), "ingotCopper", Character.valueOf( 'p' ), EmasherCore.psu,
				Character.valueOf( 'b' ), SocketsMod.blankSide ) );
	}
	
	
	/*@Override
	public boolean hasTankIndicator() {return true; }*/
	
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
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		
		boolean allOff = true;
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					ts.outputEnergy( 1000, side );
					return;
				}
				allOff = false;
			}

			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					ts.outputEnergy( 1000, side );
					return;
				}
				allOff = false;
			}
		}

		if( allOff ) {
			ts.outputEnergy( 1000, side );

		}

	}
	
	@Override
	public int extractEnergy( int amount, boolean simulate, SideConfig config, SocketTileAccess ts ) {
		boolean allOff = true;
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( ts.getRSControl( i ) ) {
					return ts.useEnergy( amount, simulate );
				}
				allOff = false;
			}

			if( config.rsLatch[i] ) {
				if( ts.getRSLatch( i ) ) {
					return ts.useEnergy( amount, simulate );
				}
				allOff = false;
			}
		}

		if( allOff ) {
			return ts.useEnergy( amount, simulate );

		}
		
		return 0;
	}

}
