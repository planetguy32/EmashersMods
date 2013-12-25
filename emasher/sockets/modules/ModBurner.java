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
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModBurner extends SocketModule
{

	public ModBurner(int id)
	{
		super(id, "sockets:burner");
	}

	@Override
	public String getLocalizedName()
	{
		return "Burner";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Creates fire with a sustained");
		l.add("internal redstone signal");
		l.add("Can be used to control Nether portals");
	}
	
	@Override
	public void getIndicatorKey(List l)
	{
		l.add(SocketsMod.PREF_RED + "RS control circuit");
		l.add(SocketsMod.PREF_DARK_PURPLE + "RS control latch");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ini", "ifi", " b ", Character.valueOf('i'), Item.ingotIron, Character.valueOf('f'), Item.flintAndSteel, Character.valueOf('n'),
				Block.netherrack, Character.valueOf('b'), SocketsMod.blankSide);
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ini", "ifi", " b ", Character.valueOf('i'), "ingotNickel", Character.valueOf('f'), Item.flintAndSteel, Character.valueOf('n'),
				Block.netherrack, Character.valueOf('b'), SocketsMod.blankSide));
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "ini", "ifi", " b ", Character.valueOf('i'), "ingotSteel", Character.valueOf('f'), Item.flintAndSteel, Character.valueOf('n'),
				Block.netherrack, Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public void init(SocketTileAccess ts, SideConfig config, ForgeDirection side) { updateFire(ts, config, side); }
	
	@Override
	public void indicatorUpdated(SocketTileAccess ts, SideConfig config, ForgeDirection side) { updateFire(ts, config, side); }
	
	@Override
	public void onAdjChange(SocketTileAccess ts, SideConfig config, ForgeDirection side) { updateFire(ts, config, side); }
	
	@Override
	public void onRSInterfaceChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on) { updateFire(ts, config, side); }
	
	@Override
	public void onRSLatchChange(SideConfig config, int index, SocketTileAccess ts, ForgeDirection side, boolean on) { updateFire(ts, config, side); }
	
	public void updateFire(SocketTileAccess ts, SideConfig config, ForgeDirection side)
	{
		boolean canBurn = false;
		
		for(int i = 0; i < 3; i++)
		{
			if(config.rsControl[i] && ts.getRSControl(i)) canBurn = true;
			if(config.rsLatch[i] && ts.getRSLatch(i)) canBurn = true;
		}
		
		int xo = ts.xCoord + side.offsetX;
		int yo = ts.yCoord + side.offsetY;
		int zo = ts.zCoord + side.offsetZ;
		
		int blockID = ts.worldObj.getBlockId(xo, yo, zo);
		
		if(blockID == Block.fire.blockID && ! canBurn) ts.worldObj.setBlockToAir(xo, yo, zo);
		else if(ts.worldObj.isAirBlock(xo, yo, zo) && canBurn) ts.worldObj.setBlock(xo, yo, zo, Block.fire.blockID);
		else if(blockID == Block.portal.blockID && ! canBurn) ts.worldObj.setBlockToAir(xo, yo, zo);
	}
	
}
