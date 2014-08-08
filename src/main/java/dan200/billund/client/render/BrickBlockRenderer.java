package dan200.billund.client.render;

import dan200.billund.client.helper.BrickRenderHelper;
import dan200.billund.shared.data.Stud;
import dan200.billund.shared.tile.TileEntityBillund;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class BrickBlockRenderer extends TileEntitySpecialRenderer {

    private void renderBrickAt(TileEntityBillund tile, double xc, double yc, double zc, float partial) {
        GL11.glPushMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        Tessellator tessellator = Tessellator.instance;

        BrickRenderHelper.translateToWorldCoords(Minecraft.getMinecraft().renderViewEntity, partial);

        tessellator.startDrawingQuads();

        for (int x = 0; x < TileEntityBillund.STUDS_PER_ROW; ++x) {
            for (int y = 0; y < TileEntityBillund.STUDS_PER_COLUMN; ++y) {
                for (int z = 0; z < TileEntityBillund.STUDS_PER_ROW; ++z) {
                    Stud stud = tile.getStudLocal(x, y, z);
                    if (stud != null) {
                        int sx = tile.xCoord * TileEntityBillund.STUDS_PER_ROW + x;
                        int sy = tile.yCoord * TileEntityBillund.STUDS_PER_COLUMN + y;
                        int sz = tile.zCoord * TileEntityBillund.STUDS_PER_ROW + z;
                        if (stud.XOrigin == sx && stud.YOrigin == sy && stud.ZOrigin == sz) {
                            // Draw the brick
                            int brightness = tile.blockType.getMixedBrightnessForBlock(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);
                            BrickRenderHelper.renderBrick(
                                    tile.getWorldObj(), brightness, stud.Colour, 1.0F,
                                    sx, sy, sz, stud.BrickWidth, stud.BrickHeight, stud.BrickDepth
                            );
                        }
                    }
                }
            }
        }

        tessellator.draw();

        // Teardown
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partial) {
        renderBrickAt((TileEntityBillund) tile, x, y, z, partial);
    }
}