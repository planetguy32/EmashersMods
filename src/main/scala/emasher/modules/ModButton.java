package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.RSPulseModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModButton extends RSPulseModule {

	public ModButton( int id ) {
		super( id, "eng_toolbox:button0", "eng_toolbox:button1", "eng_toolbox:button2", "eng_toolbox:button3", "eng_toolbox:button4", "eng_toolbox:button5", "eng_toolbox:button6", "eng_toolbox:button7",
				"eng_toolbox:button8", "eng_toolbox:button9", "eng_toolbox:button10", "eng_toolbox:button11", "eng_toolbox:button12", "eng_toolbox:button13", "eng_toolbox:button14", "eng_toolbox:button15" );
	}

	@Override
	public String getLocalizedName() {
		return "Button";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Creates an internal redstone pulse when pressed" );
		l.add( "Can be dyed different colours" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS circuits to pulse" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "RS latches to toggle" );
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "t", "b", Character.valueOf( 't' ), Blocks.stone_button, Character.valueOf( 'r' ), Items.redstone,
				Character.valueOf( 'b' ), new ItemStack( emasher.items.Items.module(), 1, 16 ) );
	}
	
	@Override
	public int getCurrentTexture( SideConfig config ) {
		return config.meta >> 3;
	}
	
	@Override
	public boolean isOutputtingRedstone( SideConfig config, SocketTileAccess ts ) {
		return false;
	}
	
	@Override
	public boolean hasLatchIndicator() {
		return true;
	}
	
	@Override
	public void changeColour( int colour, SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		config.meta = colour;
		config.meta <<= 3;
		
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void onSideActivated( SocketTileAccess ts, SideConfig config, ForgeDirection side, EntityPlayer player ) {
		config.meta &= 0xfffffff8;
		config.meta++;
		
		for( int i = 0; i < 3; i++ ) {
			if( config.rsControl[i] ) {
				ts.modifyRS( i, true );
				
			}
			
			if( config.rsLatch[i] ) {
				boolean latch = ts.getRSLatch( i );
				ts.modifyLatch( i, !latch );
			}
		}
		
		ts.sendClientSideState( side.ordinal() );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		if( ( config.meta & 7 ) > 0 ) {
			config.meta++;
			
			if( ( config.meta & 7 ) >= 4 ) {
				config.meta &= 0xfffffff8;
				
				for( int i = 0; i < 3; i++ ) {
					if( config.rsControl[i] ) {
						ts.modifyRS( i, false );
					}
				}
				
				ts.sendClientSideState( side.ordinal() );
			}
		}
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( !on ) {
			if( config.rsControl[index] && ( config.meta & 7 ) > 0 ) {
				ts.modifyRS( index, true );
			}
		}
	}

	@Override
	public boolean canModuleBeDyed() {
		return true;
	}

}
