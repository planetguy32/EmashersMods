package emasher.sockets.modules;

import emasher.api.SocketModule;

public class ModMultiBlock extends SocketModule {

	public ModMultiBlock( int id ) {
		super( id, "sockets:mbInterface" );
	}

	@Override
	public String getLocalizedName() {
		return "Multi-Block Interface";
	}
	
}
