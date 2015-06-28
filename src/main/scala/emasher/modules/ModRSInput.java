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

public class ModRSInput extends SocketModule {

	public ModRSInput( int id ) {
		super( id, "eng_toolbox:RSIN_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone Input";
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "r", "b", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Keeps an RS control channel(s) turned on" );
		l.add( "while recieving an external redstone signal" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS control channels to activate" );
	}
	
	@Override
	public boolean hasRSIndicator() {
		return true;
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( config.meta == 0 ) return "eng_toolbox:inner_redstone_inactive";
		return "eng_toolbox:inner_redstone_active";
	}

	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {
				"eng_toolbox:inner_redstone_inactive",
				"eng_toolbox:inner_redstone_active"
		};
	}
	
	@Override
	public boolean isRedstoneInterface() {
		return true;
	}
	
	@Override
	public void updateRestone( boolean on, SideConfig config, SocketTileAccess ts ) {
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				if( on ) {
					ts.modifyRS( i, true );
					config.meta = 1;
				} else {
					ts.modifyRS( i, false );
					config.meta = 0;
				}
			}
		}
	}
	
	
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( !on ) {
			if( config.rsControl[index] && ts.getSideRS( side ) ) {
				ts.modifyRS( index, true );
			}
		}
	}
	
}
