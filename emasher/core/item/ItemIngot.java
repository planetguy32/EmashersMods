package emasher.core.item;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

public class ItemIngot extends Item
{
	@SideOnly(Side.CLIENT)
	public Icon[] textures;

	public ItemIngot(int id)
	{
		super(id);
		this.setCreativeTab(EmasherCore.tabEmasher);
		setHasSubtypes(true);
	}
	
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int damage)
	{
		return textures[damage];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister ir)
	{
		textures = new Icon[9];
		
		textures[0] = ir.registerIcon("emashercore:ingotAluminium");
		textures[1] = ir.registerIcon("emashercore:ingotBronze");
		textures[2] = ir.registerIcon("emashercore:ingotCopper");
		textures[3] = ir.registerIcon("emashercore:ingotLead");
		textures[4] = ir.registerIcon("emashercore:ingotNickel");
		textures[5] = ir.registerIcon("emashercore:ingotPlatinum");
		textures[6] = ir.registerIcon("emashercore:ingotSilver");
		textures[7] = ir.registerIcon("emashercore:ingotSteel");
		textures[8] = ir.registerIcon("emashercore:ingotTin");
		
		this.itemIcon = textures[0];
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack)
	 {
		String name = "";
		switch(itemstack.getItemDamage()) 
		{
		case 0:
			name = "e_ingotAluminium";
			break;
		case 1:
			name = "e_ingotBronze";
			break;
		case 2:
			name = "e_ingotCopper";
			break;
		case 3:
			name = "e_ingotLead";
			break;
		case 4:
			name = "e_ingotNickel";
			break;
		case 5:
			name = "e_ingotPlatinum";
			break;
		case 6:
			name = "e_ingotSilver";
			break;
		case 7:
			name = "e_ingotSteel";
			break;
		case 8:
			name = "e_ingotTin";
			break;
		}
		return getUnlocalizedName() + "." + name;
	 }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
	{	
		for(int i = 0; i < 9; i++) par3List.add(new ItemStack(par1, 1, i));
    }

}
