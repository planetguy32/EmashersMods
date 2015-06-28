package emasher.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ModSpring extends SocketModule {

	public ModSpring( int id ) {
		super( id, "eng_toolbox:spring" );
	}

	@Override
	public String getLocalizedName() {
		return "Spring";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Catapults a non-player entity adjacent to this" );
		l.add( "module and within one block away from" );
		l.add( "the socket on an internal redstone pulse" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "RS control channel" );
	}
	
	@Override
	public boolean hasRSIndicator() {
		return true;
	}
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "ipi", " b ", Character.valueOf( 'i' ), Items.iron_ingot,
				Character.valueOf( 'p' ), Blocks.piston, Character.valueOf( 'b' ), emasher.items.Items.blankSide() );
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( on && config.rsControl[index] ) {
			double x = ts.xCoord + side.offsetX;
			double y = ts.yCoord + side.offsetY;
			double z = ts.zCoord + side.offsetZ;
			
			List l = ts.getWorldObj().getEntitiesWithinAABBExcludingEntity( ( Entity ) null, AxisAlignedBB.getBoundingBox( x, y, z, x + 1, y + 1, z + 1 ) );
			for( Object o : l ) {
				if( o instanceof Entity ) {
					Entity e = ( Entity ) o;
					double xm = side.offsetX;
					double ym = side.offsetY;
					double zm = side.offsetZ;
					
					e.motionX += xm;
					e.motionY += ym;
					e.motionZ += zm;
				}
			}
		}
	}

}
