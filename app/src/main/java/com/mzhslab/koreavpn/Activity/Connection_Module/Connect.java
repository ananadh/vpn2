package com.mzhslab.koreavpn.Activity.Connection_Module;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.Services.vpn_connect_time;
import com.usa_vpn.country_picker.Country_Flag_Picker;
import com.usa_vpn.country_picker.Country_Names;
import com.usa_vpn.country_picker.listeners.Country_Picker_Listener_Interface;


import java.util.Objects;

import static com.mzhslab.koreavpn.utils.Constant.ADMOB_NATIVE_TESTING;
import static com.mzhslab.koreavpn.utils.Constant.SHOW_CONNECT;
import static com.mzhslab.koreavpn.utils.Constant.getBestServer_after_calculation;


public class Connect extends AppCompatActivity implements Country_Picker_Listener_Interface {

    String success_connection;
    TextView connection_county_name, connection_download_speed, connection_upload_speed, connection_download_speed_KB, connection_upload_speed_KB;
    Chronometer Duration;
    RelativeLayout rate_lay;
    ImageView toolbar_back_button;
    private final Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    SharedPreferences sharedPreferences;
    Country_Flag_Picker my_pick2;
    Country_Names countryNamesNames3;
    ImageView image_connected;
    NativeAd nativeAd_connect;
    Runnable r;
    private RelativeLayout connected_native, Banner_start;
    private AdView adView;

    @SuppressLint({"SourceLockedOrientationActivity", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_layout);

        adView = new AdView(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
        load_native();

        show_best_server();

        startService(new Intent(Connect.this, vpn_connect_time.class));

        Duration.setOnChronometerTickListener(cArg -> {
            long time = SystemClock.elapsedRealtime() - cArg.getBase();
            int h = (int) (time / 3600000);
            int m = (int) (time - h * 3600000) / 60000;
            int s = (int) (time - h * 3600000 - m * 60000) / 1000;
            String hh = h < 10 ? "0" + h : h + "";
            String mm = m < 10 ? "0" + m : m + "";
            String ss = s < 10 ? "0" + s : s + "";
            cArg.setText(hh + "h" + ":" + mm + "m" + ":" + ss + "s");
        });
        Duration.start();

        try {
            mHandler.postDelayed(new Runnable() {
                public void run() {

                    long resetdownload = TrafficStats.getTotalRxBytes();
                    long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
                    connection_download_speed_KB.setText(rxBytes + " bytes");


                    if (rxBytes >= 1024) {

                        long rxKb = rxBytes / 1024;

                        connection_download_speed_KB.setText(rxKb + " KBs");

                        if (rxKb >= 1024) {

                            long rxMB = rxKb / 1024;
                            connection_download_speed_KB.setText(rxMB + " MBs");

                            if (rxMB >= 1024) {

                                long rxGB = rxMB / 1024;
                                connection_download_speed_KB.setText(rxGB + " GBs");
                            }
                        }
                    }

                    mStartRX = resetdownload;
                    long resetupload = TrafficStats.getTotalTxBytes();
                    long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
                    connection_upload_speed_KB.setText(txBytes + " bytes");

                    if (txBytes >= 1024) {
                        long txKb = txBytes / 1024;

                        connection_upload_speed_KB.setText(txKb + " KBs");
                        if (txKb >= 1024) {

                            long txMB = txKb / 1024;
                            connection_upload_speed_KB.setText(txMB + " MBs");

                            if (txMB >= 1024) {

                                long txGB = txMB / 1024;
                                connection_upload_speed_KB.setText(txGB + " GBs");
                            }
                        }
                    }

                    mStartTX = resetupload;
                    mHandler.postDelayed(this, 1000);
                }
            }, 1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        toolbar_back_button.setOnClickListener(view -> {
            onBackPressed();
        });

        rate_lay.setOnClickListener(view -> {
            Intent UriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(UriIntent);
        });
    }


    private void load_native() {

        final AdLoader adLoader = new AdLoader.Builder(Connect.this, ADMOB_NATIVE_TESTING)
                .forNativeAd(ad -> {

                    nativeAd_connect = ad;
                    connected_native = findViewById(R.id.banner_start);
                    NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad, null);
                    populateNativeAdView(nativeAd_connect, adView);
                    connected_native.removeAllViews();
                    connected_native.addView(adView);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                    }
                }).build();
        adLoader.loadAds(new AdRequest.Builder().build(), 3);
    }


    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);
        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null || nativeAd.getStarRating() < 3) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
    }


    private void show_best_server() {

        if (getBestServer_after_calculation(sharedPreferences) != null) {
            success_connection = Objects.requireNonNull(getBestServer_after_calculation(sharedPreferences)).getCountry_name();
            if (success_connection != null) {
                my_pick2 = new Country_Flag_Picker.Builder().with(Connect.this).listener(Connect.this).build();

                if (success_connection.equals("Germany")) {
                    image_connected.setImageResource(0);
                    image_connected.setImageResource(R.drawable.flag_de);
                    connection_county_name.setText(success_connection);
                } else {

                    if (my_pick2 != null) {
                        countryNamesNames3 = my_pick2.getCountryByName(success_connection);
                        if (countryNamesNames3 != null) {
                            image_connected.setImageResource(0);
                            image_connected.setImageResource(countryNamesNames3.getFlag());
                            connection_county_name.setText(success_connection);
                        }
                    }
                }
            }
        }

    }

    private void init() {
        image_connected = findViewById(R.id.image_connected);
        Duration = findViewById(R.id.Duration);
        connection_county_name = findViewById(R.id.connection_county_name);
        rate_lay = findViewById(R.id.rate);
        toolbar_back_button = findViewById(R.id.toolbar_back_button);
        connection_download_speed = findViewById(R.id.connection_download_speed);
        connection_upload_speed = findViewById(R.id.connection_upload_speed);
        connection_download_speed_KB = findViewById(R.id.connection_download_speed_KB);
        connection_upload_speed_KB = findViewById(R.id.connection_upload_speed_KB);

        Banner_start = findViewById(R.id.banner_start);

        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        Duration.stop();
        mHandler.removeCallbacksAndMessages(null);
        SHOW_CONNECT = false;
        finish();
        super.onBackPressed();
    }


    @Override
    protected void onDestroy() {
        Duration.stop();
        super.onDestroy();
    }


    @Override
    public void onSelectCountry(Country_Names countryNamesNames) {

    }
}
