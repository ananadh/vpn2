package com.mzhslab.koreavpn.Activity.Home_Module;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.mzhslab.koreavpn.Activity.Location_Feature.Location_Screen;
import com.mzhslab.koreavpn.Activity.Menu_Module.Menu_Activity;
import com.mzhslab.koreavpn.Proxy.Proxy_Apps_Screen;
import com.mzhslab.koreavpn.R;

import static com.mzhslab.koreavpn.utils.Constant.set_status_bar;


public class Home_Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    ImageView location_button, menu_btn, apps_btn;
    private AlertDialog RateAppDialog, AboutDialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);

        set_status_bar(Home_Activity.this);

        toolbar = findViewById(R.id.toolbar);

        location_button = toolbar.findViewById(R.id.location_button);
        apps_btn = toolbar.findViewById(R.id.apps_button);
        menu_btn = toolbar.findViewById(R.id.menu_btn);

        location_button.setOnClickListener(view -> startActivity(new Intent(Home_Activity.this , Location_Screen.class)));
        apps_btn.setOnClickListener(view -> startActivity(new Intent(Home_Activity.this , Proxy_Apps_Screen.class)));
        menu_btn.setOnClickListener(view -> startActivity(new Intent(Home_Activity.this , Menu_Activity.class)));

    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finishAffinity();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

}
