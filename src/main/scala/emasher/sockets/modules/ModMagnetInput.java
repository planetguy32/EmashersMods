package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ModMagnetInput extends ModMagnet
{
    public ModMagnetInput(int id)
    {
        super(id, "sockets:magnetInput");
    }

    @Override
    public String getLocalizedName()
    {
        return "Magnet Input";
    }

    @Override
    public void getToolTip(List l)
    {
        l.add("Combined Magnet, Fluid, Item, and Energy input modules");
    }

    @Override
    public void getIndicatorKey(List l)
    {
        l.add(SocketsMod.PREF_BLUE + "Tank to input to");
        l.add(SocketsMod.PREF_GREEN + "Inventory to input to");
        l.add(SocketsMod.PREF_RED + "Magnet Enabled");
        l.add(SocketsMod.PREF_DARK_PURPLE + "Magnet Enabled");
    }

    @Override
    public void addRecipe()
    {
        GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new ItemStack(SocketsMod.module, 1, 9), new ItemStack(SocketsMod.module, 1, 47));
    }

    @Override
    public boolean hasTankIndicator() { return true; }

    @Override
    public boolean hasInventoryIndicator() { return true; }

    @Override
    public boolean hasRSIndicator() { return true; }

    @Override
    public boolean hasLatchIndicator() { return true; }

    @Override
    public boolean isEnergyInterface(SideConfig config) { return true; }

    @Override
    public int receiveEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
    {
        return ts.addEnergy(amount, simulate);
    }

    @Override
    public boolean isFluidInterface() { return true; }

    @Override
    public boolean canInsertFluid() { return true; }


    @Override
    public int fluidFill(FluidStack fluid, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side)
    {
        if(config.tank != -1) return ts.fillInternal(config.tank, fluid, doFill);
        return 0;
    }

    @Override
    public boolean isItemInterface() { return true; }

    @Override
    public boolean canInsertItems() { return true; }

    @Override
    public boolean canDirectlyInsertItems(SideConfig config, SocketTileAccess ts)
    {
        if(config.inventory < 0 || config.inventory > 2) return false;

        return true;
    }

    @Override
    public int itemFill(ItemStack item, boolean doFill, SideConfig config, SocketTileAccess ts, ForgeDirection side)
    {
        if(config.inventory != -1) return ts.addItemInternal(item, doFill, config.inventory);
        return 0;
    }
}
