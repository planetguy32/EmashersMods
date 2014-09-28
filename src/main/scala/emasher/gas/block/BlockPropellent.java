package emasher.gas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import emasher.gas.CommonProxy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.*;

public class BlockPropellent extends BlockGasGeneric
{
	
	public BlockPropellent(int ID)
    {
        super(ID, 0, false, true, true);
		//this.setCreativeTab(EmasherCore.tabEmasher);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
		this.blockIcon = ir.registerIcon("gascraft:propellent");
    }
}
