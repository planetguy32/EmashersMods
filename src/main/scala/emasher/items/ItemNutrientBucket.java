package emasher.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemNutrientBucket extends ItemBucket {
	public ItemNutrientBucket() {
		super( emasher.blocks.Blocks.nutrientWater() );

		setCreativeTab( EngineersToolbox.tabItems() );
		setMaxStackSize( 1 );
		setUnlocalizedName( "nutWaterBucket" );
		this.setContainerItem( Items.bucket );
	}


	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister ir ) {
		itemIcon = ir.registerIcon( "eng_toolbox:nutBucket" );
	}
}
