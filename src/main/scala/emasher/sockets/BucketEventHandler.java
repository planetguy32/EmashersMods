package emasher.sockets;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import emasher.core.EmasherCore;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketEventHandler {
	@SubscribeEvent
	public void onBucketFill( FillBucketEvent event ) {
		ItemStack result;
		MovingObjectPosition t = event.target;
		if( event.world.getBlock( t.blockX, t.blockY, t.blockZ ) == SocketsMod.fluidSlickwater.getBlock() ) {
			event.world.setBlock( t.blockX, t.blockY, t.blockZ, Blocks.air );
			result = new ItemStack( SocketsMod.slickBucket );
		} else if( event.world.getBlock( t.blockX, t.blockY, t.blockZ ) == EmasherCore.nutrientWaterFluid.getBlock() ) {
			event.world.setBlock( t.blockX, t.blockY, t.blockZ, Blocks.air );
			result = new ItemStack( SocketsMod.nutBucket );
		} else return;

		event.result = result;
		event.setResult( Event.Result.ALLOW );
	}
}
