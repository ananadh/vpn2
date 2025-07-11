package de.blinkt.openvpn;

import android.app.PendingIntent;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.S)
public class Constant {
    public static int PENDING_INTENT_FLAG= PendingIntent.FLAG_MUTABLE;
}
