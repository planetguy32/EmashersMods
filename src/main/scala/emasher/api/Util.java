package emasher.api;

import emasher.sockets.SocketsMod;
import emasher.sockets.packethandling.AdapterSideMessage;
import emasher.sockets.packethandling.ChangerSideMessage;
import emasher.sockets.pipes.TileAdapterBase;
import emasher.sockets.pipes.TileDirectionChanger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidBase;

import java.util.List;

public class Util
{
	public static EntityPlayer createFakePlayer(World world, int x, int y, int z)
	{
        EntityPlayer player = createFakePlayer(world, x, y, z);
		/*EntityPlayer player = new EntityPlayer(world, "[Engineer's Toolbox]") {
            @Override
            public void addChatMessage(IChatComponent p_145747_1_) {

            }

            @Override
			public boolean canCommandSenderUseCommand(int var1, String var2) {
				return false;
			}

			@Override
			public ChunkCoordinates getPlayerCoordinates() {
				return null;
			}
		};*/
		
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		player.prevPosX = x;
		player.prevPosY = y;
		player.prevPosZ = z;
		
		return player;
	}

    public static boolean swapBlocks(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        Block b1 = world.getBlock(x, y ,z);
        int meta1 = world.getBlockMetadata(x, y, z);
        Block b2 = world.getBlock(nx, ny, nz);
        int meta2 = world.getBlockMetadata(nx, ny, nz);

        if(b1 == SocketsMod.miniPortal) return false;
        if(b2 == SocketsMod.miniPortal) return false;

        //Block b1 = Block.blocksList[id1];
        //Block b2 = Block.blocksList[id2];

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
                    SocketsMod.network.sendToDimension(new AdapterSideMessage(ta, (byte) i), world.provider.dimensionId);
                }
            }

            if(te instanceof TileDirectionChanger)
            {
                TileDirectionChanger td = (TileDirectionChanger)te;
                for(int i = 0; i < 6; i++)
                {
                    SocketsMod.network.sendToDimension(new ChangerSideMessage(td, (byte) i), world.provider.dimensionId);
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
                    SocketsMod.network.sendToDimension(new AdapterSideMessage(ta, (byte) i), world.provider.dimensionId);
                }
            }

            if(te instanceof TileDirectionChanger)
            {
                TileDirectionChanger td = (TileDirectionChanger)te;
                for(int i = 0; i < 6; i++)
                {
                    SocketsMod.network.sendToDimension(new ChangerSideMessage(td, (byte) i), world.provider.dimensionId);
                }
            }
        }

        return true;
    }

    public static boolean canMoveBlock(World world, int x, int y, int z, int nx, int ny, int nz)
    {
        if(ny >= 255 || ny <= 0) return false;
        if(! isBlockReplaceable(world, nx, ny, nz)) return false;
        //int id = world.getBlockId(x, y, z);
        Block b = world.getBlock(x, y, z);
        if(b == SocketsMod.miniPortal) return false;
        //Block b = Block.blocksList[id];
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
        //int id = world.getBlockId(x, y, z);
        Block b = world.getBlock(x, y, z);
        if(b == SocketsMod.miniPortal) return false;
        //Block b = Block.blocksList[id];
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

        List ents = world.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 3, z + 1));//.getAABBPool().getAABB(x, y, z, x + 1, y + 3, z + 1));
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
                b == Blocks.flowing_water ||
                b == Blocks.water ||
                b == Blocks.flowing_lava ||
                b == Blocks.lava ||
                (b.isReplaceable(world, x, y, z) ||
                        b instanceof BlockFluidBase);
    }
	

}
