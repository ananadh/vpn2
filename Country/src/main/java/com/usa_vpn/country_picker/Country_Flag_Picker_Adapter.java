package com.usa_vpn.country_picker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mukesh.countrypicker.R;
import com.usa_vpn.country_picker.listeners.OnItemClickListener;
import java.util.List;

public class Country_Flag_Picker_Adapter extends
    RecyclerView.Adapter<Country_Flag_Picker_Adapter.ViewHolder> {

  // region Variables
  private OnItemClickListener mlistener;
  private List<Country_Names> countries_name;
  private Context mcontext;
  private int textColour;
  // endregion

  //region Constructor
  public Country_Flag_Picker_Adapter(Context mcontext, List<Country_Names> countries_name,
                                     OnItemClickListener mlistener, int textColour) {
    this.mcontext = mcontext;
    this.countries_name = countries_name;
    this.mlistener = mlistener;
    this.textColour = textColour;
  }
  // endregion

  // region Adapter Methods
  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item_layout, parent, false);
    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    final Country_Names countryNames = countries_name.get(position);
    holder.countryName.setText(countryNames.getName());
    holder.countryName.setTextColor(textColour == 0 ? Color.BLACK : textColour);
    countryNames.loadFlagByCode(mcontext);
    if (countryNames.getFlag() != -1) {
      holder.country_Flag_ImageView.setImageResource(countryNames.getFlag());
    }
    holder.parent_view.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mlistener.onItemClicked(countryNames);
      }
    });
  }

  @Override public int getItemCount() {
    return countries_name.size();
  }
  // endregion

  // region ViewHolder
  class ViewHolder extends RecyclerView.ViewHolder {
    private ImageView country_Flag_ImageView;
    private TextView countryName;
    private LinearLayout parent_view;

    ViewHolder(View itemView) {
      super(itemView);
      country_Flag_ImageView = itemView.findViewById(R.id.country_flag);
      countryName = itemView.findViewById(R.id.countryName);
      parent_view = itemView.findViewById(R.id.parent_view);
    }
  }
  // endregion
}
