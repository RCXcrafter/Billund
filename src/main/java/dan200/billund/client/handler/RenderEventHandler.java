package dan200.billund.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import dan200.billund.client.helper.BrickRenderHelper;
import dan200.billund.shared.data.Brick;
import dan200.billund.shared.item.ItemBrick;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class RenderEventHandler {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (Minecraft.getMinecraft().gameSettings.hideGUI) {
            return;
        }

        // Draw the preview brick
        float f = event.partialTicks;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World world = Minecraft.getMinecraft().theWorld;
        if (player != null && world != null) {
            ItemStack currentStack = player.inventory.getCurrentItem();
            if (currentStack != null && currentStack.getItem() instanceof ItemBrick) {
                Brick brick = ItemBrick.getPotentialBrick(currentStack, player.worldObj, player, f);
                if (brick != null) {
                    // Setup
                    GL11.glPushMatrix();
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
                    GL11.glEnable(GL11.GL_BLEND);

                    BrickRenderHelper.translateToWorldCoords(Minecraft.getMinecraft().renderViewEntity, f);
                    BrickRenderHelper.renderBrick(world, brick);

                    // Teardown
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glPopMatrix();
                }
            }
        }
    }
}