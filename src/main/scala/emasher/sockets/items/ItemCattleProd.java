package emasher.sockets.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class ItemCattleProd extends Item
{

	public ItemCattleProd(int id)
	{
		super(id);
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setMaxDamage(64);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("cattleProd");
		
	}
	
	@Override
	public boolean isItemTool(ItemStack par1ItemStack)
    {
		return true;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		this.itemIcon = ir.registerIcon("sockets:cattleProd");
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity)
    {
		if(entity instanceof EntityAnimal)
		{
			
			EntityAnimal an = (EntityAnimal)entity;
			
			an.attackEntityFrom(DamageSource.magic, 0);
			
			return true;
		}
		
        return false;
    }

}
