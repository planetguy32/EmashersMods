package emasher.sockets.modules;

import emasher.api.PhotobioReactorRecipeRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.sockets.SocketsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModStirlingGenerator extends SocketModule {

	private static final int ACTIVE = 1;
	private static final int HIGH_POWER = 2;

	public ModStirlingGenerator(int id)
	{
		super(id, "sockets:stirlingGenIdle0", "sockets:stirlingGenActive0",
				  "sockets:stirlingGenIdle1", "sockets:stirlingGenActive1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Stirling Generator";
	}

	@Override
	public void getToolTip(List l)
	{
		l.add("Generates power when fueled");
		l.add("with burnable items");
	}

	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_GREEN + "Input inventory");
	}

	@Override
	public void addRecipe()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID),
				"g g",
				" F ",
				" b ",
				Character.valueOf('g'), Block.stone,
				Character.valueOf('F'), Block.furnaceIdle,
				Character.valueOf('b'), SocketsMod.blankSide));
	}

	@Override
	public boolean hasTankIndicator() { return false; }

	@Override
	public boolean hasInventoryIndicator() { return true; }

	@Override
	public boolean isMachine() { return false; }

	@Override
	public boolean canBeInstalled(SocketTileAccess ts, ForgeDirection side)
	{
		for(int i = 0; i < 6; i++)
		{
			SocketModule m = ts.getSide(ForgeDirection.getOrientation(i));
			if(m != null && m.isMachine()) return false;
		}

		return true;
	}

	@Override
	public void onGenericRemoteSignal(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		config.tank ^= HIGH_POWER;
		ts.sendClientSideState(side.ordinal());
	}

	@Override
	public void updateSide(SideConfig config, SocketTileAccess ts, ForgeDirection side)
	{
		boolean updateClient = false;
		if(config.inventory >= 0 && config.inventory <= 2)
		{
			boolean roomForMoreFuel = ts.sideInventory.getStackInSlot(side.ordinal()) == null;
			boolean moreFuelAvail = ts.getStackInInventorySlot(config.inventory) != null;
			boolean canStoreEnergy = ts.powerHandler.getEnergyStored() < ts.powerHandler.getMaxEnergyStored();

			if(moreFuelAvail && roomForMoreFuel && canStoreEnergy && config.meta <= 0)
			{
				ItemStack toIntake = ts.getStackInInventorySlot(config.inventory);

				int time = TileEntityFurnace.getItemBurnTime(toIntake);
				if( time > 0 )
				{
					// ensure its even
					time = (time * 2) / 2;
					ts.extractItemInternal(true, config.inventory, 1);
					config.meta = time;
				}
			}

			if(canStoreEnergy && config.meta > 0)
			{
				if((config.tank & HIGH_POWER) != HIGH_POWER)
				{
					config.meta--;
					ts.powerHandler.addEnergy(1.0f);
				}
				else
				{
					config.meta -= 2;
					ts.powerHandler.addEnergy(2.0f);
				}
				if(config.meta == 0)
				{
					config.tank &= ~ACTIVE;
					updateClient = true;
				}
				else
				{
					if((config.tank & ACTIVE) != ACTIVE)
						updateClient = true;
					config.tank |= ACTIVE;
				}
			}
			else if(!canStoreEnergy && config.meta > 0)
			{
				config.tank &= ~ACTIVE;
				updateClient = true;
			}
		}
		if(updateClient) ts.sendClientSideState(side.ordinal());
	}

	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		// not ACTIVE and not HIGH_POWER
		config.tank = 0;
	}

	@Override
	public int getCurrentTexture(SideConfig config)
	{
		if((config.tank & ACTIVE) == ACTIVE)
		{
			if((config.tank & HIGH_POWER) == HIGH_POWER)
				return 3;
			return 1;
		}
		else
		{
			if((config.tank & HIGH_POWER) == HIGH_POWER)
				return 2;
			return 0;
		}
	}
}
