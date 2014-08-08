/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.core;

import dan200.Billund;
import dan200.billund.shared.item.ItemBrick;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabBillund extends CreativeTabs {

    public CreativeTabBillund(int p1, String p2) {
        super(p1, p2);
    }

    @Override
    public Item getTabIconItem() {
        return Billund.BillundItems.brick;
    }

    @Override
    public ItemStack getIconItemStack() {
        return ItemBrick.create(11743532, 2, 2, 1);
    }

    @Override
    public String getTranslatedTabLabel() {
        return getTabLabel();
    }
}
