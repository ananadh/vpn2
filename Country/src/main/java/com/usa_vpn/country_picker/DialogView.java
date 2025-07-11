package com.usa_vpn.country_picker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mukesh.countrypicker.R;
import com.usa_vpn.country_picker.listeners.DialogInteractionListener;

import static com.usa_vpn.country_picker.Country_Flag_Picker.THEME_NEW;

public class DialogView extends BottomSheetDialogFragment {
  private static final String BUNDLE_KEY = "theme";
  private DialogInteractionListener mlistener;

  public static DialogView newInstance(int theme) {
    DialogView dialogView = new DialogView();
    Bundle args = new Bundle();
    args.putInt(BUNDLE_KEY, theme);
    dialogView.setArguments(args);
    return dialogView;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle args = getArguments();
    if (args != null) {
      int theme = args.getInt(BUNDLE_KEY, 0);
      if (theme == THEME_NEW) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MaterialDialogStyle);
      } else {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
      }
    } else {
      setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.country_picker_layout, container, false);
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mlistener.initiateUi(view);
    mlistener.setCustomStyle(view);
    mlistener.setSearchEditText();
    mlistener.setupRecyclerView(view);
  }

  public void setListener(DialogInteractionListener mlistener) {
    this.mlistener = mlistener;
  }
}
