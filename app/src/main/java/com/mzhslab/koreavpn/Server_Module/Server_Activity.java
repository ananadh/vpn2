package com.mzhslab.koreavpn.Server_Module;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mzhslab.koreavpn.Model.Api_response;
import com.mzhslab.koreavpn.Model.Cities_Server_List_Pojo;
import com.mzhslab.koreavpn.Model.Parent_Server_Adapter;
import com.mzhslab.koreavpn.Model.api_model;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.adapter.ServerCountryAdapter;
import com.mzhslab.koreavpn.vpnhelper.OnItemClickAdapter;
import com.usa_vpn.country_picker.Country_Flag_Picker;
import com.usa_vpn.country_picker.Country_Names;
import com.usa_vpn.country_picker.listeners.Country_Picker_Listener_Interface;

import java.util.ArrayList;

public class Server_Activity extends AppCompatActivity implements Country_Picker_Listener_Interface, OnItemClickAdapter {

    ArrayList<api_model> api_response_modelArrayList;
    SharedPreferences sharedpreferences;
    private ServerCountryAdapter scadapter;
    public Country_Flag_Picker countryFlagPicker;
    private OnItemClickAdapter onclick;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        sharedpreferences = getSharedPreferences("DATA", MODE_PRIVATE);
        api_response_modelArrayList = getAllServers_from_cache();
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //window.setStatusBarColor(this.getResources().getColor(R.color.server_list));

        countryFlagPicker = new Country_Flag_Picker.Builder().with(Server_Activity.this).listener(this).build();

        if (!api_response_modelArrayList.isEmpty()) {

            Log.d("api_response_model" , ""+api_response_modelArrayList.size() + "   " + getCitiesList(api_response_modelArrayList).size());
            scadapter = new ServerCountryAdapter(Server_Activity.this, getCitiesList(api_response_modelArrayList), countryFlagPicker);
            onclick=this;
            scadapter.setItemClick(onclick);
            RecyclerView recyclerView = findViewById(R.id.rv_locations);
            ImageView close = findViewById(R.id.iv_back);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Server_Activity.this);
            recyclerView.setLayoutManager(layoutManager);
            ViewCompat.setNestedScrollingEnabled(recyclerView, false);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(scadapter);

            close.setOnClickListener(view -> onBackPressed());
        }

    }


    public ArrayList<Parent_Server_Adapter> getCitiesList(ArrayList<api_model> countryList) {

        ArrayList<Parent_Server_Adapter> parentList = new ArrayList<>();

        if (countryList != null && countryList.size() > 0) {

            for (api_model parent : countryList) {

                if (!isExist(parentList, parent.getCountry_name())) {

                    Parent_Server_Adapter data = new Parent_Server_Adapter();

                    data.setCountry_name(parent.getCountry_name());

                    Log.d("Country_Nameee", "" + parent.getCountry_name());
                    data.setCity(parent.getCity());
                    data.setIp_ping(parent.getIp_ping());
                    data.setPing_val(parent.getPing_val());
                    data.setUsername(parent.getUsername());
                    data.setPassword(parent.getPassword());
                    data.setConfig(parent.getConfig());
                    data.setType(parent.getType());

                    ArrayList<Cities_Server_List_Pojo> childList = new ArrayList();
                    for (int i = 0; i < countryList.size(); i++) {

                        if (countryList.get(i) != null) {

                            if (parent.getCountry_name().equalsIgnoreCase(countryList.get(i).getCountry_name())) {

                                Cities_Server_List_Pojo citys = new Cities_Server_List_Pojo();

                                citys.setCountry_name(countryList.get(i).getCountry_name());
                                citys.setCity(countryList.get(i).getCity());
                                citys.setIp_ping(countryList.get(i).getIp_ping());
                                citys.setPing_val(countryList.get(i).getPing_val());
                                citys.setUsername(countryList.get(i).getUsername());
                                citys.setPassword(countryList.get(i).getPassword());
                                citys.setConfig(countryList.get(i).getConfig());
                                citys.setType(countryList.get(i).getType());
                                childList.add(citys);
                            }
                        }
                    }
                    data.citiesList = childList;
                    parentList.add(data);
                }
            }

        }
        return parentList;
    }
    public boolean isExist(ArrayList<Parent_Server_Adapter> arrayList, String item) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getCountry_name().equalsIgnoreCase(item))
                return true;
        }
        return false;
    }

    public ArrayList<api_model> getAllServers_from_cache() {

        try {
            Gson gson = new Gson();
            if (sharedpreferences != null) {
                String json = sharedpreferences.getString("sgvpn_server_list", null);
                if (json != null) {
                    if (!json.isEmpty() || !json.equals("")) {
                        return gson.fromJson(json, Api_response.class);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onSelectCountry(Country_Names countryNames) {

        Log.d("I am there","Hi--->"+countryNames);
    }

    @Override
    public void onResult(String countryName) {


    }
}
