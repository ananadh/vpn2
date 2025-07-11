package com.usa_vpn.country_picker.listeners;

import android.view.View;

public interface DialogInteractionListener {

  void initiateUi(View view);

  void setCustomStyle(View view);

  void setSearchEditText();

  void setupRecyclerView(View view);
}
