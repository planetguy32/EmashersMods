package emasher.gas.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockHydrogen extends BlockGasGeneric
{
	public BlockHydrogen()
    {
        super(0, true, true, false);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir)
    {
		this.blockIcon = ir.registerIcon("gascraft:hydrogen");
    }

}