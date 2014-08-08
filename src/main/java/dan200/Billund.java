/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dan200.billund.shared.block.BlockBillund;
import dan200.billund.shared.core.BillundProxyCommon;
import dan200.billund.shared.core.BuildInfo;
import dan200.billund.shared.item.ItemBrick;
import dan200.billund.shared.item.ItemOrderForm;
import net.minecraft.creativetab.CreativeTabs;

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

    @SidedProxy(clientSide = "dan200.billund.client.core.BillundProxyClient", serverSide = "dan200.billund.shared.core.BillundProxyCommon")
    public static BillundProxyCommon proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
}
