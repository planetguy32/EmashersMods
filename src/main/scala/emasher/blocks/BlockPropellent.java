package emasher.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;

public class BlockPropellent extends BlockGasGeneric {
	
	public BlockPropellent() {
		super( 0, false, true, true );
	}
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerBlockIcons( IIconRegister ir ) {
		this.blockIcon = ir.registerIcon( "eng_toolbox:propellent" );
	}
}
