/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.data;

import dan200.billund.shared.item.ItemBrick;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BillundSet {

    public static BillundSet get(int index) {
        return new BillundSet(index);
    }

    private static String[] s_setNames = new String[]{
            "Starter Pack",
            "Color Pack A",
            "Color Pack B",
            "Color Pack C",
            "Color Pack D",
    };

    private static int[] s_setCosts = new int[]{
            7,
            10,
            10,
            10,
            10
    };

    private int m_index;

    public BillundSet(int index) {
        m_index = index;
    }

    public int getCost() {
        return s_setCosts[m_index];
    }

    public String getDescription() {
        return s_setNames[m_index];
    }

    private IInventory s_addInventory = null;
    private int s_addIndex = 0;

    public void populateChest(IInventory inv) {
        s_addIndex = 0;
        s_addInventory = inv;

        switch (m_index) {
            case 0: {
                // Starter set
                // Basic pieces in 6 colours
                addBasic(Color.RED);
                addBasic(Color.GREEN);
                addBasic(Color.BLUE);
                addBasic(Color.YELLOW);
                addBasic(Color.WHITE);
                addBasic(Color.BLACK);
                break;
            }
            case 1: {
                // color Pack
                // pieces in 3 colours
                addAll(Color.RED);
                addAll(Color.GREEN);
                addAll(Color.BLUE);
                break;
            }
            case 2: {
                // color Pack
                // pieces in 3 colours
                addAll(Color.ORANGE);
                addAll(Color.YELLOW);
                addAll(Color.LIME);
                break;
            }
            case 3: {
                // color Pack
                // pieces in 3 colours
                addAll(Color.PINK);
                addAll(Color.PURPLE);
                addAll(Color.WHITE);
                break;
            }
            case 4: {
                // color Pack
                // pieces in 3 colours
                addAll(Color.LIGHT_GRAY);
                addAll(Color.GRAY);
                addAll(Color.BLACK);
                break;
            }
        }
    }

    private void add(ItemStack stack) {
        int slot = s_addIndex++;
        if (slot < s_addInventory.getSizeInventory()) {
            s_addInventory.setInventorySlotContents(slot, stack);
        }
    }

    private void addBasic(Color colour) {
        add(ItemBrick.create(colour.getRGB(), 1, 2, 24));
        add(ItemBrick.create(colour.getRGB(), 1, 4, 24));
        add(ItemBrick.create(colour.getRGB(), 2, 2, 24));
        add(ItemBrick.create(colour.getRGB(), 2, 4, 24));
    }

    private void addAll(Color colour) {
        add(ItemBrick.create(colour.getRGB(), 1, 1, 24));
        add(ItemBrick.create(colour.getRGB(), 1, 2, 24));
        add(ItemBrick.create(colour.getRGB(), 1, 3, 24));
        add(ItemBrick.create(colour.getRGB(), 1, 4, 24));
        add(ItemBrick.create(colour.getRGB(), 1, 6, 24));
        add(ItemBrick.create(colour.getRGB(), 2, 2, 24));
        add(ItemBrick.create(colour.getRGB(), 2, 3, 24));
        add(ItemBrick.create(colour.getRGB(), 2, 4, 24));
        add(ItemBrick.create(colour.getRGB(), 2, 6, 24));
    }
}
