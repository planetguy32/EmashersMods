package emasher.util;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;

public class BucketEventHandler {
	@SubscribeEvent
	public void onBucketFill( FillBucketEvent event ) {
		ItemStack result;
		MovingObjectPosition t = event.target;
		if( event.world.getBlock( t.blockX, t.blockY, t.blockZ ) == emasher.fluids.Fluids.fluidSlickwater().getBlock() ) {
			event.world.setBlock( t.blockX, t.blockY, t.blockZ, Blocks.air );
			result = new ItemStack( emasher.items.Items.slickBucket() );
		} else if( event.world.getBlock( t.blockX, t.blockY, t.blockZ ) == emasher.fluids.Fluids.nutrientWaterFluid().getBlock() ) {
			event.world.setBlock( t.blockX, t.blockY, t.blockZ, Blocks.air );
			result = new ItemStack( emasher.items.Items.nutBucket() );
		} else return;

		event.result = result;
		event.setResult( Event.Result.ALLOW );
	}
}
