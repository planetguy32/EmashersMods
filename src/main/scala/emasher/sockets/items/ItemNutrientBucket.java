package emasher.sockets.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import emasher.core.EmasherCore;
import emasher.sockets.SocketsMod;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;

public class ItemNutrientBucket extends ItemBucket
{
    public ItemNutrientBucket(int id)
    {
        super(id, EmasherCore.nutrientWaterFluid.getBlockID());

        setCreativeTab(SocketsMod.tabSockets);
        setMaxStackSize(1);
        setUnlocalizedName("nutWaterBucket");
        this.setContainerItem(Item.bucketEmpty);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister ir)
    {
        itemIcon = ir.registerIcon("sockets:nutBucket");
    }
}
