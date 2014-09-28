package emasher.sockets.packethandling;


import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.NetworkRegistry;
import emasher.api.SideConfig;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import emasher.sockets.pipes.TileAdapterBase;
import emasher.sockets.pipes.TileDirectionChanger;
import emasher.sockets.pipes.TilePipeBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class PacketTileEntity extends AbstractPacket{

    private int x, y, z;
    private byte index;
    private byte[] msg;

    public PacketTileEntity()
    {

    }

    /*public PacketTileEntity(TileEntity te)
    {
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
    }*/

    public PacketTileEntity(TileEntity te, byte[] out)
    {
        x = te.xCoord;
        y = te.yCoord;
        z = te.zCoord;
        
        index = out[0];
        msg = out;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeBytes(msg);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        int i = buffer.array().length;
        msg = new byte[i-1];
        buffer.getBytes(0, msg);
        index = msg[0];
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        //World world = player.worldObj;
        //TileEntity te = world.getTileEntity(x, y, z);

        if(index == 0)
        {
            x = NetworkUtilities.toInteger(msg, 10);
            y = NetworkUtilities.toInteger(msg, 14);
            z = NetworkUtilities.toInteger(msg, 18);
            int side = msg[1];

            World world = player.worldObj;
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                SideConfig c = ts.configs[side];

                c.meta = NetworkUtilities.toInteger(msg, 6);

                ts.sides[side] = (int)msg[1];
                c.tank = (int)msg[2];
                c.inventory = (int)msg[3];
                c.rsControl[0] = (msg[4] & 1) != 0;
                c.rsControl[1] = (msg[4] & 2) != 0;
                c.rsControl[2] = (msg[4] & 4) != 0;
                c.rsLatch[0] = (msg[5] & 1) != 0;
                c.rsLatch[1] = (msg[5] & 2) != 0;
                c.rsLatch[2] = (msg[5] & 4) != 0;
                ts.sides[side] = NetworkUtilities.toInteger(msg, 22);
                ts.sideLocked[side] = NetworkUtilities.byteToBool(msg[26]);
                ts.facID[side] = NetworkUtilities.toInteger(msg, 27);
                ts.facMeta[side] = NetworkUtilities.toInteger(msg, 31);

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, SocketsMod.socket);
            }
        }
        else if(index == 1)
        {
            x = NetworkUtilities.toInteger(msg, 9);
            y = NetworkUtilities.toInteger(msg, 13);
            z = NetworkUtilities.toInteger(msg, 17);
            int id = NetworkUtilities.toInteger(msg, 1);
            int damage = NetworkUtilities.toInteger(msg, 5);
            int inventory = msg[22];
            int size = NetworkUtilities.toInteger(msg, 23);

            ItemStack s = null;

            if(id != -1)
            {
                s = new ItemStack(Item.getItemById(id), size, damage);
                if(msg.length > 27)
                {
                    NBTTagCompound NBTData = null;
                    byte[] NBTArray = new byte[msg.length - 27];

                    for(int i = 0; i < msg.length - 27; i++)
                    {
                        NBTArray[i] = msg[i + 27];
                    }

                    try
                    {
                        NBTData = CompressedStreamTools.func_152457_a(NBTArray, NBTSizeTracker.field_152451_a);
                        if(NBTData != null) s.setTagCompound(NBTData);
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            World world = player.worldObj;
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                if(id != -1) ts.inventory.setInventorySlotContents(inventory, new ItemStack(Item.getItemById(id), size, damage));
                else ts.inventory.setInventorySlotContents(inventory, null);

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, SocketsMod.socket);
            }
        }
        else if(index == 2)
        {
            x = NetworkUtilities.toInteger(msg, 1);
            y = NetworkUtilities.toInteger(msg, 5);
            z = NetworkUtilities.toInteger(msg, 9);
            int id = NetworkUtilities.toInteger(msg, 13);
            int meta = NetworkUtilities.toInteger(msg, 17);
            int amnt = NetworkUtilities.toInteger(msg, 22);
            int tank = (int)msg[26];

            World world = player.worldObj;
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                ts.tanks[tank].setFluid(new FluidStack(id, amnt));

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, SocketsMod.socket);
            }
        }
        else if(index == 3)
        {
            x = NetworkUtilities.toInteger(msg, 1);
            y = NetworkUtilities.toInteger(msg, 5);
            z = NetworkUtilities.toInteger(msg, 9);
            int id = NetworkUtilities.toInteger(msg, 13);
            int colour = msg[17];

            World world = player.worldObj;
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TilePipeBase)
            {
                TilePipeBase p = (TilePipeBase)te;

                p.colour = colour;

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
            }
        }
        else if(index == 4)
        {
            x = NetworkUtilities.toInteger(msg, 1);
            y = NetworkUtilities.toInteger(msg, 5);
            z = NetworkUtilities.toInteger(msg, 9);
            int id = NetworkUtilities.toInteger(msg, 13);
            boolean output = false;
            if(msg[17] != 0) output = true;
            int side = (int)msg[18];

            World world = player.worldObj;
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileAdapterBase)
            {
                TileAdapterBase t = (TileAdapterBase)te;

                t.outputs[side] = output;

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
            }
        }
        else if(index == 5)
        {
            x = NetworkUtilities.toInteger(msg, 1);
            y = NetworkUtilities.toInteger(msg, 5);
            z = NetworkUtilities.toInteger(msg, 9);
            int id = NetworkUtilities.toInteger(msg, 13);
            ForgeDirection dir = ForgeDirection.getOrientation(msg[17]);
            int side = (int)msg[18];

            World world = player.worldObj;
            TileEntity te = world.getTileEntity(x, y, z);
            if(te != null && te instanceof TileDirectionChanger)
            {
                TileDirectionChanger t = (TileDirectionChanger)te;

                t.directions[side] = dir;

                world.markBlockForUpdate(x, y, z);
                world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
            }
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        x = NetworkUtilities.toInteger(msg, 1);
        y = NetworkUtilities.toInteger(msg, 5);
        z = NetworkUtilities.toInteger(msg, 9);

        World world = player.worldObj;
        TileEntity te = world.getTileEntity(x, y, z);

        int dimID;
        if(index == 0)
        {
            int side = msg[17];
            dimID = NetworkUtilities.toInteger(msg, 13);


            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                PacketHandler.instance.SendClientSideState(ts, (byte) side);
            }
        }
        else if(index == 1)
        {
            int inventory = msg[17];
            dimID = NetworkUtilities.toInteger(msg, 13);

            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                PacketHandler.instance.SendClientInventorySlot(ts, (byte) inventory);
            }
        }
        else if(index == 2)
        {
            int tank = msg[17];
            dimID = NetworkUtilities.toInteger(msg, 13);

            if(te != null && te instanceof TileSocket)
            {
                TileSocket ts = (TileSocket)te;

                PacketHandler.instance.SendClientTankSlot(ts, (byte) tank);
            }
        }
        else if(index == 3)
        {
            if(te != null && te instanceof TilePipeBase)
            {
                TilePipeBase p = (TilePipeBase)te;

                PacketHandler.instance.sendClientPipeColour(p);
            }
        }
        else if(index == 4)
        {
            if(te != null && te instanceof TileAdapterBase)
            {
                TileAdapterBase t = (TileAdapterBase)te;

                for(int i = 0; i < 6; i++)
                {
                    PacketHandler.instance.sendClientAdapterSide(t, i);
                }
            }
        }
        else if(index == 5)
        {
            if(te != null && te instanceof TileDirectionChanger)
            {
                TileDirectionChanger t = (TileDirectionChanger)te;

                for(int i = 0; i < 6; i++)
                {
                    PacketHandler.instance.sendClientChangerSide(t, i);
                }
            }
        }
    }

}
