package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModSolar extends SocketModule {

	public ModSolar( int id ) {
		super( id, "eng_toolbox:solarPanel" );
	}

	@Override
	public String getLocalizedName() {
		return "Solar Panel";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Generates power when exposed to sunlight" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_AQUA() + "Generates 5 RF/t" );
		l.add( "Can only be placed on the top of a socket" );
		l.add( "Requires sunlight to operate" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "sss", "sps", "sbs", Character.valueOf( 's' ), Blocks.daylight_detector, Character.valueOf( 'p' ), emasher.items.Items.psu(),
				Character.valueOf( 'u' ), Blocks.diamond_block, Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 7 ) );
	}
	
	@Override
	public boolean canBeInstalled( SocketTileAccess ts, ForgeDirection side ) {
		if( side != ForgeDirection.UP ) return false;
		return true;
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( ts.getWorldObj().getBlockLightValue( ts.xCoord, ts.yCoord + 1, ts.zCoord ) > 14 && ts.getMaxEnergyStored() - ts.getEnergyStored() >= 5 ) {

			if( side == ForgeDirection.UP ) ts.addEnergy( 5, false );
		}
	}

}
