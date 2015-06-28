package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModLavaIntake extends SocketModule {

	public ModLavaIntake( int id ) {
		super( id, "eng_toolbox:lavaIntake" );
	}

	@Override
	public String getLocalizedName() {
		return "Lava Intake";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Adds cobblestone to configured" );
		l.add( "inventory when adjacent to lava" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_GREEN() + "Output inventory" );
	}
	
	public boolean hasInventoryIndicator() {
		return true;
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ctc", " b ", Character.valueOf( 't' ), Blocks.iron_bars, Character.valueOf( 'c' ), new ItemStack( emasher.items.Items.module(), 1, 80 ),
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void updateSide( SideConfig config, SocketTileAccess ts, ForgeDirection side ) {
		config.meta++;
		
		if( config.meta >= 40 ) {
			config.meta = 0;
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			
			//int blockID = ts.getWorldObj().getBlockId(xo, yo, zo);
			Block b = ts.getWorldObj().getBlock( xo, yo, zo );
			Material mat = Material.water;
			if( b != null ) mat = b.getMaterial();
			
			if( mat == Material.lava ) {
				if( config.inventory >= 0 && config.inventory < 3 && ts.addItemInternal( new ItemStack( Blocks.cobblestone ), false, config.inventory ) == 1 ) {
					ts.addItemInternal( new ItemStack( Blocks.cobblestone ), true, config.inventory );
				}
			}
		}
	}

}
