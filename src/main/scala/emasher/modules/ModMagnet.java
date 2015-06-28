package emasher.modules;

import emasher.api.SocketModule;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

public class ModMagnet extends SocketModule {

	public ModMagnet( int id, String texture ) {
		super( id, texture );
	}

	@Override
	public String getLocalizedName() {
		return "Magnet";
	}

	@Override
	public void getToolTip( List l ) {
		l.add( "Pushes or pulls adjacent blocks when the socket moves" );
	}

	@Override
	public void getIndicatorKey( List l ) {
		l.add( emasher.util.Config.PREF_RED() + "Enabled" );
		l.add( emasher.util.Config.PREF_DARK_PURPLE() + "Enabled" );
	}

	@Override
	public void addRecipe() {
		CraftingManager.getInstance().getRecipeList().add( new ShapedOreRecipe( new ItemStack( emasher.items.Items.module(), 1, moduleID ), "i", "b",
				Character.valueOf( 'i' ), Blocks.iron_block,
				Character.valueOf( 'b' ), emasher.items.Items.blankSide() ) );
	}

	@Override
	public boolean hasRSIndicator() {
		return true;
	}

	@Override
	public boolean hasLatchIndicator() {
		return true;
	}
}
