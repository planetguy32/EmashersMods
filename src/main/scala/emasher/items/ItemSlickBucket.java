package emasher.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.EngineersToolbox;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemSlickBucket extends ItemBucket {
	
	public ItemSlickBucket() {
		super( emasher.blocks.Blocks.blockSlickwater() );
		
		setCreativeTab( EngineersToolbox.tabItems() );
		setMaxStackSize( 1 );
		setUnlocalizedName( "slickwaterBucket" );
		this.setContainerItem( Items.bucket );
	}
	
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister ir ) {
		itemIcon = ir.registerIcon( "eng_toolbox:slickbucket" );
	}

}