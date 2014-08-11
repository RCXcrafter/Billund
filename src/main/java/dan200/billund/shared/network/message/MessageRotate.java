package dan200.billund.shared.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dan200.billund.shared.core.BillundProxyCommon;
import io.netty.buffer.ByteBuf;

/**
 * @author dmillerw
 */
public class MessageRotate implements IMessage, IMessageHandler<MessageRotate, IMessage> {

    public int rotate;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(rotate);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        rotate = buf.readInt();
    }

    @Override
    public IMessage onMessage(MessageRotate message, MessageContext ctx) {
        BillundProxyCommon.rotate = message.rotate;
        return null;
    }
}
