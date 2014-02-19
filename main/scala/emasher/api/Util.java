package emasher.api;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
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

    public static void swapBlocks(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        int id1 = world.getBlockId(x, y ,z);
        int meta1 = world.getBlockMetadata(x, y, z);
        int id2 = world.getBlockId(nx, ny, nz);
        int meta2 = world.getBlockMetadata(nx, ny, nz);

        Block b1 = Block.blocksList[id1];
        Block b2 = Block.blocksList[id2];

        if(b1 != null && b1.blockHardness < 0) return;
        if(b2 != null && b2.blockHardness < 0) return;

        TileEntity te1 = world.getBlockTileEntity(x, y, z);
        if(te1 != null) world.removeBlockTileEntity(x, y, z);
        TileEntity te2 = world.getBlockTileEntity(nx, ny, nz);
        if(te2 != null) world.removeBlockTileEntity(x, y, z);

        world.setBlock(x, y, z, id2, meta2, 3);
        if(te2 != null) world.setBlockTileEntity(x, y, z, te2);

        world.setBlock(nx, ny, nz, id1, meta1, 3);
        if(te1 != null) world.setBlockTileEntity(nx, ny, nz, te1);

        if(te1 != null && te1 instanceof SocketTileAccess)
        {
            for(int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                SocketModule m = ((SocketTileAccess) te1).getSide(d);
                m.onSocketPlaced(((SocketTileAccess) te1).getConfigForSide(d), (SocketTileAccess)te1, d);
            }
        }

        if(te2 != null && te2 instanceof SocketTileAccess)
        {
            for(int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                SocketModule m = ((SocketTileAccess) te2).getSide(d);
                m.onSocketPlaced(((SocketTileAccess) te2).getConfigForSide(d), (SocketTileAccess)te2, d);
            }
        }
    }

    public static void moveBlock(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        if(! world.isAirBlock(nx, ny, nz)) return;
        int id = world.getBlockId(x, y, z);
        Block b = Block.blocksList[id];
        if(b != null && b.blockHardness < 0) return;
        int meta = world.getBlockMetadata(x, y, z);
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if(te != null) world.removeBlockTileEntity(x, y, z);
        world.setBlock(nx, ny, nz, id, meta, 3);
        if(te != null) world.setBlockTileEntity(nx, ny, nz, te);

        if(te != null && te instanceof SocketTileAccess)
        {
            for(int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                SocketModule m = ((SocketTileAccess) te).getSide(d);
                m.onSocketPlaced(((SocketTileAccess) te).getConfigForSide(d), (SocketTileAccess)te, d);
            }
        }
    }
	

}
