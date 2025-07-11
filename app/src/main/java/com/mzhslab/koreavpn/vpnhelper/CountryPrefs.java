package com.mzhslab.koreavpn.vpnhelper;

import android.content.Context;
import android.content.SharedPreferences;

public class CountryPrefs {
    private final SharedPreferences sharedPreferences;

    private final static String LOCATION_NAME = "location_name";
    private final static String LOCATION_POSITION = "position";
    private final static String LOCATION_SELECTED = "selected_location";
    private final static String HELP_TEXT = "help_text_shown";

    public CountryPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences("com.mzhslab.koreavpn.region_pref", Context.MODE_PRIVATE);
    }




    public String getSelectedLocation() {
        return sharedPreferences.getString(LOCATION_SELECTED, "");
    }

    public void setSelectedLocation(String locationName) {
        sharedPreferences.edit().putString(LOCATION_SELECTED, locationName).apply();
    }

    public boolean getHelpShown() {
        return sharedPreferences.getBoolean(HELP_TEXT, false);
    }

    public void setHelpShown(boolean status) {
        sharedPreferences.edit().putBoolean(HELP_TEXT, status).apply();
    }


}
