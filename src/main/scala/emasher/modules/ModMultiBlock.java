package emasher.modules;

import emasher.api.SocketModule;

public class ModMultiBlock extends SocketModule {

	public ModMultiBlock( int id ) {
		super( id, "eng_toolbox:mbInterface" );
	}

	@Override
	public String getLocalizedName() {
		return "Multi-Block Interface";
	}
	
}
