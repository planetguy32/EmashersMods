package emasher.sockets.packethandling;

import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import emasher.api.SideConfig;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;
import emasher.sockets.pipes.TileAdapterBase;
import emasher.sockets.pipes.TileDirectionChanger;
import emasher.sockets.pipes.TilePipeBase;
import javafx.collections.ListChangeListener;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

public class Handlers {
    public static void onSocketStateMessage(SocketStateMessage message, MessageContext ctx)
    {
        World world = Minecraft.getMinecraft().theWorld;

        int x = NetworkUtilities.toInteger(message.msg, 10);
        int y = NetworkUtilities.toInteger(message.msg, 14);
        int z = NetworkUtilities.toInteger(message.msg, 18);
        int side = message.msg[1];

        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileSocket)
        {
            TileSocket ts = (TileSocket)te;

            SideConfig c = ts.configs[side];

            c.meta = NetworkUtilities.toInteger(message.msg, 6);

            ts.sides[side] = NetworkUtilities.toInteger(message.msg, 22);
            c.tank = (int)message.msg[2];
            c.inventory = (int)message.msg[3];
            c.rsControl[0] = (message.msg[4] & 1) != 0;
            c.rsControl[1] = (message.msg[4] & 2) != 0;
            c.rsControl[2] = (message.msg[4] & 4) != 0;
            c.rsLatch[0] = (message.msg[5] & 1) != 0;
            c.rsLatch[1] = (message.msg[5] & 2) != 0;
            c.rsLatch[2] = (message.msg[5] & 4) != 0;
            ts.sides[side] = NetworkUtilities.toInteger(message.msg, 22);
            ts.sideLocked[side] = NetworkUtilities.byteToBool(message.msg[26]);
            ts.facID[side] = NetworkUtilities.toInteger(message.msg, 27);
            ts.facMeta[side] = NetworkUtilities.toInteger(message.msg, 31);

            world.markBlockForUpdate(x, y, z);
            world.notifyBlockChange(x, y, z, SocketsMod.socket);
        }
    }

    public static void onSocketItemMessage(SocketItemMessage message, MessageContext ctx)
    {
        World world = Minecraft.getMinecraft().theWorld;

        int x = NetworkUtilities.toInteger(message.msg, 9);
        int y = NetworkUtilities.toInteger(message.msg, 13);
        int z = NetworkUtilities.toInteger(message.msg, 17);
        int id = NetworkUtilities.toInteger(message.msg, 1);
        int damage = NetworkUtilities.toInteger(message.msg, 5);
        int inventory = message.msg[22];
        int size = NetworkUtilities.toInteger(message.msg, 23);

        ItemStack s = null;

        if(id != -1) {
            s = new ItemStack(Item.getItemById(id), size, damage);
            if (message.msg.length > 27) {
                NBTTagCompound NBTData = null;
                byte[] NBTArray = new byte[message.msg.length - 27];

                for (int i = 0; i < message.msg.length - 27; i++) {
                    NBTArray[i] = message.msg[i + 27];
                }

                try {
                    NBTData = CompressedStreamTools.func_152457_a(NBTArray, NBTSizeTracker.field_152451_a);
                    if (NBTData != null) s.setTagCompound(NBTData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

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

    public static void onSocketFluidMessage(SocketFluidMessage message, MessageContext ctx)
    {
        World world = Minecraft.getMinecraft().theWorld;

        int x = NetworkUtilities.toInteger(message.msg, 1);
        int y = NetworkUtilities.toInteger(message.msg, 5);
        int z = NetworkUtilities.toInteger(message.msg, 9);
        int id = NetworkUtilities.toInteger(message.msg, 13);
        int meta = NetworkUtilities.toInteger(message.msg, 17);
        int amnt = NetworkUtilities.toInteger(message.msg, 22);
        int tank = (int)message.msg[26];

        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileSocket)
        {
            TileSocket ts = (TileSocket)te;

            ts.tanks[tank].setFluid(new FluidStack(id, amnt));

            world.markBlockForUpdate(x, y, z);
            world.notifyBlockChange(x, y, z, SocketsMod.socket);
        }
    }

    public static void onPipeColourMessage(PipeColourMessage message, MessageContext ctx)
    {
        World world = Minecraft.getMinecraft().theWorld;

        int x = NetworkUtilities.toInteger(message.msg, 1);
        int y = NetworkUtilities.toInteger(message.msg, 5);
        int z = NetworkUtilities.toInteger(message.msg, 9);
        int id = NetworkUtilities.toInteger(message.msg, 13);
        int colour = message.msg[17];

        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TilePipeBase)
        {
            TilePipeBase p = (TilePipeBase)te;

            p.colour = colour;

            world.markBlockForUpdate(x, y, z);
            world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
        }
    }

    public static void onAdapterSideMessage(AdapterSideMessage message, MessageContext ctx)
    {
        World world = Minecraft.getMinecraft().theWorld;

        int x = NetworkUtilities.toInteger(message.msg, 1);
        int y = NetworkUtilities.toInteger(message.msg, 5);
        int z = NetworkUtilities.toInteger(message.msg, 9);
        int id = NetworkUtilities.toInteger(message.msg, 13);
        boolean output = false;
        if(message.msg[17] != 0) output = true;
        int side = (int)message.msg[18];

        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileAdapterBase)
        {
            TileAdapterBase t = (TileAdapterBase)te;

            t.outputs[side] = output;

            world.markBlockForUpdate(x, y, z);
            world.notifyBlockChange(x, y, z, world.getBlock(x, y, z));
        }
    }

    public static void onChangeSideMessage(ChangerSideMessage message, MessageContext ctx)
    {
        World world = Minecraft.getMinecraft().theWorld;

        int x = NetworkUtilities.toInteger(message.msg, 1);
        int y = NetworkUtilities.toInteger(message.msg, 5);
        int z = NetworkUtilities.toInteger(message.msg, 9);
        int id = NetworkUtilities.toInteger(message.msg, 13);
        ForgeDirection dir = ForgeDirection.getOrientation(message.msg[17]);
        int side = (int)message.msg[18];

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
