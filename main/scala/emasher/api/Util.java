package emasher.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;

public class Util
{
	public static EntityPlayer createFakePlayer(World world, int x, int y, int z)
	{
		EntityPlayer player = new EntityPlayer(world, "[Engineer's Toolbox]") {
			@Override
			public void sendChatToPlayer(ChatMessageComponent var1) {
			}

			@Override
			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return false;
			}

			@Override
			public ChunkCoordinates getPlayerCoordinates() {
				return null;
			}
		};
		
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		player.prevPosX = x;
		player.prevPosY = y;
		player.prevPosZ = z;
		
		return player;
	}
	

}
