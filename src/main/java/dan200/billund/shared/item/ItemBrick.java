/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.item;

import dan200.billund.Billund;
import dan200.billund.shared.core.BillundProxyCommon;
import dan200.billund.shared.data.Brick;
import dan200.billund.shared.data.Stud;
import dan200.billund.shared.data.StudColour;
import dan200.billund.shared.tile.TileEntityBillund;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.List;

public class ItemBrick extends Item {

    public ItemBrick() {
        super();

        setMaxStackSize(64);
        setHasSubtypes(true);
        setUnlocalizedName("billbrick");
        setCreativeTab(Billund.getCreativeTab());
    }

    public static ItemStack create(int colour, int width, int depth, int quantity) {
        int damage = ((width - 1) & 0x1) + (((depth - 1) & 0x7) << 1);
        ItemStack stack = new ItemStack(BillundItems.brick, quantity, damage);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("color", colour);
        stack.setTagCompound(nbt);
        return stack;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list) {
        for (int i = 0; i < ItemDye.field_150922_c.length; i++) {
            int colour = ItemDye.field_150922_c[i];
            list.add(create(colour, 1, 1, 1));
            list.add(create(colour, 1, 2, 1));
            list.add(create(colour, 1, 3, 1));
            list.add(create(colour, 1, 4, 1));
            list.add(create(colour, 1, 6, 1));
            list.add(create(colour, 2, 2, 1));
            list.add(create(colour, 2, 3, 1));
            list.add(create(colour, 2, 4, 1));
            list.add(create(colour, 2, 6, 1));
        }
    }

    public static TileEntityBillund.StudRaycastResult raycastFromPlayer(World world, EntityPlayer player, float f) {
        // Calculate the raycast origin and direction
        double yOffset2 = (!world.isRemote && player.isSneaking()) ? -0.08 : 0.0; // TODO: Improve
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double x = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double y = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62 - player.yOffset + yOffset2; // TODO: Improve
        double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        Vec3 position = Vec3.createVectorHelper(x, y, z);

        float f3 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-pitch * 0.017453292F);
        float f6 = MathHelper.sin(-pitch * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;

        float distance = 5.0f;
        if (player instanceof EntityPlayerMP) {
            distance = (float) ((EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        }
        Vec3 direction = Vec3.createVectorHelper((double) f7, (double) f6, (double) f8);

        // Do the raycast
        return TileEntityBillund.raycastStuds(world, position, direction, distance);
    }

    public static Brick getPotentialBrick(ItemStack stack, World world, EntityPlayer player, float f) {
        // Do the raycast
        TileEntityBillund.StudRaycastResult result = raycastFromPlayer(world, player, f);
        if (result != null) {
            // Calculate where to place the brick
            int defaultWidth = getWidth(stack);
            int defaultDepth = getDepth(stack);
            int width = 0;
            int depth = 0;
            int height = 1;

            int placeX = result.hitX;
            int placeY = result.hitY;
            int placeZ = result.hitZ;

            switch (BillundProxyCommon.rotate) {
                case 0:
                default:
                    depth = defaultDepth;
                    width = defaultWidth;
                    break;
                case 1:
                    depth = defaultWidth;
                    width = defaultDepth;
                    break;
                case 2:
                    depth = defaultDepth;
                    width = defaultWidth;
                    placeZ -= depth - 1;
                    break;
                case 3:
                    depth = defaultWidth;
                    width = defaultDepth;
                    placeX -= width - 1;
                    break;
            }

            switch (result.hitSide) {
                case 0:
                    placeY -= height;
                    break;
                case 1:
                    placeY++;
                    break;
                case 2:
                    placeZ -= depth;
                    break;
                case 3:
                    placeZ++;
                    break;
                case 4:
                    placeX -= width;
                    break;
                case 5:
                    placeX++;
                    break;
            }

            // Try a few positions nearby
            Brick brick = new Brick(getColour(stack), placeX, placeY, placeZ, width, height, depth);
            for (int x = 0; x < width; ++x) {
                for (int z = 0; z < depth; ++z) {
                    for (int y = 0; y < height; ++y) {
                        brick.XOrigin = placeX - x;
                        brick.YOrigin = placeY - y;
                        brick.ZOrigin = placeZ - z;
                        if (TileEntityBillund.canAddBrick(world, brick)) {
                            return brick;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Brick getExistingBrick(World world, EntityPlayer player, float f) {
        // Do the raycast
        TileEntityBillund.StudRaycastResult result = raycastFromPlayer(world, player, f);
        if (result != null) {
            Stud stud = TileEntityBillund.getStud(world, result.hitX, result.hitY, result.hitZ);
            if (stud != null && stud.Colour != StudColour.Wall) {
                return new Brick(stud.Colour, stud.XOrigin, stud.YOrigin, stud.ZOrigin, stud.BrickWidth, stud.BrickHeight, stud.BrickDepth);
            }
        }
        return null;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//    	Brick brick = getPotentialBrick( stack, world, player, 1.0f );
//    	if( brick != null )
//		{
//			return true;
//		}
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
//    	Brick brick = getPotentialBrick( stack, world, player, 1.0f );
//    	if( brick != null )
//		{
//			return true;
//		}
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        Brick brick = getPotentialBrick(stack, world, player, 1.0f);
        if (brick != null) {
            if (!world.isRemote) {
                // Place the brick
                TileEntityBillund.addBrick(world, brick);

                if (!player.capabilities.isCreativeMode) {
                    // Decrement stackSize
                    stack.stackSize--;
                }
            }
        }
        return stack;
    }

    public static int getWidth(ItemStack stack) {
        int damage = stack.getItemDamage();
        return (damage & 0x1) + 1;
    }

    public static int getHeight(ItemStack stack) {
        return 1;
    }

    public static int getDepth(ItemStack stack) {
        int damage = stack.getItemDamage();
        return ((damage >> 1) & 0x7) + 1;
    }

    public static int getColour(ItemStack stack) {
        return stack.hasTagCompound() ? stack.getTagCompound().getInteger("color") : 0;
    }
}
