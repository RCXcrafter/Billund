/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.item;

import dan200.Billund;
import dan200.billund.shared.handler.GuiHandler;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;

public class ItemOrderForm extends Item {

    private static IIcon s_icon;

    public ItemOrderForm() {
        super();

        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName("billform");
        setCreativeTab(Billund.getCreativeTab());
    }

    public static ItemStack create(int colour, int width, int depth, int quantity) {
        int damage = ((width - 1) & 0x1) + (((depth - 1) & 0x7) << 1) + ((colour & 0xf) << 4);
        return new ItemStack(Billund.BillundItems.brick, quantity, damage);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        list.add(new ItemStack(Billund.BillundItems.orderForm, 1, 0));
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        player.openGui(Billund.instance, GuiHandler.GUI_ORDER_FORM, world, 0, 0, 0);
        return stack;
    }

    @Override
    public void registerIcons(IIconRegister iconRegister) {
        s_icon = iconRegister.registerIcon("billund:orderform");
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return s_icon;
    }
}
