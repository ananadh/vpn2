
package com.mzhslab.koreavpn.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mzhslab.koreavpn.Model.Cities_Server_List_Pojo;
import com.mzhslab.koreavpn.Model.Parent_Server_Adapter;
import com.mzhslab.koreavpn.Model.api_model;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.utils.Constant;
import com.mzhslab.koreavpn.vpnhelper.OnItemClickAdapter;
import com.usa_vpn.country_picker.Country_Names;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.mzhslab.koreavpn.utils.Constant.decodeSampledBitmapFromResource;
import static com.mzhslab.koreavpn.utils.Constant.storeValueToPreference;


public class Server_City_Adapter extends RecyclerView.Adapter<Server_City_View_Holder> {

    public Context mContext;
    public ArrayList<Cities_Server_List_Pojo> city_list;
    Country_Names countries;
    String country_namess;
    Parent_Server_Adapter parentserver;
    SharedPreferences choose_Server_preference;
    OnItemClickAdapter listener;

    public Server_City_Adapter(Context pContext, ArrayList<Cities_Server_List_Pojo> pFlowerLists, Parent_Server_Adapter parentserver,
                               String country_namess, Country_Names countries) {

        mContext = pContext;
        city_list = pFlowerLists;
        this.parentserver = parentserver;
        this.country_namess = country_namess;
        this.countries = countries;

        choose_Server_preference = mContext.getSharedPreferences("DATA", MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Server_City_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_city_item_, parent, false);
        return new Server_City_View_Holder(mView);
    }

    public void setOnClick(OnItemClickAdapter onclick) {
        listener = onclick;
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull Server_City_View_Holder holder, int position) {

        Cities_Server_List_Pojo citiesServerListPojo = city_list.get(position);

        Log.d("citiesServerListPojo", "" + citiesServerListPojo.getCity());
        holder.child_countryName.setText("" + citiesServerListPojo.getCity());

        holder.child_flag.setImageBitmap(decodeSampledBitmapFromResource(mContext.getResources(), countries.getFlag(), 50, 50));

        if (citiesServerListPojo.getPing_val() <= 300) {
            //less then 300
            holder.ping_val_city.setTextColor(mContext.getResources().getColor(R.color.connected));
            holder.ping_val_city.setText("" + citiesServerListPojo.getPing_val() + " ms");
        } else if (citiesServerListPojo.getPing_val() > 300 && citiesServerListPojo.getPing_val() < 600) {
            //greater then 300 & less then 600
            holder.ping_val_city.setTextColor(mContext.getResources().getColor(R.color.blue));
            holder.ping_val_city.setText("" + citiesServerListPojo.getPing_val() + " ms");
        } else if (citiesServerListPojo.getPing_val() > 600 && citiesServerListPojo.getPing_val() < 1000) {
            //greater then 300 & less then 600
            holder.ping_val_city.setTextColor(mContext.getResources().getColor(R.color.yellow));
            holder.ping_val_city.setText("" + citiesServerListPojo.getPing_val() + " ms");
        } else {
            holder.ping_val_city.setTextColor(mContext.getResources().getColor(R.color.sample_progress_primary));
            holder.ping_val_city.setText("" + citiesServerListPojo.getPing_val() + " ms");
        }

//        holder.child_usage.setText("" + citiesServerListPojo.getCapacity_val() + "%");
        holder.cardview_location_city.setOnClickListener(view -> {

            Log.d("its_clicked", "" + citiesServerListPojo.getIp_ping() + "  " + citiesServerListPojo.getCountry_name() + " " + citiesServerListPojo.getCountry_name());

            api_model Api_response_model = new api_model();
            Api_response_model.setServer_id(citiesServerListPojo.getServer_id());
            Api_response_model.setCountry_name(citiesServerListPojo.getCountry_name());
            Api_response_model.setCity(citiesServerListPojo.getCity());
            Api_response_model.setIp_ping(citiesServerListPojo.getIp_ping());
            Api_response_model.setConfig(citiesServerListPojo.getConfig());
            Api_response_model.setType(citiesServerListPojo.getType());
            Api_response_model.setUsername(citiesServerListPojo.getUsername());
            Api_response_model.setPassword(citiesServerListPojo.getPassword());

            Constant.IS_RUN = true;
            Constant.SHOW_CONNECT = true;
            storeValueToPreference(choose_Server_preference, "sgvpn_model_data", Api_response_model);

            Intent intentData = new Intent();
            intentData.putExtra("countryName", citiesServerListPojo.getCountry_name() + " ");
            ((Activity) mContext).setResult(110090, intentData);
            ((Activity) mContext).finish();

            Intent intent = new Intent("server_model");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        });

        holder.child_radioBtn.setOnClickListener(view -> {
            listener.onResult(citiesServerListPojo.getCountry_name().toString());

            Log.d("its_clicked", "" + citiesServerListPojo.getIp_ping() + "  " + citiesServerListPojo.getCountry_name() + " " + citiesServerListPojo.getCountry_name());

            api_model Api_response_model = new api_model();
            Api_response_model.setServer_id(citiesServerListPojo.getServer_id());
            Api_response_model.setCountry_name(citiesServerListPojo.getCountry_name());
            Api_response_model.setCity(citiesServerListPojo.getCity());
            Api_response_model.setIp_ping(citiesServerListPojo.getIp_ping());
            Api_response_model.setConfig(citiesServerListPojo.getConfig());
            Api_response_model.setType(citiesServerListPojo.getType());
            Api_response_model.setUsername(citiesServerListPojo.getUsername());
            Api_response_model.setPassword(citiesServerListPojo.getPassword());

            Constant.IS_RUN = true;
            Constant.SHOW_CONNECT = true;

            storeValueToPreference(choose_Server_preference, "sgvpn_model_data", Api_response_model);
            Intent intentData = new Intent();
            intentData.putExtra("countryName", citiesServerListPojo.getCountry_name() + " ");
            ((Activity) mContext).setResult(110090, intentData);
            ((Activity) mContext).finish();
            Intent intent = new Intent("server_model");
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        });
    }

    @Override
    public int getItemCount() {
        int limit = 20;
        return Math.min(city_list.size(), limit);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}


class Server_City_View_Holder extends RecyclerView.ViewHolder {

    public TextView child_countryName, udp_tcp, ping_val_city;
    ImageView child_flag;
    RadioButton child_radioBtn;
    LinearLayout cardview_location_city;


    Server_City_View_Holder(View itemView) {
        super(itemView);
        child_countryName = itemView.findViewById(R.id.country_name_city);
        child_flag = itemView.findViewById(R.id.child_flag);
        ping_val_city = itemView.findViewById(R.id.ping_val_city);
        child_radioBtn = itemView.findViewById(R.id.child_radioBtn);
        cardview_location_city = itemView.findViewById(R.id.cardview_location_city);
    }
}













