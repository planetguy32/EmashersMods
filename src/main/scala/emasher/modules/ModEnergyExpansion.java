package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModEnergyExpansion extends SocketModule {
	public ModEnergyExpansion( int id ) {
		super( id, "eng_toolbox:energy_expansion", "eng_toolbox:energy_expansion_in", "eng_toolbox:energy_expansion_out" );
	}

	@Override
	public String getLocalizedName() {
		return "Energy Storage Upgrade";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Adds 1 000 000 RF" );
		l.add( "of extra energy storage" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_WHITE() + "Configure if input or output or neither" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "pdp", "ggg", "pbp", Character.valueOf( 'g' ), Items.gold_ingot, Character.valueOf( 'p' ), emasher.items.Items.psu(),
				Character.valueOf( 'd' ), Items.diamond, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		return "eng_toolbox:inner_grey_tile";
	}

	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"eng_toolbox:inner_grey_tile"};
	}
	
	@Override
	public int getCurrentTexture( SideConfig config ) {
		return config.meta;
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
		return config.meta != 0;
	}
	
	@Override
	public void onGenericRemoteSignal( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		config.meta++;
		if( config.meta == 3 ) config.meta = 0;
		ts.sendClientSideState( side.ordinal() );
		ts.updateAdj( side );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		
		boolean allOff = true;
		if( config.meta == 2 ) {
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

	}
	
	@Override
	public int receiveEnergy( int amount, boolean simulate, SideConfig config, SocketTileAccess ts ) {
		boolean allOff = true;
		if( config.meta == 1 ) {
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
		}
		
		return 0;
	}
	
	@Override
	public int extractEnergy( int amount, boolean simulate, SideConfig config, SocketTileAccess ts ) {
		boolean allOff = true;
		if( config.meta == 2 ) {
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
		}
		
		return 0;
	}
	
	@Override
	public void init( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		ts.setMaxEnergyStored( ts.getMaxEnergyStored() + 1000000 );
	}
	
	@Override
	public void onRemoved( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		ts.setMaxEnergyStored( ts.getMaxEnergyStored() - 1000000 );
	}
}
