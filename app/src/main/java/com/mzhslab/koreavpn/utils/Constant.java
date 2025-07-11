package com.mzhslab.koreavpn.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mzhslab.koreavpn.Model.api_data_model_updated;
import com.mzhslab.koreavpn.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class Constant {
    public static String ADMOB_NATIVE_TESTING;
    public static String ADMOB_BANNER_AD;
    public static String ADMOB_INTERSETIAL_AD;
    public static boolean IS_RUN = false;
    public static boolean SHOW_CONNECT = false;
    public static String json_value = "";
    public static String startapp_id = "";
    public static String Startad_enable = "";
    public static String open_ad;
    public static int PENDING_INTENT_FLAG = PendingIntent.FLAG_MUTABLE;
    public static final String APP_URL = "https://onevpn.in/onevpn_koreavpn/api.php?action=get_all_Servers";
    public static final String ADS_URL = "https://onevpn.in/onevpn_koreavpn/api.php?action=get_ad_ids";

    public static boolean isNetworkAvailable(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }


    public static String LoadData(Context context) {
        String json = null;
        try {
            InputStream inputStream = context.getAssets().open("response.txt");
            int size = inputStream.available();
            byte[] byteArray = new byte[size];
            inputStream.read(byteArray);
            inputStream.close();
            json = new String(byteArray, StandardCharsets.UTF_8);

            Log.d("backup_response", "" + json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public static void storeValueToPreference(SharedPreferences sharedPreferences, String key, Object object) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(object);
            if (sharedPreferences.contains(key)) {
                editor.remove(key).apply();
            }
            editor.putString(key, json);
            editor.apply();
        }
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static api_data_model_updated getBestServer_after_calculation(SharedPreferences sharedPreferences) {
        api_data_model_updated responsePojo = (api_data_model_updated) getPreference("sgvpn_model_data", sharedPreferences);
        if (responsePojo != null) {
            if (responsePojo.getCountry_name() != null) {
                return responsePojo;
            } else {
                return null;
            }
        }
        return null;
    }

    public static Object getPreference(String key, SharedPreferences global_sharedPreferences) {
        Gson gson = new Gson();
        if (global_sharedPreferences != null) {
            json_value = global_sharedPreferences.getString(key, null);
        }

        if (json_value != null) {
            if (!json_value.isEmpty()) {
                try {
                    return gson.fromJson(json_value, api_data_model_updated.class);
                } catch (JsonSyntaxException | IllegalStateException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }

        return null;
    }

    public static boolean isOven_Vpn_ConnectionActive() {

        String iface = "";
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp())
                    iface = networkInterface.getName();
                if (iface.contains("tun") || iface.contains("ppp") || iface.contains("pptp")) {
                    return true;
                }
            }
        } catch (SocketException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void set_status_bar(Activity activity) {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorAccent));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void set_status_bar_t(Activity activity) {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.orange));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


