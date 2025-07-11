package com.mzhslab.koreavpn.vpnhelper;

import android.content.Context;
import android.util.Log;

import de.blinkt.openvpn.OpenVpnConnector;
import de.blinkt.openvpn.core.VpnStatus;


public class VPNServerHelper {

    private Context context;
    String file;

    public VPNServerHelper(Context context) {
        this.context = context;
    }

    public boolean connectOrDisconnect(String locationFileName, String user, String password) {
        boolean isActive = VpnStatus.isVPNActive();
        if (isActive) {
            disconnectFromVpn();
        } else {
            connectToVpn(locationFileName, user, password);
        }
        return true;
    }

    public void connectToVpn(String locationFileName, String user, String password) {
        try {
            Log.d("checkProfile2", "  " + locationFileName + "   " + user + "    " + password);
            OpenVpnConnector.connectToVpn(context, locationFileName, user, password);
        } catch (RuntimeException e) {
            Log.d("checkProfile4", "" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnectFromVpn() {
        OpenVpnConnector.disconnectFromVpn(context);
    }
}
