package dan200.billund.shared.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author dmillerw
 */
public class EmeraldHelper {

    public static int getPlayerBalance(EntityPlayer player) {
        int total = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.emerald) {
                total += stack.stackSize;
            }
        }
        return total;
    }

    public static boolean removeEmeralds(EntityPlayer player, int cost) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }

        // Find enough emeralds
        int emeralds = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.emerald) {
                emeralds += stack.stackSize;
                if (emeralds >= cost) {
                    break;
                }
            }
        }

        if (emeralds >= cost) {
            // Then expend them
            emeralds = cost;
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.emerald) {
                    if (stack.stackSize <= emeralds) {
                        player.inventory.setInventorySlotContents(i, null);
                        emeralds -= stack.stackSize;
                    } else {
                        stack.stackSize -= emeralds;
                        emeralds = 0;
                    }
                    if (emeralds == 0) {
                        break;
                    }
                }
            }
            player.inventory.markDirty();
            return true;
        }
        return false;
    }
}
