package com.mzhslab.koreavpn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Arrays;
import java.util.HashSet;

public class Apps_Packages {

    public static HashSet<String> getPackageNamesFromString(String tickedAppStrs) {
        HashSet<String> result = new HashSet<>(Arrays.asList(tickedAppStrs.split(",")));
        result.add("");
        return result;
    }

    public static HashSet<String> getUnSelectedPackageNamesFromString(String untickedAppStr) {

        return new HashSet<>(Arrays.asList(untickedAppStr.split(",")));
    }

    public static void putPref(String key, String value, Context context) {
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, value);
                editor.apply();
            }
        }
    }

    public static void putPref(String key, int value, Context context) {
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(key, value);
                editor.apply();
            }
        }

    }

    public static void putPref(String key, long value, Context context) {
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(key, value);
                editor.apply();
            }
        }

    }

    public static void putPref(String key, boolean value, Context context) {
        if (context != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(key, value);
                editor.apply();
            }
        }
    }

    public static String getPref(String key, String defValue, Context context) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences != null)
                return preferences.getString(key, defValue);
            else
                return defValue;
        }

        return defValue;
    }

    public static int getPref(String key, int defValue, Context context) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences != null) {
                return preferences.getInt(key, defValue);
            } else {
                return defValue;
            }
        }
        return defValue;
    }

    public static long getPref(String key, long defValue, Context context) {

        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences != null) {
                return preferences.getLong(key, defValue);
            } else {
                return defValue;
            }
        }
        return defValue;
    }

    public static boolean getPref(String key, boolean defValue, Context context) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences != null) {
                return preferences.getBoolean(key, defValue);
            } else {
                return defValue;
            }
        }

        return defValue;
    }
}
