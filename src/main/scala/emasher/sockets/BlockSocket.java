package emasher.sockets;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.packethandling.PacketPipeline;
import emasher.sockets.packethandling.PacketTileEntity;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import emasher.api.ModuleRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.core.EmasherCore;
import emasher.sockets.items.*;
import buildcraft.api.tools.IToolWrench;

public class BlockSocket extends BlockContainer
{
	@SideOnly(Side.CLIENT)
	public IIcon[][] textures;
	@SideOnly(Side.CLIENT)
	public IIcon[] tankIndicator;
	@SideOnly(Side.CLIENT)
	public IIcon[] inventoryIndicator;
	@SideOnly(Side.CLIENT)
	public IIcon[] rsIndicator;
	@SideOnly(Side.CLIENT)
	public IIcon[] latchIndicator;
	@SideOnly(Side.CLIENT)
	public IIcon[] bar1;
	@SideOnly(Side.CLIENT)
	public IIcon[] bar2;
	@SideOnly(Side.CLIENT)
	public IIcon buttonInd;
	@SideOnly(Side.CLIENT)
	public IIcon[] chargeInd;
	@SideOnly(Side.CLIENT)
	public IIcon hasData;
	
	public static final String[] dyes =
        {
            "dyeBlack",
            "dyeRed",
            "dyeGreen",
            "dyeBrown",
            "dyeBlue",
            "dyePurple",
            "dyeCyan",
            "dyeLightGray",
            "dyeGray",
            "dyePink",
            "dyeLime",
            "dyeYellow",
            "dyeLightBlue",
            "dyeMagenta",
            "dyeOrange",
            "dyeWhite"
        };
	
	public BlockSocket(int id)
	{
		super(Material.iron);
		setCreativeTab(SocketsMod.tabSockets);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileSocket();
	}
	
