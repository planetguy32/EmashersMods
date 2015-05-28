package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModOsPump extends SocketModule {

	public ModOsPump( int id ) {
		super( id, "sockets:osPump" );
	}

	@Override
	public String getLocalizedName() {
		return "Water Intake";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Collects water from adjacent" );
		l.add( "water source without destroying it" );
		l.add( "and inputs water into selected tank" );
		l.add( "Only works if there are" );
		l.add( "two of them adjacent to water" );
		l.add( "on the same socket configured" );
		l.add( "to use the same tank" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( SocketsMod.PREF_BLUE + "Output tank" );
	}
	
	public boolean hasTankIndicator() {
		return true;
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "iti", " b ", Character.valueOf( 't' ), Blocks.iron_bars, Character.valueOf( 'i' ), Items.iron_ingot,
				Character.valueOf( 'b' ), SocketsMod.blankSide );
		
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "iti", " b ", Character.valueOf( 't' ), Blocks.iron_bars, Character.valueOf( 'i' ), "ingotAluminum",
				Character.valueOf( 'b' ), SocketsMod.blankSide ) );
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "iti", " b ", Character.valueOf( 't' ), Blocks.iron_bars, Character.valueOf( 'i' ), "ingotTin",
				Character.valueOf( 'b' ), SocketsMod.blankSide ) );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		int xo = ts.xCoord + side.offsetX;
		int yo = ts.yCoord + side.offsetY;
		int zo = ts.zCoord + side.offsetZ;
		
		Block block = ts.getWorldObj().getBlock( xo, yo, zo );
		boolean first = false;
		int numBlocks = 0;
		
		if( block == Blocks.water ) {
			config.meta = 1;
			for( int i = 0; i < 6; i++ ) {
				if( i == side.ordinal() ) first = true;
				if( ts.getSide( ForgeDirection.getOrientation( i ) ).moduleID == this.moduleID ) {
					if( ts.getConfigForSide( ForgeDirection.getOrientation( i ) ).meta == 1 && ts.getConfigForSide( ForgeDirection.getOrientation( i ) ).tank == config.tank ) {
						if( !first ) return;
						if( config.tank >= 0 && config.tank <= 2 ) {
							numBlocks++;
						}
					}
				}
			}
		} else {
			config.meta = 0;
		}
		
		if( config.tank >= 0 && config.tank <= 2 && numBlocks >= 2 ) {
			ts.fillInternal( config.tank, new FluidStack( FluidRegistry.WATER, 100 ), true );
		}
	}

}
