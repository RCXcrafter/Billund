package dan200.billund.shared.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import dan200.billund.Billund;
import dan200.billund.shared.block.BlockBillund;
import dan200.billund.shared.data.Brick;
import dan200.billund.shared.item.ItemBrick;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

/**
 * @author dmillerw
 */
public class EntityEventHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            // See what brick is in front of the player
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            World world = Minecraft.getMinecraft().theWorld;

            Brick hoverBrick = null;
            if (player != null && world != null) {
                BlockBillund.setHoverBrick(ItemBrick.getExistingBrick(world, player, 1F));
            } else {
                BlockBillund.setHoverBrick(null);
            }
        }
    }

    @SubscribeEvent
    public void onEntityLivingDeath(LivingDeathEvent event) {
        if (event.entity.worldObj.isRemote) {
            return;
        }

        if (event.entity instanceof EntityZombie) {
            Random r = new Random();
            EntityLivingBase living = (EntityLivingBase) event.entity;
            if ((living.isChild() && r.nextInt(20) == 0) || (!living.isChild() && r.nextInt(100) == 0)) {
                event.entity.entityDropItem(new ItemStack(Billund.BillundItems.orderForm, 1), 0.0f);
            }
        }
    }
}
