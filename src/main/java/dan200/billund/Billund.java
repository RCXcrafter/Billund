/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import dan200.billund.shared.core.BillundProxyCommon;
import net.minecraft.creativetab.CreativeTabs;

@Mod(modid = "Billund", name = "Billund", version = "%MOD_VERSION%")
public class Billund {

    public static CreativeTabs creativeTab;

    public static CreativeTabs getCreativeTab() {
        return creativeTab;
    }

    @Mod.Instance(value = "Billund")
    public static Billund instance;

    @SidedProxy(clientSide = "dan200.billund.client.core.BillundProxyClient", serverSide = "dan200.billund.shared.core.BillundProxyCommon")
    public static BillundProxyCommon proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
    }
}
