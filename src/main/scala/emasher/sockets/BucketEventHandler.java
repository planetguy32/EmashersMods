package emasher.sockets;

import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.FillBucketEvent;

public class BucketEventHandler
{
	@ForgeSubscribe
	public void onBucketFill(FillBucketEvent event)
	{
		ItemStack result;
		MovingObjectPosition t = event.target;
		if(event.world.getBlockId(t.blockX, t.blockY, t.blockZ) == SocketsMod.fluidSlickwater.getBlockID())
		{
			event.world.setBlock(t.blockX, t.blockY, t.blockZ, 0);
			result = new ItemStack(SocketsMod.slickBucket);
		}
		else return;

		event.result = result;
		event.setResult(Result.ALLOW);
	}
}
