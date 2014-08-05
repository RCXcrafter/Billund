/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013. Do not distribute without permission.
 * Send enquiries to dratcliffe@gmail.com
 */

package dan200.billund.server;

import dan200.billund.shared.BillundProxyCommon;

public class BillundProxyServer extends BillundProxyCommon {

    @Override
    public void init() {
        super.init();
        registerForgeHandlers();
    }

    private void registerForgeHandlers() {
    }
}
