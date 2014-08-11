package dan200.billund.shared.recipe;

import dan200.billund.shared.item.BillundItems;
import dan200.billund.shared.item.ItemBrick;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class RecipeBrick implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        int bricks = 0;
        int components = 0;
        for (int i=0; i<inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == BillundItems.brick) {
                    bricks++;
                } else if (stack.getItem() == Items.dye || stack.getItem() == Items.glowstone_dust || Block.getBlockFromItem(stack.getItem()) == Blocks.glass) {
                    components++;
                }
            }
        }
        return bricks == 1 && components == 1;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack brick = null;
        ItemStack component = null;
        boolean dye = false;
        boolean light = false;
        boolean glass = false;
        for (int i=0; i<inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == BillundItems.brick) {
                    brick = stack.copy();
                } else if (stack.getItem() == Items.dye) {
                    component = stack.copy();
                    dye = true;
                } else if (stack.getItem() == Items.glowstone_dust) {
                    component = stack.copy();
                    light = true;
                } else if (Block.getBlockFromItem(stack.getItem()) == Blocks.glass) {
                    component = stack.copy();
                    glass = true;
                }
            }
        }
        if (dye) {
            return ItemBrick.create(
                    ItemBrick.getIlluminated(brick),
                    ItemBrick.getTransparent(brick),
                    ItemDye.field_150922_c[component.getItemDamage()],
                    ItemBrick.getWidth(brick),
                    ItemBrick.getDepth(brick),
                    1
            );
        } else if (light) {
            return ItemBrick.create(
                    true,
                    ItemBrick.getTransparent(brick),
                    ItemBrick.getColour(brick),
                    ItemBrick.getWidth(brick),
                    ItemBrick.getDepth(brick),
                    1
            );
        } else if (glass) {
            return ItemBrick.create(
                    ItemBrick.getIlluminated(brick),
                    true,
                    ItemBrick.getColour(brick),
                    ItemBrick.getWidth(brick),
                    ItemBrick.getDepth(brick),
                    1
            );
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(BillundItems.brick);
    }
}
