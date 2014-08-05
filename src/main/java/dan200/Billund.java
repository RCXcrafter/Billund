/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dan200.billund.shared.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

///////////////
// UNIVERSAL //
///////////////

@Mod(modid = "Billund", name = BuildInfo.ModName, version = BuildInfo.Version)
public class Billund {

    public static class BillundBlocks {
        public static BlockBillund billund;
    }

    public static class BillundItems {
        public static ItemBrick brick;
        public static ItemOrderForm orderForm;
    }

    // Other stuff
    public static CreativeTabs creativeTab;

    public static CreativeTabs getCreativeTab() {
        return creativeTab;
    }

    // Implementation
    @Mod.Instance(value = "Billund")
    public static Billund instance;

    @SidedProxy(clientSide = "dan200.billund.client.BillundProxyClient", serverSide = "dan200.billund.server.BillundProxyServer")
    public static IBillundProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    private static boolean removeEmeralds(EntityPlayer player, int cost) {
        // Find enough emeralds
        int emeralds = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() == Items.emerald) {
                emeralds += stack.stackSize;
                if (emeralds >= cost) {
                    break;
                }
            }
        }

        if (emeralds >= cost) {
            // Then expend them
            emeralds = cost;
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack != null && stack.getItem() == Items.emerald) {
                    if (stack.stackSize <= emeralds) {
                        player.inventory.setInventorySlotContents(i, null);
                        emeralds -= stack.stackSize;
                    } else {
                        stack.stackSize -= emeralds;
                        emeralds = 0;
                    }
                    if (emeralds == 0) {
                        break;
                    }
                }
            }
            player.inventory.markDirty();
            return true;
        }
        return false;
    }

//    public static void handlePacket(BillundPacket packet, Player player) {
//        switch (packet.packetType) {
//            case BillundPacket.OrderSet: {
//                EntityPlayer entity = (EntityPlayer) player;
//                World world = entity.worldObj;
//                int set = packet.dataInt[0];
//                int cost = BillundSet.get(set).getCost();
//                if (removeEmeralds(entity, cost)) {
//                    Random r = new Random();
//                    world.spawnEntityInWorld(new EntityAirDrop(
//                            world,
//                            Math.floor(entity.posX - 8 + r.nextInt(16)) + 0.5f,
//                            Math.min(world.getHeight(), 255) - r.nextInt(32) - 0.5f,
//                            Math.floor(entity.posZ - 8 + r.nextInt(16)) + 0.5f,
//                            set
//                    ));
//                }
//                break;
//            }
//        }
//    }
}
