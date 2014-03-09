package emasher.api;

import com.mojang.authlib.GameProfile;
//import emasher.sockets.PacketHandler;
//import emasher.sockets.SocketsMod;
//import emasher.sockets.pipes.TileAdapterBase;
//import emasher.sockets.pipes.TileDirectionChanger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
//import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.List;

public class Util
{
	/*public static EntityPlayer createFakePlayer(World world, int x, int y, int z)
	{
		/*EntityPlayer player = new EntityPlayer(world, "[Engineer's Toolbox]") {
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

        if(world instanceof WorldServer) {
            EntityPlayer player = FakePlayerFactory.get((WorldServer)world, new GameProfile("[Engineer's Toolbox", "[Engineer's Toolbox]"));

            player.posX = x;
            player.posY = y;
            player.posZ = z;
            player.prevPosX = x;
            player.prevPosY = y;
            player.prevPosZ = z;

            return player;
        }

        return null;
	}

    public static boolean swapBlocks(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        Block b1 = world.getBlock(x, y, z);//world.getBlockId(x, y ,z);
        int meta1 = world.getBlockMetadata(x, y, z);
        Block b2 = world.getBlock(nx, ny, nz);
        int meta2 = world.getBlockMetadata(nx, ny, nz);

        if(b1 == SocketsMod.miniPortal) return false;
        if(b2 == SocketsMod.miniPortal) return false;

        if(b1 != null && b1.getBlockHardness(world, x, y, z) < 0) return false;
        if(b2 != null && b2.getBlockHardness(world, nx, ny, nz) < 0) return false;

        TileEntity te1 = world.getTileEntity(x, y, z);
        NBTTagCompound nbt1 = new NBTTagCompound();
        if(te1 != null) te1.writeToNBT(nbt1);
        world.removeTileEntity(x, y, z);
        world.setBlockToAir(x, y, z);

        TileEntity te2 = world.getTileEntity(nx, ny, nz);
        NBTTagCompound nbt2 = new NBTTagCompound();
        if(te2 != null) te2.writeToNBT(nbt2);
        world.removeTileEntity(nx, ny, nz);
        world.setBlockToAir(nx, ny, nz);

        world.setBlock(x, y, z, b2, meta2, 3);
        if(b2 instanceof BlockContainer)
        {
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null)
            {
                te.readFromNBT(nbt2);
                te.xCoord = x;
                te.yCoord = y;
                te.zCoord = z;

                if(te instanceof SocketTileAccess) for(int i = 0; i < 6; i++)
                {
                    ForgeDirection d = ForgeDirection.getOrientation(i);
                    SocketModule m = ((SocketTileAccess) te).getSide(d);
                    m.onSocketPlaced(((SocketTileAccess) te).getConfigForSide(d), (SocketTileAccess)te, d);
                }
            }
        }

        world.setBlock(nx, ny, nz, b1, meta1, 3);
        if(b1 instanceof BlockContainer)
        {
            TileEntity te = world.getTileEntity(nx, ny, nz);
            if(te != null)
            {
                te.readFromNBT(nbt1);
                te.xCoord = nx;
                te.yCoord = ny;
                te.zCoord = nz;

                if(te instanceof SocketTileAccess) for(int i = 0; i < 6; i++)
                {
                    ForgeDirection d = ForgeDirection.getOrientation(i);
                    SocketModule m = ((SocketTileAccess) te).getSide(d);
                    m.onSocketPlaced(((SocketTileAccess) te).getConfigForSide(d), (SocketTileAccess)te, d);
                }
            }
        }

        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null)
        {
            if(te instanceof SocketTileAccess)
            {
                SocketTileAccess ts = (SocketTileAccess)te;
                for(int i = 0; i < 6; i++)
                {
                    ForgeDirection side = ForgeDirection.getOrientation(i);
                    SocketModule m = ts.getSide(side);
                    m.onSocketPlaced(ts.getConfigForSide(side), ts, side);
                    ts.sendClientSideState(i);
                }
            }

            if(te instanceof TileAdapterBase)
            {
                TileAdapterBase ta = (TileAdapterBase)te;
                for(int i = 0; i < 6; i++)
                {
                    PacketHandler.instance.sendClientAdapterSide(ta, i);
                }
            }

            if(te instanceof TileDirectionChanger)
            {
                TileDirectionChanger td = (TileDirectionChanger)te;
                for(int i = 0; i < 6; i++)
                {
                    PacketHandler.instance.sendClientChangerSide(td, i);
                }
            }
        }


        te = world.getTileEntity(nx, ny, nz);

        if(te != null)
        {
            if(te instanceof SocketTileAccess)
            {
                SocketTileAccess ts = (SocketTileAccess)te;
                for(int i = 0; i < 6; i++)
                {
                    ForgeDirection side = ForgeDirection.getOrientation(i);
                    SocketModule m = ts.getSide(side);
                    m.onSocketPlaced(ts.getConfigForSide(side), ts, side);
                    ts.sendClientSideState(i);
                }
            }

            if(te instanceof TileAdapterBase)
            {
                TileAdapterBase ta = (TileAdapterBase)te;
                for(int i = 0; i < 6; i++)
                {
                    PacketHandler.instance.sendClientAdapterSide(ta, i);
                }
            }

            if(te instanceof TileDirectionChanger)
            {
                TileDirectionChanger td = (TileDirectionChanger)te;
                for(int i = 0; i < 6; i++)
                {
                    PacketHandler.instance.sendClientChangerSide(td, i);
                }
            }
        }

        return true;
    }

    public static boolean canMoveBlock(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        if(ny >= 255 || ny <= 0) return false;
        if(! isBlockReplaceable(world, nx, ny, nz)) return false;
        Block b = world.getBlock(x, y, z);
        if(b == SocketsMod.miniPortal) return false;
        return ! (b != null && b.getBlockHardness(world, x, y, z) < 0);
    }

    public static boolean moveBlock(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        return moveBlock(world, x, y, z, nx, ny, nz, true);
    }

    public static boolean moveBlock(World world, int x, int y, int z, int nx, int ny, int nz, boolean updateSocket)
    {
        if(ny >= 255 || ny <= 0) return false;
        if(! isBlockReplaceable(world, nx, ny, nz)) return false;
        Block b = world.getBlock(x, y, z);
        if(b == SocketsMod.miniPortal) return false;
        if(b != null && b.getBlockHardness(world, x, y, z) < 0) return false;
        int meta = world.getBlockMetadata(x, y, z);

        TileEntity te = world.getTileEntity(x, y, z);
        NBTTagCompound nbt = new NBTTagCompound();
        if(te != null)
        {
            te.writeToNBT(nbt);
            world.removeTileEntity(x, y, z);
        }

        world.setBlockToAir(x, y, z);

        world.setBlock(nx, ny, nz, b, meta, 3);

        List ents = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getAABBPool().getAABB(x, y, z, x + 1, y + 3, z + 1));
        for(Object e: ents) {
            if(e instanceof Entity)
            {
                Entity ent = (Entity)e;
                ent.setPosition(ent.posX + (nx - x), ent.posY + (ny - y), ent.posZ + (nz - z));
            }
        }

        te = world.getTileEntity(nx, ny, nz);
        if(te != null)
        {
            te.readFromNBT(nbt);
            te.xCoord = nx;
            te.yCoord = ny;
            te.zCoord = nz;

            if(updateSocket && te instanceof SocketTileAccess) for(int i = 0; i < 6; i++)
            {
                ForgeDirection d = ForgeDirection.getOrientation(i);
                SocketModule m = ((SocketTileAccess) te).getSide(d);
                m.onSocketPlaced(((SocketTileAccess) te).getConfigForSide(d), (SocketTileAccess)te, d);
            }
        }

        return true;
    }

    public static boolean isBlockReplaceable(World world, int x, int y, int z)
    {
        Block b = world.getBlock(x, y, z);
        return world.isAirBlock(x, y, z) ||
                b == Blocks.vine ||
                b == Blocks.tallgrass ||
                b == Blocks.deadbush ||
                b == Blocks.fire ||
                b == Blocks.water ||
                b == Blocks.flowing_water ||
                b == Blocks.lava ||
                b == Blocks.flowing_lava ||
                b.isReplaceable(world, x, y, z) ||
                b instanceof BlockFluidBase;
    }*/
	

}
