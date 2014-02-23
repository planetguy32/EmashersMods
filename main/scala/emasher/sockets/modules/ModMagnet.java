package emasher.sockets.modules;

import emasher.api.SocketModule;

public class ModMagnet extends SocketModule
{

    public ModMagnet(int id)
    {
        super(id, "sockets:magnet");
    }

    @Override
    public String getLocalizedName()
    {
        return "Magnet";
    }

    @Override
    public boolean hasRSIndicator() { return true; }

    @Override
    public boolean hasLatchIndicator() { return true; }
}
