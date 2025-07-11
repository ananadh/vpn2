package com.mzhslab.koreavpn.App_Class;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.mzhslab.koreavpn.utils.AppOpenManager;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.Collections;
import java.util.List;

public class MyApplication extends Application {
    private static AppOpenManager appOpenManager;
    private static Context mContext;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        List<String> testDeviceIds = Collections.singletonList("7D4189C95680C91815F7CF69CB8A59D4");
        RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(this, initializationStatus -> {});
        StartAppSDK.init(this, "201953300", false);
        StartAppAd.disableSplash();
    }
    public static Context getContext() {
        return MyApplication.mContext;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}


