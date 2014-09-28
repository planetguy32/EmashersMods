package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.api.Util;
//import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;

public class ModBlockPlacer extends SocketModule
{

	public ModBlockPlacer(int id)
	{
		super(id, "sockets:blockPlacer", "sockets:blockPlacerSneak");
	}

	@Override
	public String getLocalizedName()
	{
		return "Activator";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Simulates a player's right click");
		l.add("Compatible with most items");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Optional inventory containing 'held' item");
		l.add(SocketsMod.PREF_RED + "RS Activation Pulse");
		l.add(SocketsMod.PREF_WHITE + "Toggle 'sneak' mode");
		l.add(SocketsMod.PREF_YELLOW + "Extra item output sent to Machine Output");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "h", "d", "b", Character.valueOf('d'), Blocks.dispenser, Character.valueOf('h'), Blocks.piston,
				Character.valueOf('u'), Blocks.trapdoor, Character.valueOf('b'), SocketsMod.blankSide);
	}
	
	public boolean hasInventoryIndicator() { return true; }
	public boolean hasRSIndicator() { return true; }
	
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		if(config.meta == 0) config.meta = 1;
		else config.meta = 0;
		ts.sendClientSideState(side.ordinal());
	}
	
	public int getCurrentTexture(SideConfig config)
	{
		return config.meta;
	}
	
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on)
	{
		if(on && config.rsControl[index])
		{
			//PacketHandler.instance.doClick(ts, side);
			
			doClick(config, ts, side);
			
			if(config.inventory >= 0 && config.inventory <= 2) ts.sendClientInventorySlot(config.inventory);
			ts.sendClientSideState(side.ordinal());
		}
		
		
	}
	
	public void doClick(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
			EntityPlayer fakePlayer = Util.createFakePlayer(ts.getWorldObj(), ts.xCoord, ts.yCoord, ts.zCoord);
			fakePlayer.inventory.currentItem = 0;
			fakePlayer.worldObj = ts.getWorldObj();
			int xo = ts.xCoord + side.offsetX;
			int yo = ts.yCoord + side.offsetY;
			int zo = ts.zCoord + side.offsetZ;
			Block b = ts.getWorldObj().getBlock(xo, yo, zo);
			
			if(config.inventory >= 0 && config.inventory <= 2 && ts.getStackInInventorySlot(config.inventory) != null )
			{
					ItemStack stack = ts.getStackInInventorySlot(config.inventory);
					Item theItem = stack.getItem();
					ts.setInventoryStack(config.inventory, null);
					
					fakePlayer.setCurrentItemOrArmor(0, stack);
					
					fakePlayer.worldObj = ts.getWorldObj();
					fakePlayer.setSneaking(config.meta == 1);
					
					fakePlayer.posY++;
					int useOffset = -1;
					
					if(! theItem.onItemUseFirst(stack, fakePlayer, ts.getWorldObj(), ts.xCoord + side.offsetX, ts.yCoord + side.offsetY, ts.zCoord + side.offsetZ, 1, 0.5f, 0.5f, 0.5f))
					{
						if(ts.getWorldObj().isAirBlock(xo, yo, zo) || (config.meta == 0 || theItem.doesSneakBypassUse(ts.getWorldObj(), ts.xCoord, ts.yCoord, ts.zCoord, fakePlayer)))
						{
							if(! theItem.onItemUse(stack, fakePlayer, ts.getWorldObj(), ts.xCoord + side.offsetX, ts.yCoord + side.offsetY - 1, ts.zCoord + side.offsetZ, 1, 0.5f, 0.5f, 0.5f))
							{	
								boolean doRC = true;
								if(ts.getWorldObj().isAirBlock(xo, yo, zo))
								{
									doRC = false;
									List<Entity> l = ts.getWorldObj().getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(xo, yo, zo, xo + 1, yo + 1, zo + 1));
									if(l.size() > 0)
									{
										Entity toUse = l.get(0);
										if(toUse instanceof EntityLiving)
										{
											if(! ((EntityLiving)toUse).interactFirst(fakePlayer))
											{
												doRC = ! theItem.itemInteractionForEntity(stack, fakePlayer, (EntityLiving)toUse);
											}
											
										}
									}
									else
									{
										doRC = true;
									}
								}
								
								if(doRC && ! theItem.onItemUse(stack, fakePlayer, ts.getWorldObj(), ts.xCoord + side.offsetX, ts.yCoord + side.offsetY, ts.zCoord + side.offsetZ, 1, 0.5f, 0.5f, 0.5f))
								{
									float ax = 0;//0F * side.offsetX;
									float ay = 0;//0F * side.offsetY;
									float az = 0;//0F * side.offsetZ;
									
									if(side.offsetX == 0) ax = 0.5F;
									if(side.offsetY == 0) ay = 0.5F;
									if(side.offsetZ == 0) az = 0.5F;
									
									fakePlayer.posX = xo + ax;
									fakePlayer.posY = yo + ay;
									fakePlayer.posZ = zo + az;
									fakePlayer.prevPosX = xo + ax;
									fakePlayer.prevPosY = yo + ay;
									fakePlayer.prevPosZ = zo + az;
									
									
									switch(side)
									{
									case WEST:
										fakePlayer.rotationPitch = 0;
										fakePlayer.rotationYaw = 90;
										break;
									case NORTH:
										fakePlayer.rotationPitch = 0;
										fakePlayer.rotationYaw = 180;
										//fakePlayer.posZ += 0.9F;
										//fakePlayer.prevPosZ += 0.9F;
										break;
									case EAST:
										fakePlayer.rotationPitch = 0;
										fakePlayer.rotationYaw = -90;
										break;
									case SOUTH:
										fakePlayer.rotationPitch = 0;
										fakePlayer.rotationYaw = 1;
										fakePlayer.posZ += 0.5F;
										fakePlayer.prevPosZ += 0.5F;
										break;
									case UP:
										fakePlayer.rotationPitch = 271;
										fakePlayer.rotationYaw = 0;
										break;
									case DOWN:
										fakePlayer.rotationPitch = 90;
										fakePlayer.rotationYaw = 0;
										break;
									default:
										;
									}
									
									
									fakePlayer.inventory.setInventorySlotContents(0, theItem.onItemRightClick(stack, ts.getWorldObj(), fakePlayer));
									
								}
							}
						}
						else
						{
							tryActivateBlock(config, ts, side, fakePlayer, xo, yo, zo, b);
						}
					}
				
				//fakePlayer.worldObj = null;
				stack = fakePlayer.inventory.getCurrentItem();
				for(int i = 1; i < fakePlayer.inventory.getSizeInventory(); i++)
				{
					if(fakePlayer.inventory.getStackInSlot(i) != null)
					{
						ts.forceOutputItem(fakePlayer.inventory.getStackInSlot(i));
						fakePlayer.inventory.setInventorySlotContents(i, null);
					}
				}
				fakePlayer.setCurrentItemOrArmor(0, null);
				//if(stack.stackSize == 0) System.out.println("Stack is 0");
				ts.setInventoryStack(config.inventory, stack);
			}
			else
			{
				tryActivateBlock(config, ts, side, fakePlayer, xo, yo, zo, b);
				
				for(int i = 0; i < fakePlayer.inventory.getSizeInventory(); i++)
				{
					if(fakePlayer.inventory.getStackInSlot(i) != null) ts.forceOutputItem(fakePlayer.inventory.getStackInSlot(i));
					fakePlayer.inventory.setInventorySlotContents(i, null);
				}
			}
	}
	
	public void tryActivateBlock(SideConfig config, SocketTileAccess ts, ForgeDirection side, EntityPlayer fakePlayer, int x, int y, int z, Block block)
	{
		if(Block.blockRegistry.containsId(Block.getIdFromBlock(block)))
		{
			if(! block.onBlockActivated(ts.getWorldObj(), x, y, z, fakePlayer, side.getOpposite().ordinal(), 0.5f, 0.5f, 0.5f))
			{
				block.onBlockClicked(ts.getWorldObj(), x, y, z, fakePlayer);
			}
		}
		
		if(ts.getWorldObj().isAirBlock(x, y, z))
		{
			List<Entity> l = ts.getWorldObj().getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1));
			if(l.size() > 0)
			{
				Entity toUse = l.get(0);
				if(toUse instanceof EntityLiving)
				{
					((EntityLiving)toUse).interactFirst(fakePlayer);
				}
			}
		}
	}

}
