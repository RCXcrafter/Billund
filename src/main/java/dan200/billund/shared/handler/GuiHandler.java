package dan200.billund.shared.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import dan200.billund.client.gui.GuiOrderForm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * @author dmillerw
 */
public class GuiHandler implements IGuiHandler {

    public static final int GUI_ORDER_FORM = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case GUI_ORDER_FORM:
                return new GuiOrderForm(player);
        }
        return null;
    }
}
