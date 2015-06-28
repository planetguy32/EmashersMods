package emasher.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ModBlank extends SocketModule {

	public ModBlank( int id ) {
		super( id, "eng_toolbox:bg" );
	}

	@Override
	public String getLocalizedName() {
		return "Blank";
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		return "eng_toolbox:inner_circuit";
	}

	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"eng_toolbox:inner_circuit"};
	}

}
