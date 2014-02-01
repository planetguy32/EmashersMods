package emasher.sockets.modules;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;
import emasher.api.RSPulseModule;
import emasher.api.SideConfig;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.PacketHandler;
import emasher.sockets.SocketsMod;
import emasher.sockets.TileSocket;

public class ModFluidDetector extends RSPulseModule
{

	public ModFluidDetector(int id)
	{
		super(id, "sockets:DETLiquid_0", "sockets:DETLiquid_1");
	}

	@Override
	public String getLocalizedName()
	{
		return "Fluid Detector";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Creates an internal redstone pulse");
		l.add("when fluid is added to its configured tank");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_BLUE + "Tank to watch");
		l.add(SocketsMod.PREF_RED + "RS control channel to pulse");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " h ", "iui", " b ", Character.valueOf('i'), Item.ingotIron, Character.valueOf('h'), Block.pressurePlateStone,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('b'), SocketsMod.blankSide);
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " h ", "iui", " b ", Character.valueOf('i'), "ingotAluminum", Character.valueOf('h'), Block.pressurePlateStone,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('b'), SocketsMod.blankSide));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), " h ", "iui", " b ", Character.valueOf('i'), "ingotTin", Character.valueOf('h'), Block.pressurePlateStone,
				Character.valueOf('u'), Item.bucketEmpty, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasTankIndicator() { return true; }
	
	@Override
	public void onTankChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean add)
	{
		if(add && (index == config.tank || config.tank == -1))
		{
			config.meta = 1;
			
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					ts.modifyRS(i, true);
				}
			}
			
			ts.updateAdj(side);
			ts.sendClientSideState(side.ordinal());
			//PacketHandler.instance.SendClientSideState(ts, (byte)side.ordinal());
		}
	}

}
