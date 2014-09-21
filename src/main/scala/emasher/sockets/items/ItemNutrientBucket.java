package emasher.sockets.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;

public class ItemNutrientBucket extends ItemBucket
{
    public ItemNutrientBucket(int id)
    {
        super(EmasherCore.nutrientWater);

        setCreativeTab(SocketsMod.tabSockets);
        setMaxStackSize(1);
        setUnlocalizedName("nutWaterBucket");
        this.setContainerItem(Items.bucket);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister ir)
    {
        itemIcon = ir.registerIcon("sockets:nutBucket");
    }
}
