package com.mzhslab.koreavpn.Activity.Terms_Conditions_Feature;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mzhslab.koreavpn.Activity.Home_Module.Home_Activity;
import com.mzhslab.koreavpn.R;


public class Terms_Conditions_Screen extends AppCompatActivity {

    Button Accept_btn, Exit_btn;
    ImageView Exit_top;
    SharedPreferences terms_Services_sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_conditions_feature);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
        terms_Services_sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);

        Accept_btn = findViewById(R.id.Accept_btn);
        Exit_btn = findViewById(R.id.Exit_btn);
        Exit_top = findViewById(R.id.terms_close);
        setupHyperlink();

        Exit_btn.setOnClickListener(view -> {
            if (terms_Services_sharedPreferences != null) {
                SharedPreferences.Editor editor = terms_Services_sharedPreferences.edit();
                if (editor != null) {
                    editor.putBoolean("isLogin", false).apply();
                    finishAffinity();
                }
            }
        });

        Exit_top.setOnClickListener(view -> {
            if (terms_Services_sharedPreferences != null) {
                SharedPreferences.Editor editor = terms_Services_sharedPreferences.edit();
                if (editor != null) {
                    editor.putBoolean("isLogin", false).apply();
                    finishAffinity();
                }
            }
        });


        Accept_btn.setOnClickListener(view -> {
            if (terms_Services_sharedPreferences != null) {
                SharedPreferences.Editor editor = terms_Services_sharedPreferences.edit();
                if (editor != null) {
                    editor.putBoolean("isLogin", true);
                    editor.apply();
                    startActivity(new Intent(Terms_Conditions_Screen.this, Home_Activity.class));
                }
            }
        });
    }

    private void setupHyperlink() {
        TextView linkTextView = findViewById(R.id.textView3);
        linkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
