/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.client.core;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import dan200.Billund;
import dan200.billund.client.handler.RenderEventHandler;
import dan200.billund.client.render.BrickBlockRenderer;
import dan200.billund.client.render.BrickItemRenderer;
import dan200.billund.client.render.RenderAirDrop;
import dan200.billund.shared.core.BillundProxyCommon;
import dan200.billund.shared.entity.EntityAirDrop;
import dan200.billund.shared.tile.TileEntityBillund;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class BillundProxyClient extends BillundProxyCommon {

    @Override
    public void preInit() {
        super.preInit();

        MinecraftForge.EVENT_BUS.register(new RenderEventHandler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBillund.class, new BrickBlockRenderer());
        MinecraftForgeClient.registerItemRenderer(Billund.BillundItems.brick, new BrickItemRenderer());
        RenderingRegistry.registerEntityRenderingHandler(EntityAirDrop.class, new RenderAirDrop());
    }
}
