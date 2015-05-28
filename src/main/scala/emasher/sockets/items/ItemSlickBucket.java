package emasher.sockets.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

public class ItemSlickBucket extends ItemBucket {
	
	public ItemSlickBucket() {
		super( SocketsMod.blockSlickwater );
		
		setCreativeTab( SocketsMod.tabSockets );
		setMaxStackSize( 1 );
		setUnlocalizedName( "slickwaterBucket" );
		this.setContainerItem( Items.bucket );
	}
	
	
	@Override
	@SideOnly( Side.CLIENT )
	public void registerIcons( IIconRegister ir ) {
		itemIcon = ir.registerIcon( "sockets:slickbucket" );
	}

}