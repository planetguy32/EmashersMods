package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class ModBreaker extends SocketModule {

	public ModBreaker( int id ) {
		super( id, "sockets:breaker" );
	}

	@Override
	public String getLocalizedName() {
		return "Breaker";
	}
	
	@Override
	public void getToolTip( List l ) {
		l.add( "Breaks blocks softer than obsidian" );
		l.add( "and collects their drops" );
	}
	
	@Override
	public void getIndicatorKey( List l ) {
		l.add( SocketsMod.PREF_GREEN + "Output Inventory" );
		l.add( SocketsMod.PREF_RED + "RS Activation Pulse" );
		l.add( SocketsMod.PREF_DARK_PURPLE + "RS Latch Activation Pulse" );
		l.add( SocketsMod.PREF_YELLOW + "Extra output sent to machine output" );
	}
	
	
	@Override
	public void addRecipe() {
		GameRegistry.addShapedRecipe( new ItemStack( SocketsMod.module, 1, moduleID ), "ipi", "ici", " b ", Character.valueOf( 'i' ), Items.iron_ingot, Character.valueOf( 'p' ), Items.iron_pickaxe, Character.valueOf( 'c' ),
				Blocks.dispenser, Character.valueOf( 'b' ), SocketsMod.blankSide );
	}
	
	@Override
	public boolean hasInventoryIndicator() {
		return true;
	}
	
	@Override
	public boolean hasRSIndicator() {
		return true;
	}
	
	@Override
	public boolean hasLatchIndicator() {
		return true;
	}
	
	@Override
	public void onRSInterfaceChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( on ) tryBreakBlock( true, index, ts, config, side );
	}
	
	@Override
	public void onRSLatchChange( SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on ) {
		if( on ) tryBreakBlock( false, index, ts, config, side );
	}
	
	public void tryBreakBlock( boolean control, int index, SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		boolean canBreak = false;
		if( control ) {
			if( config.rsControl[index] ) canBreak = true;
		} else {
			if( config.rsLatch[index] ) canBreak = true;
		}
		
		if( config.inventory < 0 || config.inventory > 2 ) canBreak = false;
		
		int xo = ts.xCoord + side.offsetX;
		int yo = ts.yCoord + side.offsetY;
		int zo = ts.zCoord + side.offsetZ;

		Block b = ts.getWorldObj().getBlock( xo, yo, zo );
		;
		int blockID = Block.getIdFromBlock( b );

		//if(blockID != 0) b = Block.blocksList[blockID];
		
		if( canBreak && b != null && b.getBlockHardness( ts.getWorldObj(), xo, yo, zo ) < 50F && b.getBlockHardness( ts.getWorldObj(), xo, yo, zo ) >= 0 ) //50F is obsidian's hardness
		{
			ArrayList<ItemStack> items = b.getDrops( ts.getWorldObj(), xo, yo, zo, ts.getWorldObj().getBlockMetadata( xo, yo, zo ), 0 );
			for( ItemStack item : items ) {
				if( ts.addItemInternal( item, false, config.inventory ) == item.stackSize ) {
					ts.addItemInternal( item, true, config.inventory );
				} else {
					if( ts.forceOutputItem( item, false ) == item.stackSize ) {
						ts.forceOutputItem( item, true );
					} else {
						this.dropItemsOnSide( ts, side, item );
					}
				}
				
				ts.getWorldObj().removeTileEntity( xo, yo, zo );
				ts.getWorldObj().setBlockToAir( xo, yo, zo );
			}
		}
		
		
	}
	
	public void dropItemsOnSide( SocketTileAccess ts, ForgeDirection side, ItemStack stack ) {
		if( !ts.getWorldObj().isRemote ) {
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			float f = 0.7F;
			double d0 = ( double ) ( ts.getWorldObj().rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
			double d1 = ( double ) ( ts.getWorldObj().rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
			double d2 = ( double ) ( ts.getWorldObj().rand.nextFloat() * f ) + ( double ) ( 1.0F - f ) * 0.5D;
			EntityItem entityitem = new EntityItem( ts.getWorldObj(), ( double ) xo + d0, ( double ) yo + d1, ( double ) zo + d2, stack.copy() );
			entityitem.delayBeforeCanPickup = 1;
			ts.getWorldObj().spawnEntityInWorld( entityitem );
		}
	}
	
}
