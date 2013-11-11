package emasher.sockets.modules;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.registry.GameRegistry;
import emasher.api.SideConfig;
import emasher.api.SocketModule;
import emasher.api.SocketTileAccess;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;

public class ModEnergyInput extends SocketModule
{

	public ModEnergyInput(int id)
	{
		super(id, "sockets:energyInput");
	}

	@Override
	public String getLocalizedName()
	{
		return "Energy Input";
	}
	
	@Override
	public void getToolTip(List l)
	{
		l.add("Accepts both MJ and EU");
		l.add("from adjacent cables/generators/etc.");
	}
	
	@Override
	public void addRecipe()
	{
		GameRegistry.addShapedRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "g g", " b ", Character.valueOf('g'), Item.goldNugget, Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide);
		
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(SocketsMod.module, 1, moduleID), "g g", " b ", Character.valueOf('g'), "ingotCopper", Character.valueOf('p'), EmasherCore.psu,
				Character.valueOf('b'), SocketsMod.blankSide));
	}
	
	
	@Override
	public boolean hasRSIndicator() { return true; }
	
	@Override
	public boolean hasLatchIndicator() { return true; }
	
	@Override
	public boolean isEnergyInterface(SideConfig config) { return true; }
	
	@Override
	public int receiveEnergy(int amount, boolean simulate, SideConfig config, SocketTileAccess ts)
	{
		boolean allOff = true;
		
			for(int i = 0; i < 3; i++)
			{
				if(config.rsControl[i])
				{
					if(ts.getRSControl(i))
					{
						return ts.addEnergy(amount, simulate);
					}
					allOff = false;
				}
				
				if(config.rsLatch[i])
				{
					if(ts.getRSLatch(i))
					{
						return ts.addEnergy(amount, simulate);
					}
					allOff = false;
				}
			}
			
			if(allOff)
			{
				return ts.addEnergy(amount, simulate);
				
			}
		
		return 0;
	}
	
	

}
