package emasher.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModStirlingGenerator extends SocketModule {

	private static final int ACTIVE = 1;
	private static final int HIGH_POWER = 2;

	public ModStirlingGenerator( int id ) {
		super( id, "eng_toolbox:stirlingGenIdle0",
				"eng_toolbox:stirlingGenIdle1" );
	}

	@Override
	public String getLocalizedName() {
		return "Stirling Generator";
	}

	@Override
	public void getToolTip( List l ) {
		l.add( "Generates power using" );
		l.add( "furnace fuel" );
		l.add( "High power mode uses" );
		l.add( "more than double the fuel" );
		l.add( "to produce double the power" );
		l.add( "per tick" );
	}

	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_GREEN() + "Input inventory" );
		l.add( emasher.util.Config.PREF_WHITE() + "Toggles high power mode" );
		l.add( emasher.util.Config.PREF_AQUA() + "Produces 10 or 20 RF/t" );
	}

	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ),
				"ggg",
				" F ",
				" b ",
				Character.valueOf( 'g' ), Blocks.stone,
				Character.valueOf( 'F' ), Blocks.furnace,
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}

	@Override
	public boolean hasInventoryIndicator() {
		return true;
	}

	@Override
	public void onGenericRemoteSignal( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		config.tank ^= HIGH_POWER;
		ts.sendClientSideState( side.ordinal() );
	}

	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		boolean updateClient = false;
		if( config.inventory >= 0 && config.inventory <= 2 ) {
			boolean roomForMoreFuel = ts.sideInventory.getStackInSlot( side.ordinal() ) == null;
			boolean moreFuelAvail = ts.getStackInInventorySlot( config.inventory ) != null;
			boolean canStoreEnergy = ts.getEnergyStored() < ts.getMaxEnergyStored();

			if( moreFuelAvail && roomForMoreFuel && canStoreEnergy && config.meta <= 0 ) {
				ItemStack toIntake = ts.getStackInInventorySlot( config.inventory );

				int time = TileEntityFurnace.getItemBurnTime( toIntake );
				if( time > 0 ) {
					// ensure it's even
					if( time % 2 != 0 ) time++;
					ts.extractItemInternal( true, config.inventory, 1 );
					config.meta = time;
				}
			}

			if( canStoreEnergy && config.meta > 0 ) {
				if( ( config.tank & HIGH_POWER ) != HIGH_POWER ) {
					config.meta--;
					ts.addEnergy( 10, false );
				} else {
					config.meta -= 2;
					if( ts.getWorldObj().rand.nextBoolean() ) config.meta--;
					ts.addEnergy( 20, false );
				}
				if( config.meta == 0 ) {
					config.tank &= ~ACTIVE;
					updateClient = true;
				} else {
					if( ( config.tank & ACTIVE ) != ACTIVE )
						updateClient = true;
					config.tank |= ACTIVE;
				}
			} else if( !canStoreEnergy && config.meta > 0 ) {
				config.tank &= ~ACTIVE;
				updateClient = true;
			}
		}
		if( updateClient ) ts.sendClientSideState( side.ordinal() );
	}

	@Override
	public void init( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		// not ACTIVE and not HIGH_POWER
		config.tank = 0;
		ts.sendClientSideState( side.ordinal() );
	}

	@Override
	public int getCurrentTexture( SideConfig config ) {
		if( ( config.tank & HIGH_POWER ) == HIGH_POWER )
			return 1;
		return 0;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		if( ( config.tank & ACTIVE ) == ACTIVE ) {
			if( ( config.tank & HIGH_POWER ) == HIGH_POWER )
				return "eng_toolbox:inner_fire_blue";
			return "eng_toolbox:inner_fire_orange";
		} else {
			return "eng_toolbox:inner_black";
		}
	}

	@Override
	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"eng_toolbox:inner_fire_blue", "eng_toolbox:inner_fire_orange"};
	}
}
