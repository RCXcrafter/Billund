/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.Billund;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

public abstract class BillundProxyCommon implements IBillundProxy {

    @Override
    public void preInit() {
        registerItems();
    }

    @Override
    public void init() {
        registerEntities();
        registerTileEntities();
        registerForgeHandlers();
    }

    private void registerItems() {
        // Register our own creative tab
        Billund.creativeTab = new CreativeTabBillund(CreativeTabs.getNextID(), "Billund");

        // Billund block
        Billund.BillundBlocks.billund = new BlockBillund();
        GameRegistry.registerBlock(Billund.BillundBlocks.billund, "Billund");

        // Brick item
        Billund.BillundItems.brick = new ItemBrick();
        GameRegistry.registerItem(Billund.BillundItems.brick, "Brick");

        // Order form item
        Billund.BillundItems.orderForm = new ItemOrderForm();
        GameRegistry.registerItem(Billund.BillundItems.orderForm, "OrderForm");
    }

    private void registerEntities() {
        // airdrop entity
        EntityRegistry.registerModEntity(EntityAirDrop.class, "AirDrop", 1, Billund.instance, 80, 3, true);
    }

    private void registerTileEntities() {
        // Tile Entities
        GameRegistry.registerTileEntity(TileEntityBillund.class, "billund");
    }

    private void registerForgeHandlers() {
        ForgeHandlers handlers = new ForgeHandlers();
        MinecraftForge.EVENT_BUS.register(handlers);
    }

    public class ForgeHandlers {
        private Random r = new Random();

        private ForgeHandlers() {
        }

        // Forge event responses

        @SubscribeEvent
        public void onEntityLivingDeath(LivingDeathEvent event) {
            if (event.entity.worldObj.isRemote) {
                return;
            }

            if (event.entity instanceof EntityZombie) {
                EntityLivingBase living = (EntityLivingBase) event.entity;
                if ((living.isChild() && r.nextInt(20) == 0) ||
                        (!living.isChild() && r.nextInt(100) == 0)) {
                    event.entity.entityDropItem(new ItemStack(Billund.BillundItems.orderForm, 1), 0.0f);
                }
            }
        }
    }
}
