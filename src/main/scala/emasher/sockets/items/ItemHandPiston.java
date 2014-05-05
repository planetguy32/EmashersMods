package emasher.sockets.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class ItemHandPiston extends Item
{
	public ItemHandPiston(int id)
	{
		super(id);
		
		this.setCreativeTab(SocketsMod.tabSockets);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("eng_handPiston");
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
		this.itemIcon = ir.registerIcon("sockets:handPiston");
	}
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float par8, float par9, float par10)
    {
		ForgeDirection d = ForgeDirection.getOrientation(side);
		ForgeDirection o = d.getOpposite();
		
		int xo = x + o.offsetX;
		int yo = y + o.offsetY;
		int zo = z + o.offsetZ;
		
		//int id == world.getBlock
		
		if(world.getBlockId(xo, yo, zo) == 0)
		{
			
			
			return true;
		}
		
		return false;
    }
	
}
