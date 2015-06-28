package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSPulseModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModFluidDetector extends RSPulseModule {

	public ModFluidDetector( int id ) {
		super( id, "eng_toolbox:DETLiquid_0" );
	}

	@Override
	public String getLocalizedName() {
		return "Fluid Detector";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Creates an internal redstone pulse" );
		l.add( "when fluid is added to its configured tank" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_BLUE() + "Tank to watch" );
		l.add( emasher.util.Config.PREF_RED() + "RS control channel to pulse" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), " h ", "iui", " b ", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'h' ), Blocks.stone_pressure_plate,
				Character.valueOf( 'u' ), Items.bucket, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), " h ", "iui", " b ", Character.valueOf( 'i' ), "ingotAluminum", Character.valueOf( 'h' ), Blocks.stone_pressure_plate,
				Character.valueOf( 'u' ), Items.bucket, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), " h ", "iui", " b ", Character.valueOf( 'i' ), "ingotTin", Character.valueOf( 'h' ), Blocks.stone_pressure_plate,
				Character.valueOf( 'u' ), Items.bucket, Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}
	
	@Override
	public boolean hasTankIndicator() {
		return true;
	}
	
	@Override
	public void onTankChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add ) {
		if( add && ( index == config.tank || config.tank == -1 ) ) {
			config.meta = 1;
			
			for( int i = 0; i < 3; i++ ) {
				if( config.rsControl[i] ) {
					ts.modifyRS( i, true );
				}
			}
			
			ts.updateAdj( side );
			ts.sendClientSideState( side.ordinal() );
			//PacketHandler.instance.SendClientSideState(ts, (byte)side.ordinal());
		}
	}

}
