package emasher.gas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import emasher.core.EmasherCore;
import emasher.gas.CommonProxy;

public class BlockHydrogen extends BlockGasGeneric
{
	public BlockHydrogen(int ID)
    {
        super(ID, 0, true, true, false);
		//this.setCreativeTab(EmasherCore.tabEmasher);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
		this.blockIcon = ir.registerIcon("gascraft:hydrogen");
    }
    
}