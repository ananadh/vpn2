package com.mzhslab.koreavpn.adapter;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mzhslab.koreavpn.Model.Parent_Server_Adapter;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.vpnhelper.OnItemClickAdapter;
import com.usa_vpn.country_picker.Country_Flag_Picker;
import com.usa_vpn.country_picker.Country_Names;

import java.util.List;

import static com.mzhslab.koreavpn.utils.Constant.decodeSampledBitmapFromResource;

public class ServerCountryAdapter extends RecyclerView.Adapter<ServerCountryAdapter.RecentViewHolder> implements OnItemClickAdapter {
    private final Context context;

    private List<Parent_Server_Adapter> locationList;
    private String selected;
    OnItemClickAdapter listener;
    Country_Flag_Picker picker;
    Country_Names countries;

    public ServerCountryAdapter(Context context, List<Parent_Server_Adapter> locationList, Country_Flag_Picker picker) {

        this.context = context;
        this.locationList = locationList;
        this.picker = picker;

        Log.d("locationList", "" + locationList.size());
    }

   public void setItemClick(OnItemClickAdapter onclick){
       listener=onclick;
    }
    @Override
    public @NonNull RecentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loc_item, viewGroup, false);
        return new RecentViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecentViewHolder viewHolder, int position) {
        Parent_Server_Adapter parentServerAdapter = locationList.get(position);
        viewHolder.txtLocationName.setText(parentServerAdapter.getCountry_name());
        if (parentServerAdapter.getPing_val() <= 300) {
            //less then 300
            viewHolder.latency_country.setTextColor(context.getResources().getColor(R.color.connected));
            viewHolder.latency_country.setText(""+parentServerAdapter.getPing_val()+ " ms");
        } else if (parentServerAdapter.getPing_val()> 300 && parentServerAdapter.getPing_val() < 600) {
            //greater then 300 & less then 600
            viewHolder.latency_country.setTextColor(context.getResources().getColor(R.color.blue));
            viewHolder.latency_country.setText(""+parentServerAdapter.getPing_val()+ " ms");
        } else if (parentServerAdapter.getPing_val() > 600 && parentServerAdapter.getPing_val()< 1000) {
            //greater then 300 & less then 600
            viewHolder.latency_country.setTextColor(context.getResources().getColor(R.color.yellow));
            viewHolder.latency_country.setText(""+parentServerAdapter.getPing_val()+ " ms");
        } else {
            viewHolder.latency_country.setTextColor(context.getResources().getColor(R.color.sample_progress_primary));
            viewHolder.latency_country.setText(""+parentServerAdapter.getPing_val()+ " ms");
        }

//        viewHolder.usage_country.setText("" + parentServerAdapter.getCapacity() + " %");
        countries = picker.getCountryByName(parentServerAdapter.getCountry_name());

        if (countries == null) {
            countries = picker.getCountryByName("United States");
        }


        Server_City_Adapter myAdapter = new Server_City_Adapter(context, parentServerAdapter.citiesList, parentServerAdapter, parentServerAdapter.getCountry_name(), countries);
//        viewHolder.citiesListRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        viewHolder.citiesListRecyclerview.setLayoutManager(layoutManager);
        viewHolder.citiesListRecyclerview.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(viewHolder.citiesListRecyclerview, false);
        listener=this;
        myAdapter.setOnClick(listener);
        viewHolder.citiesListRecyclerview.setAdapter(myAdapter);

        if (countries != null) {
            viewHolder.ivCountryFlag.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), countries.getFlag(), 50, 50));
        }
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onResult(String countryName) {

    }


    static class RecentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtLocationName, latency_country;
        public AppCompatImageView ivCountryFlag, mToggleIcon;
        LinearLayout parent_items;
        RecyclerView citiesListRecyclerview;

        RecentViewHolder(View itemView) {
            super(itemView);
            txtLocationName = itemView.findViewById(R.id.country_name);
//            usage_country = itemView.findViewById(R.id.usage);
            latency_country = itemView.findViewById(R.id.latency);

            ivCountryFlag = itemView.findViewById(R.id.flag);
            parent_items = itemView.findViewById(R.id.parent_items);
            mToggleIcon = itemView.findViewById(R.id.toggleIcon);
            citiesListRecyclerview = itemView.findViewById(R.id.cities_list_recyclerView);
            parent_items.setOnClickListener(this);
        }

        //0--> visible  8>> gone
        public void showChildRecyclerView(View view) {

            mToggleIcon.setRotation(180);

            view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final int targetHeight = view.getMeasuredHeight();
            view.getLayoutParams().height = 0;
            view.setVisibility(View.VISIBLE);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(targetHeight);
            valueAnimator.addUpdateListener(animation -> {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
            });
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.start();


        }

        public void hideChildRecyclerView(View view) {

            mToggleIcon.setRotation(0);

            final int initialHeight = view.getMeasuredHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofInt(initialHeight, 0);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(animation -> {
                view.getLayoutParams().height = (int) animation.getAnimatedValue();
                view.requestLayout();
                if ((int) animation.getAnimatedValue() == 0)
                    view.setVisibility(View.GONE);
            });
            valueAnimator.setInterpolator(new AccelerateInterpolator());
            valueAnimator.setDuration(200);
            valueAnimator.start();
        }

        @Override
        public void onClick(View view) {
            Log.d("parent_clicked", "parent_clicked" + "  " + citiesListRecyclerview.getVisibility());

            if (citiesListRecyclerview.getVisibility() == View.VISIBLE)
                hideChildRecyclerView(citiesListRecyclerview);
            else
                showChildRecyclerView(citiesListRecyclerview);
        }
    }
}


