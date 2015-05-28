package emasher.sockets.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

//import emasher.sockets.PacketHandler;

public class ModEnergyIndicator extends SocketModule {
	private static int dFull = 12;
	
	public ModEnergyIndicator( int id ) {
		super( id, "sockets:enIndicator0" );
	}

	@Override
	public String getLocalizedName() {
		return "Energy Indicator";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Displays the amount of" );
		l.add( "energy stored in the socket" );
	}
	
	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "ggg", "slr", " b ", Character.valueOf( 'g' ), Blocks.glass_pane, Character.valueOf( 's' ), Items.glowstone_dust,
				Character.valueOf( 'l' ), "dyeCyan", Character.valueOf( 'r' ), Items.redstone, Character.valueOf( 'b' ), SocketsMod.blankSide ) );
	}
	
	@Override
	public void onSideActivated( SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player ) {
//		System.out.println("Stored: " + ts.getEnergyStored());
//		System.out.println("Capacity: " + ts.getMaxEnergyStored());
//      System.out.println("==============================");
	}

	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		double f = ( double ) ts.getEnergyStored() / ( double ) ts.getMaxEnergyStored();
		int d = ( int ) ( f * 100 );
		if( d != config.meta ) {
			config.meta = d;
			ts.sendClientSideState( side.ordinal() );
		}
	}

	@Override
	public boolean renderEnergyAmount() {
		return true;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"sockets:inner_blue_tile"};
	}
	
}
