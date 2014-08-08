package dan200.billund.client.helper;

import dan200.Billund;
import dan200.billund.shared.data.Brick;
import dan200.billund.shared.data.Stud;
import dan200.billund.shared.data.StudColour;
import dan200.billund.shared.item.ItemBrick;
import dan200.billund.shared.tile.TileEntityBillund;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

/**
 * @author dmillerw
 */
public class BrickRenderHelper {

    public static void translateToWorldCoords(Entity entity, float frame) {
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
    }

    public static int getColor(int r, int g, int b) {
        return ((r & 255) << 16) |
               ((g & 255) << 8)  |
               ((b & 255) << 0);
    }

    public static void renderBrick(ItemStack brick, boolean scale, boolean center) {
        Tessellator tessellator = Tessellator.instance;
        int brightness = 15;

        int colour = ItemBrick.getColour(brick);
        int width = ItemBrick.getWidth(brick);
        int height = ItemBrick.getHeight(brick);
        int depth = ItemBrick.getDepth(brick);

        // Setup
        GL11.glPushMatrix();

        if (scale) {
            float scaleValue = ((float) TileEntityBillund.LAYERS_PER_BLOCK) / Math.max(2.0f, (float) Math.max(width, depth) - 0.5f);
            GL11.glScalef(scaleValue, scaleValue, scaleValue);
        }
        if (center) {
            GL11.glTranslatef(
                    -0.5f * ((float) width / (float) TileEntityBillund.ROWS_PER_BLOCK),
                    -0.5f * ((float) height / (float) TileEntityBillund.LAYERS_PER_BLOCK),
                    -0.5f * ((float) depth / (float) TileEntityBillund.ROWS_PER_BLOCK)
            );
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        renderBrick(null, brightness, colour, 0.5F, 0, 0, 0, width, height, depth);
        tessellator.draw();

        // Teardown
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void renderBrick(IBlockAccess world, Brick brick) {
        int localX = (brick.XOrigin % TileEntityBillund.ROWS_PER_BLOCK + TileEntityBillund.ROWS_PER_BLOCK) % TileEntityBillund.ROWS_PER_BLOCK;
        int localY = (brick.YOrigin % TileEntityBillund.LAYERS_PER_BLOCK + TileEntityBillund.LAYERS_PER_BLOCK) % TileEntityBillund.LAYERS_PER_BLOCK;
        int localZ = (brick.ZOrigin % TileEntityBillund.ROWS_PER_BLOCK + TileEntityBillund.ROWS_PER_BLOCK) % TileEntityBillund.ROWS_PER_BLOCK;
        int blockX = (brick.XOrigin - localX) / TileEntityBillund.ROWS_PER_BLOCK;
        int blockY = (brick.YOrigin - localY) / TileEntityBillund.LAYERS_PER_BLOCK;
        int blockZ = (brick.ZOrigin - localZ) / TileEntityBillund.ROWS_PER_BLOCK;

        Tessellator tessellator = Tessellator.instance;
        int brightness = Billund.BillundBlocks.billund.getMixedBrightnessForBlock(world, blockX, blockY, blockZ);

        tessellator.startDrawingQuads();
        renderBrick(world, brightness, brick.Colour, 0.65F, brick.XOrigin, brick.YOrigin, brick.ZOrigin, brick.Width, brick.Height, brick.Depth);
        tessellator.draw();
    }

    public static void renderBrick(IBlockAccess world, int brightness, int colour, float alpha, int sx, int sy, int sz, int width, int height, int depth) {
        // Draw the brick
        if (world != null) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(brightness);
        }

        float pixel = 1.0f / 96.0f;
        float xBlockSize = (float) TileEntityBillund.STUDS_PER_ROW;
        float yBlockSize = (float) TileEntityBillund.STUDS_PER_COLUMN;
        float zBlockSize = (float) TileEntityBillund.STUDS_PER_ROW;

        float startX = (float) sx / xBlockSize;
        float startY = (float) sy / yBlockSize;
        float startZ = (float) sz / zBlockSize;
        float endX = startX + ((float) width / xBlockSize);
        float endY = startY + ((float) height / yBlockSize);
        float endZ = startZ + ((float) depth / zBlockSize);
        renderBox(
                colour,
                alpha,
                startX, startY, startZ,
                endX, endY, endZ,
                true
        );

        // Draw the studs
        int sny = sy + height;
        startY = (float) sny / yBlockSize;
        endY = startY + (0.1666f / yBlockSize);
        for (int snx = sx; snx < sx + width; ++snx) {
            startX = (float) snx / xBlockSize;
            endX = startX + (1.0f / xBlockSize);
            for (int snz = sz; snz < sz + depth; ++snz) {
                boolean drawStud;
                if (world != null) {
                    Stud above = TileEntityBillund.getStud(world, snx, sny, snz);
                    drawStud = (above == null) || (above.Colour == StudColour.TranslucentWall);
                } else {
                    drawStud = true;
                }

                if (drawStud) {
                    startZ = (float) snz / zBlockSize;
                    endZ = startZ + (1.0f / zBlockSize);
                    renderBox(
                            colour,
                            alpha,
                            startX + pixel * 2.0f, startY, startZ + pixel * 4.0f,
                            startX + pixel * 4.0f, endY, endZ - pixel * 4.0f,
                            false
                    );
                    renderBox(
                            colour,
                            alpha,
                            startX + pixel * 4.0f, startY, startZ + pixel * 2.0f,
                            endX - pixel * 4.0f, endY, endZ - pixel * 2.0f,
                            false
                    );
                    renderBox(
                            colour,
                            alpha,
                            endX - pixel * 4.0f, startY, startZ + pixel * 4.0f,
                            endX - pixel * 2.0f, endY, endZ - pixel * 4.0f,
                            false
                    );
                }
            }
        }
    }

    private static void renderBox(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ, boolean bottom) {
        // X faces
        renderFaceXNeg(color, alpha, startX, startY, startZ, endX, endY, endZ);
        renderFaceXPos(color, alpha, startX, startY, startZ, endX, endY, endZ);

        // Y faces
        if (bottom) {
            renderFaceYNeg(color, alpha, startX, startY, startZ, endX, endY, endZ);
        }
        renderFaceYPos(color, alpha, startX, startY, startZ, endX, endY, endZ);

        // Z faces
        renderFaceZNeg(color, alpha, startX, startY, startZ, endX, endY, endZ);
        renderFaceZPos(color, alpha, startX, startY, startZ, endX, endY, endZ);
    }

    private static void renderFaceXNeg(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        tessellator.setColorRGBA_F(r * 0.6f, g * 0.6f,  b * 0.6f, alpha);
        tessellator.addVertex(startX, endY, endZ);
        tessellator.addVertex(startX, endY, startZ);
        tessellator.addVertex(startX, startY, startZ);
        tessellator.addVertex(startX, startY, endZ);
    }

    private static void renderFaceXPos(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        tessellator.setColorRGBA_F(r * 0.6f, g * 0.6f,  b * 0.6f, alpha);
        tessellator.addVertex(endX, startY, endZ);
        tessellator.addVertex(endX, startY, startZ);
        tessellator.addVertex(endX, endY, startZ);
        tessellator.addVertex(endX, endY, endZ);
    }

    private static void renderFaceYNeg(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        tessellator.setColorRGBA_F(r * 0.5f, g * 0.5f, b * 0.5f, alpha);
        tessellator.addVertex(startX, startY, endZ);
        tessellator.addVertex(startX, startY, startZ);
        tessellator.addVertex(endX, startY, startZ);
        tessellator.addVertex(endX, startY, endZ);
    }

    private static void renderFaceYPos(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        tessellator.setColorRGBA_F(r, g, b, alpha);
        tessellator.addVertex(endX, endY, endZ);
        tessellator.addVertex(endX, endY, startZ);
        tessellator.addVertex(startX, endY, startZ);
        tessellator.addVertex(startX, endY, endZ);
    }

    private static void renderFaceZNeg(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        tessellator.setColorRGBA_F(r * 0.8f, g * 0.8f, b * 0.8f, alpha);
        tessellator.addVertex(startX, endY, startZ);
        tessellator.addVertex(endX, endY, startZ);
        tessellator.addVertex(endX, startY, startZ);
        tessellator.addVertex(startX, startY, startZ);
    }

    private static void renderFaceZPos(int color, float alpha, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        tessellator.setColorRGBA_F(r * 0.8f, g * 0.8f, b * 0.8f, alpha);
        tessellator.addVertex(startX, startY, endZ);
        tessellator.addVertex(endX, startY, endZ);
        tessellator.addVertex(endX, endY, endZ);
        tessellator.addVertex(startX, endY, endZ);
    }
}
