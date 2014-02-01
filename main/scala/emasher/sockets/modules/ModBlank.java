package emasher.sockets.modules;

import emasher.api.SocketModule;

public class ModBlank extends SocketModule
{

	public ModBlank(int id)
	{
		super(id, "sockets:bg");
	}

	@Override
	public String getLocalizedName()
	{
		return "Blank";
	}

}
