package dan200.billund.shared.network;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import dan200.billund.shared.network.message.MessageOrder;

/**
 * @author dmillerw
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("Billund");

    public static void initialize() {
        INSTANCE.registerMessage(MessageOrder.class, MessageOrder.class, 0, Side.SERVER);
    }
}
