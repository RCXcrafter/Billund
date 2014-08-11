/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.data;

import net.minecraft.item.ItemDye;

public enum Color {

    BLACK,
    RED,
    GREEN,
    BROWN,
    BLUE,
    PURPLE,
    CYAN,
    LIGHT_GRAY,
    GRAY,
    PINK,
    LIME,
    YELLOW,
    LIGHT_BLUE,
    MAGENTA,
    ORANGE,
    WHITE;

    public int getRGB() {
        return ItemDye.field_150922_c[ordinal()];
    }
}
