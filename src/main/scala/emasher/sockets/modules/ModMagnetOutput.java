package emasher.sockets.modules;

import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class ModMagnetOutput extends ModMagnet
{
    public ModMagnetOutput(int id)
    {
        super(id, "sockets:magnetOutput");
    }

    @Override
    public String getLocalizedName()
    {
        return "Magnet Output";
    }

    @Override
    public void getToolTip(List l)
    {
        l.add("Combined Magnet, Fluid, Item, and Energy output modules");
    }

    @Override
    public void getIndicatorKey(List l)
    {
        l.add(SocketsMod.PREF_BLUE + "Tank to output from");
        l.add(SocketsMod.PREF_GREEN + "Inventory to output from");
        l.add(SocketsMod.PREF_RED + "Magnet Enabled");
        l.add(SocketsMod.PREF_DARK_PURPLE + "Magnet Enabled");
    }

    @Override
    public void addRecipe()
    {
        GameRegistry.addShapelessRecipe(new ItemStack(SocketsMod.module, 1, moduleID), new ItemStack(SocketsMod.module, 1, 10), new ItemStack(SocketsMod.module, 1, 47));
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
    public boolean isFluidInterface() { return true; }

    @Override
    public boolean canExtractFluid() { return true; }

    @Override
    public boolean isItemInterface() { return true; }

    @Override
    public boolean canExtractItems() { return true; }

    @Override
    public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
    {
        EnergyInsert(ts, config, side);
        FluidInsert(ts, config, side);
        ItemInsert(ts, config, side);
    }

    private void EnergyInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
    {
        ts.outputEnergy(1000, side);
    }

    private void FluidInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
    {
        if(config.tank < 0 || config.tank > 2) return;
        ts.tryInsertFluid(config.tank, side);
    }

    private void ItemInsert(SocketTileAccess ts, SideConfig config, ForgeDirection side)
    {
        if(config.inventory < 0 || config.inventory > 2) return;
        if(ts.tryInsertItem(ts.getStackInInventorySlot(config.inventory), side)) ts.extractItemInternal(true, config.inventory, 1);
    }

    @Override
    public FluidStack fluidExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts)
    {
        if(config.tank != -1) return ts.drainInternal(config.tank, amount, doExtract);
        return null;
    }

    @Override
    public ItemStack itemExtract(int amount, boolean doExtract, SideConfig config, SocketTileAccess ts)
    {
        if(config.inventory != -1) return ts.extractItemInternal(doExtract, config.inventory, amount);
        return null;
    }

    @Override
    public int extractEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
    {
        return ts.useEnergy(amount, simulate);
    }

    @Override
    public boolean canDirectlyExtractItems(SideConfig config, SocketTileAccess ts)
    {
        if(config.inventory < 0 || config.inventory > 2) return false;

        return true;
    }
}
