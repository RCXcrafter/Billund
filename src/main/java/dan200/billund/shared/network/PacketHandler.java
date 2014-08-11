package dan200.billund.shared.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dan200.billund.shared.network.message.MessageOrder;
import dan200.billund.shared.network.message.MessageRotate;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("Billund");

    public static void initialize() {
        INSTANCE.registerMessage(MessageOrder.class, MessageOrder.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageRotate.class, MessageRotate.class, 1, Side.SERVER);
    }
}
