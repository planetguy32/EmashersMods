package emasher.sockets.modules;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class ModBlank extends SocketModule {

	public ModBlank( int id ) {
		super( id, "sockets:bg" );
	}

	@Override
	public String getLocalizedName() {
		return "Blank";
	}

	@SideOnly( Side.CLIENT )
	public String getInternalTexture( SocketTileAccess ts, SideConfig config, ForgeDirection side ) {
		return "sockets:inner_circuit";
	}

	@SideOnly( Side.CLIENT )
	public String[] getAllInternalTextures() {
		return new String[] {"sockets:inner_circuit"};
	}

}
