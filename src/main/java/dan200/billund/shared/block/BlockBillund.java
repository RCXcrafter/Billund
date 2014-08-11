/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.block;

import dan200.billund.shared.data.Brick;
import dan200.billund.shared.data.Stud;
import dan200.billund.shared.item.ItemBrick;
import dan200.billund.shared.tile.TileEntityBillund;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class BlockBillund extends BlockContainer {

    private static IIcon s_transparentIcon;

    private static Brick s_hoverBrick = null;

    public static void setHoverBrick(Brick brick) {
        s_hoverBrick = brick;
    }

    public static int blockRenderID;

    public BlockBillund() {
        super(Material.wood);
        setHardness(0.25f);
        setBlockName("billund");

        // These get replaced on the client
        blockRenderID = -1;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
        return false;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
        if (!world.isRemote) {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
            if (tileEntity != null && tileEntity instanceof TileEntityBillund) {
                // Find a brick to destroy
                TileEntityBillund billund = (TileEntityBillund) tileEntity;
                Brick brick = ItemBrick.getExistingBrick(world, player, 1.0f);
                if (brick != null) {
                    // Remove the brick
                    TileEntityBillund.removeBrick(world, brick);

                    // Spawn an item for the destroyed brick
                    if (!player.capabilities.isCreativeMode) {
                        float brickX = ((float) brick.xOrigin + (float) brick.width * 0.5f) / (float) TileEntityBillund.ROWS_PER_BLOCK;
                        float brickY = ((float) brick.yOrigin + (float) brick.height) / (float) TileEntityBillund.LAYERS_PER_BLOCK;
                        float brickZ = ((float) brick.zOrigin + (float) brick.depth * 0.5f) / (float) TileEntityBillund.ROWS_PER_BLOCK;
                        ItemStack stack = ItemBrick.create(brick.illuminated, brick.transparent, brick.smooth, brick.color, Math.min(brick.width, brick.depth), Math.max(brick.width, brick.depth), 1);
                        EntityItem entityitem = new EntityItem(world, brickX, brickY + 0.05f, brickZ, stack);
                        entityitem.motionX = 0.0f;
                        entityitem.motionY = 0.0f;
                        entityitem.motionZ = 0.0f;
                        entityitem.delayBeforeCanPickup = 30;
                        world.spawnEntityInWorld(entityitem);
                    }

                    // Clear the block
                    if (billund.isEmpty()) {
                        world.setBlockToAir(x, y, z);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund) {
            TileEntityBillund billund = (TileEntityBillund) tileEntity;
            return billund.globalIllumination ? 15 : 0;
        }
        return 0;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, Block block) {
        TileEntity tileEntity = world.getTileEntity(i, j, k);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund) {
            TileEntityBillund billund = (TileEntityBillund) tileEntity;
            billund.cullOrphans();
            if (billund.isEmpty()) {
                world.setBlockToAir(i, j, k);
            }
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k) {
        if (s_hoverBrick != null) {
            // See if the hovered brick is in the start bit
            int sx = s_hoverBrick.xOrigin;
            int sy = s_hoverBrick.yOrigin;
            int sz = s_hoverBrick.zOrigin;
            {
                int localX = (sx % TileEntityBillund.ROWS_PER_BLOCK + TileEntityBillund.ROWS_PER_BLOCK) % TileEntityBillund.ROWS_PER_BLOCK;
                int localY = (sy % TileEntityBillund.LAYERS_PER_BLOCK + TileEntityBillund.LAYERS_PER_BLOCK) % TileEntityBillund.LAYERS_PER_BLOCK;
                int localZ = (sz % TileEntityBillund.ROWS_PER_BLOCK + TileEntityBillund.ROWS_PER_BLOCK) % TileEntityBillund.ROWS_PER_BLOCK;
                int blockX = (sx - localX) / TileEntityBillund.ROWS_PER_BLOCK;
                int blockY = (sy - localY) / TileEntityBillund.LAYERS_PER_BLOCK;
                int blockZ = (sz - localZ) / TileEntityBillund.ROWS_PER_BLOCK;

                if ((i == blockX || i == blockX + 1) &&
                        (j == blockY || j == blockY + 1) &&
                        (k == blockZ || k == blockZ + 1)) {
                    float xScale = 1.0f / (float) TileEntityBillund.ROWS_PER_BLOCK;
                    float yScale = 1.0f / (float) TileEntityBillund.LAYERS_PER_BLOCK;
                    float zScale = 1.0f / (float) TileEntityBillund.ROWS_PER_BLOCK;

                    float startX = (float) (sx - (i * TileEntityBillund.ROWS_PER_BLOCK)) * xScale;
                    float startY = (float) (sy - (j * TileEntityBillund.LAYERS_PER_BLOCK)) * yScale;
                    float startZ = (float) (sz - (k * TileEntityBillund.ROWS_PER_BLOCK)) * zScale;
                    this.setBlockBounds(
                            startX, startY, startZ,
                            startX + (float) s_hoverBrick.width * xScale,
                            startY + (float) s_hoverBrick.height * yScale,
                            startZ + (float) s_hoverBrick.depth * zScale
                    );
                    return;
                }
            }
        }

        // Set bounds to something that should hopefully never be hit
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getBoundingBox((double) par2 + this.minX, (double) par3 + this.minY, (double) par4 + this.minZ, (double) par2 + this.maxX, (double) par3 + this.maxY, (double) par4 + this.maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getBoundingBox((double) par2 + this.minX, (double) par3 + this.minY, (double) par4 + this.minZ, (double) par2 + this.maxX, (double) par3 + this.maxY, (double) par4 + this.maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB bigBox, List list, Entity entity) {
        TileEntity tileEntity = world.getTileEntity(i, j, k);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund) {
            double originX = (double) i;
            double originY = (double) j;
            double originZ = (double) k;
            double stepX = 1.0 / (double) TileEntityBillund.STUDS_PER_ROW;
            double stepY = 1.0 / (double) TileEntityBillund.STUDS_PER_COLUMN;
            double stepZ = 1.0 / (double) TileEntityBillund.STUDS_PER_ROW;

            int minsx = i * TileEntityBillund.STUDS_PER_ROW;
            int minsy = j * TileEntityBillund.STUDS_PER_COLUMN;
            int minsz = k * TileEntityBillund.STUDS_PER_ROW;

            TileEntityBillund billund = (TileEntityBillund) tileEntity;
            for (int x = 0; x < TileEntityBillund.STUDS_PER_ROW; ++x) {
                for (int y = 0; y < TileEntityBillund.STUDS_PER_COLUMN; ++y) {
                    for (int z = 0; z < TileEntityBillund.STUDS_PER_ROW; ++z) {
                        Stud stud = billund.getStudLocal(x, y, z);
                        if (stud != null) {
                            double startX = originX + (double) x * stepX;
                            double startY = originY + (double) y * stepY;
                            double startZ = originZ + (double) z * stepZ;
                            if (stud.xOrigin < minsx || stud.yOrigin < minsy || stud.zOrigin < minsz) {
                                // If the origin of this brick is in a different block, add our own aabbs for each stud
                                AxisAlignedBB littleBox = AxisAlignedBB.getBoundingBox(
                                        startX, startY, startZ,
                                        startX + stepX,
                                        startY + stepY,
                                        startZ + stepZ
                                );
                                if (littleBox.intersectsWith(bigBox)) {
                                    list.add(littleBox);
                                }
                            } else {
                                // Else, if this stud *is* the origin, add an aabb for the whole thing
                                int sx = x + minsx;
                                int sy = y + minsy;
                                int sz = z + minsz;
                                if (sx == stud.xOrigin && sy == stud.yOrigin && sz == stud.zOrigin) {
                                    AxisAlignedBB littleBox = AxisAlignedBB.getBoundingBox(
                                            startX, startY, startZ,
                                            startX + (double) stud.brickWidth * stepX,
                                            startY + (double) stud.brickHeight * stepY,
                                            startZ + (double) stud.brickDepth * stepZ
                                    );
                                    if (littleBox.intersectsWith(bigBox)) {
                                        list.add(littleBox);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
        return ItemBrick.create(s_hoverBrick.illuminated, s_hoverBrick.transparent, s_hoverBrick.smooth, s_hoverBrick.color, s_hoverBrick.width, s_hoverBrick.depth, 1);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int i, int j, int k, int side) {
        return s_transparentIcon;
    }

    @Override
    public IIcon getIcon(int side, int damage) {
        return s_transparentIcon;
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int l, float m, float n, float o) {
        if (!world.isRemote) {
            if (s_hoverBrick != null) {
                ItemStack held = entityplayer.getHeldItem();

                if (held != null && held.getItem() instanceof ItemDye) {
                    Brick newBrick = new Brick(s_hoverBrick.illuminated, s_hoverBrick.transparent, s_hoverBrick.smooth, s_hoverBrick.color, s_hoverBrick.xOrigin, s_hoverBrick.yOrigin, s_hoverBrick.zOrigin, s_hoverBrick.width, s_hoverBrick.height, s_hoverBrick.depth);
                    Color brickColor = new Color(newBrick.color);
                    Color dyeColor = new Color(ItemDye.field_150922_c[held.getItemDamage()]);
                    int br = brickColor.getRed();
                    int bg = brickColor.getGreen();
                    int bb = brickColor.getBlue();
                    int dr = dyeColor.getRed();
                    int dg = dyeColor.getGreen();
                    int db = dyeColor.getBlue();
                    int ar = (br + dr) / 2;
                    int ag = (bg + dg) / 2;
                    int ab = (bb + db) / 2;
                    newBrick.color = new Color(ar, ag, ab).getRGB();
                    TileEntityBillund.addBrick(world, newBrick);
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityBillund();
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        s_transparentIcon = iconRegister.registerIcon("billund:transparent");
    }
}
