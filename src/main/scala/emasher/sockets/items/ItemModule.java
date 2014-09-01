package emasher.sockets.items;

import java.util.List;

import net.minecraft.block.Block;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.ModuleRegistry;
import emasher.api.SocketModule;
import emasher.core.EmasherCore;
import emasher.sockets.BlockSocket;
import emasher.sockets.SocketsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemModule extends Item
{
	@SideOnly(Side.CLIENT)
	public IIcon[] textures;
	
	public ItemModule(int id)
	{
		super();
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setHasSubtypes(true);
		this.setUnlocalizedName("socket_module");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage)
    {
        return textures[damage];
    }
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player)
    {
		if(player.isSneaking() && ! world.isRemote && player.capabilities.isCreativeMode)
		{
			do
			{
				item.setItemDamage(item.getItemDamage() + 1);
				if(item.getItemDamage() == ModuleRegistry.numModules) item.setItemDamage(0);
			} while(ModuleRegistry.getModule(item.getItemDamage()) == null);
		}
		
		return item;
    }
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			SocketModule m = ModuleRegistry.getModule(par1ItemStack.getItemDamage());
			par3List.add("");
			m.getToolTip(par3List);
			par3List.add("");
			m.getIndicatorKey(par3List);
			
			for(int i = 0; i < 2; i++)
			{
				Object l = par3List.get(par3List.size() - 1);
				if((l instanceof String) && l.equals(""))
				{
					par3List.remove(par3List.size() - 1);
				}
			}
		}
		else
		{
			par3List.add(EnumChatFormatting.GOLD + (EnumChatFormatting.ITALIC + "Hold shift for info..."));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) 
	{
		return getUnlocalizedName() + "." + itemstack.getItemDamage();
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List list)
	{
		for(int i = 1; i < ModuleRegistry.numModules; i++)
		{
			if(ModuleRegistry.getModule(i) != null)
			{
				list.add(new ItemStack(item, 1, i));
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister ir)
	{
		SocketModule m;
		int l;
		int temp;
		
		this.itemIcon = ir.registerIcon("bg");
		
		textures = new IIcon[ModuleRegistry.numModules];
		
		for(int i = 0; i < ModuleRegistry.numModules; i++)
		{
			m = ModuleRegistry.getModule(i);
			if(m != null)
			{
				textures[i] = ir.registerIcon(m.textureFiles[0]);
			}
		}
	}

}
