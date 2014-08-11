package dan200.billund.client.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dan200.billund.shared.core.BillundProxyCommon;
import dan200.billund.shared.network.PacketHandler;
import dan200.billund.shared.network.message.MessageRotate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

/**
 * @author dmillerw
 */
public class ClientTickHandler {

    public static KeyBinding KEY_ROTATE = new KeyBinding("billund.rotate", Keyboard.KEY_R, "keys.general");

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player != null) {
            if (KEY_ROTATE.isPressed()) {
                BillundProxyCommon.rotate = (BillundProxyCommon.rotate + 1) % 4;
                MessageRotate messageRotate = new MessageRotate();
                messageRotate.rotate = BillundProxyCommon.rotate;
                PacketHandler.INSTANCE.sendToServer(messageRotate);
                System.out.println(BillundProxyCommon.rotate);
            }

//            if (lastSlot != player.inventory.currentItem) {
//                offsetX = 0;
//                offsetZ = 0;
//                lastSlot = player.inventory.currentItem;
//            }

//            if (GuiScreen.isShiftKeyDown()) {
//                int rotation = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//                GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
//
//                if (gameSettings.keyBindForward.isPressed()) {
//                    switch (rotation) {
//                        case 0: offsetZ++; break;
//                        case 1: offsetX--; break;
//                        case 2: offsetZ--; break;
//                        case 3: offsetX++; break;
//                    }
//                }
//
//                if (gameSettings.keyBindBack.isPressed()) {
//                    switch (rotation) {
//                        case 0: offsetZ--; break;
//                        case 1: offsetX++; break;
//                        case 2: offsetZ++; break;
//                        case 3: offsetX--; break;
//                    }
//                }
//
//                if (gameSettings.keyBindLeft.isPressed()) {
//                    switch (rotation) {
//                        case 0: offsetX++; break;
//                        case 1: offsetZ++; break;
//                        case 2: offsetX--; break;
//                        case 3: offsetZ--; break;
//                    }
//                }
//
//                if (gameSettings.keyBindRight.isPressed()) {
//                    switch (rotation) {
//                        case 0: offsetX--; break;
//                        case 1: offsetZ--; break;
//                        case 2: offsetX++; break;
//                        case 3: offsetZ++; break;
//                    }
//                }
//            }
        }
    }
}
