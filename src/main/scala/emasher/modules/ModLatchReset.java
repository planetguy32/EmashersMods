package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModLatchReset extends SocketModule {

	public ModLatchReset( int id ) {
		super( id, "eng_toolbox:RESET_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Redstone Latch Reset";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Tuns off selected internal RS latches" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS latches to turn off" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "tgr", " b ", Character.valueOf( 't' ), Blocks.redstone_torch, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'g' ), Items.iron_ingot, Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 16 ) );
	}
	
	@Override
	public boolean hasLatchIndicator() {
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
			if( config.rsLatch[i] ) {
				if( on ) {
					ts.modifyLatch( i, false );
					config.meta = 1;
				} else {
					config.meta = 0;
				}
			}
		}
	}
	
}
