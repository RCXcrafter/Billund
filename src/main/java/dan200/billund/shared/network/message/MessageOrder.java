package dan200.billund.shared.network.message;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import dan200.billund.shared.data.BillundSet;
import dan200.billund.shared.entity.EntityAirDrop;
import dan200.billund.shared.helper.EmeraldHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author dmillerw
 */
public class MessageOrder implements IMessage, IMessageHandler<MessageOrder, IMessage> {

    public int orderType;

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(orderType);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        orderType = buf.readInt();
    }

    @Override
    public IMessage onMessage(MessageOrder message, MessageContext ctx) {
        EntityPlayer entity = ctx.getServerHandler().playerEntity;
        World world = entity.worldObj;
        int set = message.orderType;
        int cost = BillundSet.get(set).getCost();
        if (EmeraldHelper.removeEmeralds(entity, cost)) {
            Random r = new Random();
            world.spawnEntityInWorld(new EntityAirDrop(
                    world,
                    Math.floor(entity.posX - 8 + r.nextInt(16)) + 0.5f,
                    Math.min(world.getHeight(), 255) - r.nextInt(32) - 0.5f,
                    Math.floor(entity.posZ - 8 + r.nextInt(16)) + 0.5f,
                    set
            ));
        }
    return null;
    }
}