	@Override
	public boolean hasTileEntity()
	{
		return true;
	}
	
	
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
	{
		TileEntity t = world.getTileEntity(x, y, z);

		if(! world.isRemote && t != null && t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			
			if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == SocketsMod.remote && ! player.isSneaking())
			{
				switch(player.getCurrentEquippedItem().getItemDamage())
				{
				case 0:
					if(ts.getSide(ForgeDirection.getOrientation(side)).hasTankIndicator() && !ts.sideLocked[side]) ts.nextTank(side);
					break;
				case 1:
					if(ts.getSide(ForgeDirection.getOrientation(side)).hasInventoryIndicator() && !ts.sideLocked[side]) ts.nextInventory(side);
					break;
				case 2:
					if(ts.getSide(ForgeDirection.getOrientation(side)).hasRSIndicator() && !ts.sideLocked[side]) ts.nextRS(side);
					break;
				case 3:
					if(ts.getSide(ForgeDirection.getOrientation(side)).hasLatchIndicator() && !ts.sideLocked[side]) ts.nextLatch(side);
					break;
				case 4:
					if(!ts.sideLocked[side]) ts.getSide(ForgeDirection.getOrientation(side)).onGenericRemoteSignal(ts, ts.configs[side], ForgeDirection.getOrientation(side), subX, subY, subZ);
					break;
				case 5:
					ts.lockSide(side);
					break;
				case 6:
					ts.facID[side] = 0;
					ts.facMeta[side] = 0;
                    PacketHandler.instance.SendClientSideState(ts, (byte)side);
                    break;
				}
			}
			else if(player.getCurrentEquippedItem() != null && (player.getCurrentEquippedItem().getItem() instanceof IToolWrench))
			{
				if(player.getCurrentEquippedItem().getItem() instanceof ItemEngWrench && player.getCurrentEquippedItem().getItemDamage() > 0)
				{
					int dam = player.getCurrentEquippedItem().getItemDamage();
					if(dam == 1)
					{
						ItemStack theStack = new ItemStack(this, 1, 0);
						
						NBTTagCompound data = new NBTTagCompound();
						ts.writeToNBT(data);
						theStack.setTagCompound(data);
						
						if (! world.isRemote)
				        {
				            float f = 0.7F;
				            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
				            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
				            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
				            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, theStack);
				            entityitem.delayBeforeCanPickup = 10;
				            world.spawnEntityInWorld(entityitem);
				        }
						
						world.setBlockToAir(x, y, z);
					}
					else
					{
						int sideID = ts.sides[side];
						
						if(sideID != 0 && ! ts.sideLocked[side])
						{
							ItemStack theStack = new ItemStack(SocketsMod.module, 1, sideID);
							
							if (! world.isRemote)
					        {
								ForgeDirection d = ForgeDirection.getOrientation(side);
								int xo = x + d.offsetX;
								int yo = y + d.offsetY;
								int zo = z + d.offsetZ;
								
					            float f = 0.7F;
					            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
					            EntityItem entityitem = new EntityItem(world, (double)xo + d0, (double)yo + d1, (double)zo + d2, theStack);
					            entityitem.delayBeforeCanPickup = 5;
					            world.spawnEntityInWorld(entityitem);
					            ts.getSide(d).onRemoved(ts, ts.configs[side], ForgeDirection.getOrientation(side));
					        }
							
							
							ts.sides[side] = 0;
							ts.resetConfig(side);
						}
					}
				}
				else if(player.isSneaking())
				{
					ItemStack theStack = new ItemStack(this, 1, 0);
					
					NBTTagCompound data = new NBTTagCompound();
					ts.writeToNBT(data);
					//data.setBoolean("ench", true);
					theStack.setTagCompound(data);
					
					if (! world.isRemote)
			        {
			            float f = 0.7F;
			            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
			            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, theStack);
			            entityitem.delayBeforeCanPickup = 10;
			            world.spawnEntityInWorld(entityitem);
			        }
					
					world.setBlockToAir(x, y, z);
					world.removeTileEntity(x, y, z);
				}
				else
				{	
					int sideID = ts.sides[side];
					
					if(sideID != 0 && ! ts.sideLocked[side])
					{
						ItemStack theStack = new ItemStack(SocketsMod.module, 1, sideID);
						
						if (! world.isRemote)
				        {
				            float f = 0.7F;
				            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
				            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
				            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
				            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, theStack);
				            entityitem.delayBeforeCanPickup = 10;
				            world.spawnEntityInWorld(entityitem);
				            ts.getSide(ForgeDirection.getOrientation(side))
				            	.onRemoved(ts, ts.configs[side], ForgeDirection.getOrientation(side));
				        }
						
						
						ts.sides[side] = 0;
						ts.resetConfig(side);
					}
				}
			}
			else if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == SocketsMod.module)
			{
				if(ts.sides[side] == 0 || player.capabilities.isCreativeMode)
				{
					if(ModuleRegistry.getModule(player.getCurrentEquippedItem().getItemDamage()).canBeInstalled(ts, ForgeDirection.getOrientation(side)))
					{
						ts.sides[side] = player.getCurrentEquippedItem().getItemDamage();
						ts.resetConfig(side);
						ts.getSide(ForgeDirection.getOrientation(side)).init(ts, ts.configs[side], ForgeDirection.getOrientation(side));
						if(! player.capabilities.isCreativeMode) player.getCurrentEquippedItem().stackSize--;
					}
				}
			}
			/*else if(Loader.isModLoaded("BuildCraft|Core") && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemFacade)
			{
				int bId = ItemFacade.getBlockId(player.getCurrentEquippedItem());
				
				if(Block.blocksList[bId] != null && Block.blocksList[bId].isOpaqueCube())
				{
					ts.facID[side] = bId;
					ts.facMeta[side] =ItemFacade.getMetaData(player.getCurrentEquippedItem());
					PacketHandler.instance.SendClientSideState(ts, (byte)side);
					player.getCurrentEquippedItem().stackSize--;
				}
			}*/
			else
			{
				SocketModule m = ts.getSide(ForgeDirection.getOrientation(side));
				boolean wasDye = false;

                if(m.canModuleBeDyed())
                {
						
                    if(player.getCurrentEquippedItem() != null)
                    {
                        int oreId = OreDictionary.getOreID(player.getCurrentEquippedItem());
                        for(int i = 0; i < dyes.length; i++)
                        {
                            if(oreId == OreDictionary.getOreID(dyes[i]))
                            {
                                wasDye = true;
                                m.changeColour(i, ts.configs[side], ts, ForgeDirection.getOrientation(side));
                                player.getCurrentEquippedItem().stackSize--;
                                break;
                            }
                        }
                    }
                }
				
				if(! wasDye) m.onSideActivated(ts, ts.configs[side], ForgeDirection.getOrientation(side), player, subX, subY, subZ);
			}
			
		}
		
		if(t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			ItemStack item = player.getCurrentEquippedItem();
			if((item == null || (! (item.getItem() instanceof IToolWrench) && item.getItem() != SocketsMod.module && item.getItem() != SocketsMod.remote))  && side >= 0 && side < 6)
			{
				int oreId = OreDictionary.getOreID(player.getCurrentEquippedItem());
				boolean wasDye = false;
				for(int i = 0; i < dyes.length; i++)
				{
					if(oreId == OreDictionary.getOreID(dyes[i]))
					{
						wasDye = true;
						break;
					}
				}
				SocketModule m = ts.getSide(ForgeDirection.getOrientation(side));
				if(m != null && ! wasDye) m.onSideActivatedClient(ts, ts.configs[side], ForgeDirection.getOrientation(side), player, subX, subY, subZ);
			}
		}
		return true;
	}
	
	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity)
	{
		TileEntity t = world.getTileEntity(x, y, z);
		SocketModule m;
		
		if(! world.isRemote && t != null && t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			for(int i = 0; i < 6; i++)
			{
				m = ts.getSide(ForgeDirection.getOrientation(i));
				m.onEntityWalkOn(ts, ts.configs[i], ForgeDirection.getOrientation(i), entity);
			}
		}
	}
	
	
	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6)
	{
		//System.out.println("Break");
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
		par1World.removeTileEntity(par2, par3, par4);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int blockSide)
    {
		IIcon result = blockIcon;
		
		TileEntity t = world.getTileEntity(x, y, z);
		
		if(t != null && t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			
			if(ts.facID[blockSide] > 0 && Block.getBlockById(ts.facID[blockSide]) != null)
			{
				Block b = Block.getBlockById(ts.facID[blockSide]);
				result = b.getIcon(blockSide, ts.facMeta[blockSide]);
			}
			else if(ts.sides[blockSide] != 0)
			{
				SocketModule m = ts.getSide(ForgeDirection.getOrientation(blockSide));
				SideConfig c = ts.configs[blockSide];
				int index = m.getCurrentTexture(c, ts, ForgeDirection.getOrientation(blockSide));
				result = textures[m.moduleID][index];
			}
		}
		
		return result;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
		SocketModule m;
		int l;
		int temp;
		
		this.blockIcon = ir.registerIcon("sockets:bg");
		
		textures = new IIcon[ModuleRegistry.numModules][];
		tankIndicator = new IIcon[4];
		inventoryIndicator = new IIcon[4];
		rsIndicator = new IIcon[8];
		latchIndicator = new IIcon[8];
		bar1 = new IIcon[8];
		bar2 = new IIcon[8];
		chargeInd = new IIcon[13];
		
		for(int i = 0; i < ModuleRegistry.numModules; i++)
		{
			m = ModuleRegistry.getModule(i);
			if(m != null)
			{
				l = m.textureFiles.length;
				textures[i] = new IIcon[l];
				for(int j = 0; j < l; j++)
				{
					textures[i][j] = ir.registerIcon(m.textureFiles[j]);
				}
			}
		}
		
		for(int i = -1; i < 3; i++)
		{
			if(i == -1) temp = 3;
			else temp = i;
			tankIndicator[temp] = ir.registerIcon("sockets:ind_l_" + i);
			inventoryIndicator[temp] = ir.registerIcon("sockets:ind_i_"+i);
		}
		
		for(int i = 0; i < 8; i++)
		{
			rsIndicator[i] = ir.registerIcon("sockets:ind_r_" + i);
			latchIndicator[i] = ir.registerIcon("sockets:ind_a_" + i);
			bar1[i] = ir.registerIcon("sockets:rsInd" + i);
			bar2[i] = ir.registerIcon("sockets:timeInd" + i);
		}
		
		for(int i = 0; i < 13; i++)
		{
			chargeInd[i] = ir.registerIcon("sockets:chargeInd" + i);
		}
		
		buttonInd = ir.registerIcon("sockets:buttonInd");
		hasData = ir.registerIcon("sockets:hasData");
		
    }
	
	@Override
    public boolean canProvidePower()
    {
        return true;
    }
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		if(side < 0 || side > 5) return false;
		
		TileEntity t = world.getTileEntity(x, y, z);
		
		if(t != null && t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(side));
			//System.out.println(side + ", " + ts.sides[side]);
			return m.isRedstoneInterface();
		}
		
		return false;
	}
	
	
	
	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block nblock)
	{
		TileEntity t = world.getTileEntity(x, y, z);
		
		if(t != null && t instanceof TileSocket && ! world.isRemote)
		{
			TileSocket ts = (TileSocket)t;
			SocketModule m;
			
			for(int i = 0; i < 6; i++)
			{
				ForgeDirection d = ForgeDirection.getOrientation(i);
				ForgeDirection opposite = d.getOpposite();
				m = ts.getSide(opposite);
				
				if(nblock != Blocks.redstone_wire && ts.initialized)
				{
					m.onAdjChange(ts, ts.configs[opposite.ordinal()], opposite);
					ts.checkSideForChange(i);
				}
				
				boolean rs = world.getIndirectPowerOutput(x + opposite.offsetX, y + opposite.offsetY, z + opposite.offsetZ, opposite.ordinal());
				
				boolean oldRS = ts.sideRS[opposite.ordinal()];
				if(rs != oldRS)
				{	
					ts.sideRS[opposite.ordinal()] = rs;
					m.updateRestone(rs, ts.configs[opposite.ordinal()], ts);
                    PacketHandler.instance.SendClientSideState(ts, (byte)opposite.ordinal());
				}
			}
		}
	}
	
	@Override
    public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side)
    {
		TileEntity t = world.getTileEntity(x, y, z);
		
		side = ForgeDirection.OPPOSITES[side];
		
		if(t != null && t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(side));
			SideConfig c = ts.configs[side];
			
			if(m.isOutputingRedstone(c, ts)) return 15;
		}
		
		return 0;
    }
	
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side)
    {
		TileEntity t = world.getTileEntity(x, y, z);
		
		side = ForgeDirection.OPPOSITES[side];
		
		if(t != null && t instanceof TileSocket)
		{
			TileSocket ts = (TileSocket)t;
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(side));
			SideConfig c = ts.configs[side];
			
			if(m.isOutputingRedstone(c, ts)) return 15;
		}
		
		return 0;
    }
	
	@Override
	public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {		
		return true;
    }

}
