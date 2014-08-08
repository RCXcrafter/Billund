/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.shared.core;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import dan200.Billund;
import dan200.billund.shared.block.BlockBillund;
import dan200.billund.shared.entity.EntityAirDrop;
import dan200.billund.shared.handler.EntityEventHandler;
import dan200.billund.shared.handler.GuiHandler;
import dan200.billund.shared.item.ItemBrick;
import dan200.billund.shared.item.ItemOrderForm;
import dan200.billund.shared.network.PacketHandler;
import dan200.billund.shared.tile.TileEntityBillund;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;

public class BillundProxyCommon {

    public void preInit() {
        // Register our own creative tab
        Billund.creativeTab = new CreativeTabBillund(CreativeTabs.getNextID(), "Billund");

        // Billund block
        Billund.BillundBlocks.billund = new BlockBillund();
        GameRegistry.registerBlock(Billund.BillundBlocks.billund, "Billund");
        GameRegistry.registerTileEntity(TileEntityBillund.class, "billund");

        // Brick item
        Billund.BillundItems.brick = new ItemBrick();
        GameRegistry.registerItem(Billund.BillundItems.brick, "Brick");

        // Order form item
        Billund.BillundItems.orderForm = new ItemOrderForm();
        GameRegistry.registerItem(Billund.BillundItems.orderForm, "OrderForm");

        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
        FMLCommonHandler.instance().bus().register(new EntityEventHandler());

        NetworkRegistry.INSTANCE.registerGuiHandler(Billund.instance, new GuiHandler());
        PacketHandler.initialize();

        EntityRegistry.registerModEntity(EntityAirDrop.class, "AirDrop", 1, Billund.instance, 80, 3, true);
    }
}
