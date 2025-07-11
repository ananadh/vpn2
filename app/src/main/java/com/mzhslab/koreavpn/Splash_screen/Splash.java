package com.mzhslab.koreavpn.Splash_screen;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.mzhslab.koreavpn.Activity.Home_Module.Home_Activity;
import com.mzhslab.koreavpn.Services.Api_Fetch_Service.Fetch_Service;
import com.mzhslab.koreavpn.Activity.Terms_Conditions_Feature.Terms_Conditions_Screen;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.utils.roundedprogress.indeterminate.IndeterminateRoundCornerProgressBar;
import com.mzhslab.koreavpn.utils.Constant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static com.mzhslab.koreavpn.utils.Constant.isOven_Vpn_ConnectionActive;
import static com.mzhslab.koreavpn.utils.Constant.set_status_bar_t;


public class Splash extends AppCompatActivity {

  Dialog dialog_not_fetch;
  private IndeterminateRoundCornerProgressBar mProgress;
  String local_ip;
  SharedPreferences splash_pref;
  boolean should_move_to_home;

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);

    splash_pref = getSharedPreferences("DATA", MODE_PRIVATE);
    Constant.IS_RUN = false;
    Constant.SHOW_CONNECT = false;

    set_status_bar_t(Splash.this);
    init();
  }

  private void init() {
    mProgress = findViewById(R.id.progressBarIndeterminate2);
    run_next();
  }


  private void run_next() {
    if (isOven_Vpn_ConnectionActive()) {
      move_to_next_Screen(true);
    } else {
      new local_ip_address().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
  }

  public class local_ip_address extends AsyncTask<Void, Void, String> {


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected String doInBackground(Void... params) {

      Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

      try {
        URL whatismyip = new URL("http://checkip.amazonaws.com");

        BufferedReader in = null;
        try {
          try {
            in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            local_ip = null;

            local_ip = in.readLine();
          } catch (IOException e) {
            e.printStackTrace();
          }
        } finally {
          if (in != null) {
            try {
              in.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }

      return local_ip;
    }

    protected void onPostExecute(String pingSpeed) {

      if (local_ip != null) {
        if (splash_pref != null) {
          SharedPreferences.Editor editor = splash_pref.edit();
          if (splash_pref.contains("fetched_ip") && splash_pref.contains("fetched_ip")) {
            editor.remove("fetched_ip").apply();
          }
          editor.putString("fetched_ip", local_ip).apply();
        }
        start_service(local_ip);
      }

    }
  }


  private void move_to_next_Screen(boolean is_connected) {
    if (is_connected) {
      new Handler().postDelayed(() -> {
        Intent intent = new Intent(Splash.this, Home_Activity.class);
        startActivity(intent);
      }, 3000);
    } else {
      startApp();
    }
  }

  private void start_service(String local_ip) {
    LocalBroadcastManager.getInstance(Splash.this).registerReceiver(data_fetch, new IntentFilter("data_fetched"));
    try {
      Intent serviceIntent = new Intent(Splash.this, Fetch_Service.class).putExtra("my_local_ip", local_ip);
      startService(serviceIntent);
    } catch (IllegalStateException e) {
      e.printStackTrace();
    }
  }


  public BroadcastReceiver data_fetch = new BroadcastReceiver() {
    @SuppressLint("SetTextI18n")
    @Override
    public void onReceive(Context context, Intent intent) {
      boolean data_fetched = intent.getBooleanExtra("data_fetch", false);

      if (Constant.IS_RUN) {
        Log.d("data_fetch", "" + data_fetch);
        if (data_fetched) {
          move_to_next_Screen(false);
        } else {
          //unable dialog
          dialog_not_fetch = null;
          dialog_not_fetch = new Dialog(Splash.this);
          dialog_not_fetch.requestWindowFeature(Window.FEATURE_NO_TITLE);
          dialog_not_fetch.setContentView(R.layout.no_data_fetch);
          Button retry_btn = dialog_not_fetch.findViewById(R.id.retry_btn);
          dialog_not_fetch.setCancelable(false);

          if (!((Splash.this).isFinishing())) {
            dialog_not_fetch.show();
          }

          retry_btn.setOnClickListener(view -> {

            dialog_not_fetch.dismiss();
            if (data_fetch != null) {
              LocalBroadcastManager.getInstance(Splash.this).unregisterReceiver(data_fetch);
            }

            stopService(new Intent(Splash.this, Fetch_Service.class));
            start_service(local_ip);
          });
        }

        if (mProgress.getVisibility() == View.VISIBLE) {
          mProgress.setVisibility(View.GONE);
        }
      }

      Constant.IS_RUN = false;
    }
  };

  @Override
  protected void onDestroy() {
    if (data_fetch != null) {
      LocalBroadcastManager.getInstance(this).unregisterReceiver(data_fetch);
    }
    super.onDestroy();
  }

  private void startApp() {
    if (splash_pref != null) {
      if (splash_pref.contains("isLogin")) {
        should_move_to_home = splash_pref.getBoolean("isLogin", false);
        Log.d("isLogin_data", "" + should_move_to_home);

        Intent intent;
        if (should_move_to_home) {
          intent = new Intent(Splash.this, Home_Activity.class);
        } else {
          intent = new Intent(Splash.this, Terms_Conditions_Screen.class);
        }
        startActivity(intent);
      } else {
        Intent intent = new Intent(Splash.this, Terms_Conditions_Screen.class);

        startActivity(intent);
      }
    }

  }
}

