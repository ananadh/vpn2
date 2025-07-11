package com.mzhslab.koreavpn.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.mzhslab.koreavpn.utils.Constant.ADMOB_INTERSETIAL_AD;
import static com.mzhslab.koreavpn.utils.Constant.ADMOB_NATIVE_TESTING;
import static com.mzhslab.koreavpn.utils.Constant.IS_RUN;
import static com.mzhslab.koreavpn.utils.Constant.getBestServer_after_calculation;
import static com.mzhslab.koreavpn.utils.Constant.isNetworkAvailable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.mzhslab.koreavpn.Activity.Connection_Module.Connect;
import com.mzhslab.koreavpn.Proxy.Proxy_Apps_Screen;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.Server_Module.Server_Activity;
import com.mzhslab.koreavpn.utils.Constant;
import com.mzhslab.koreavpn.vpnhelper.CountryPrefs;
import com.mzhslab.koreavpn.vpnhelper.VPNServerHelper;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.usa_vpn.country_picker.Country_Flag_Picker;
import com.usa_vpn.country_picker.Country_Names;
import com.usa_vpn.country_picker.listeners.Country_Picker_Listener_Interface;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import de.blinkt.openvpn.core.ConnectionStatus;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.ProfileManager;
import de.blinkt.openvpn.core.VpnStatus;


public class Frag_Home_Activity extends Fragment implements VpnStatus.StateListener, Country_Picker_Listener_Interface {

    public TextView taplocation;
    public Country_Flag_Picker countryFlagPicker;
    LinearLayout proxy_apps;
    public AppCompatImageView cv_location;
    public boolean isConnected = false;
    public boolean isConnectedForGoogleAd = true;
    public String connectedCountry;
    SharedPreferences sharedpreferences;
    public Handler handler = new Handler();
    Typeface type_Face1, type_Face2;
    protected IActivityEnabledListener aeListener;
    Dialog disconnect_dialog;
    TextView txtCountryName;
    ConstraintLayout btnDisConnect, connectNew;
    AppCompatImageView ivConnectionStatusCr, ivConnectionStatusIc;
    public Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isConnected) {
                vpnServerHelper.disconnectFromVpn();

            }
        }
    };

    public VPNServerHelper vpnServerHelper;
    public CountryPrefs countryPrefs;
    ConstraintLayout connectButton;
    ImageView img_connected, img_disconnected, connecting_vpn;
    ProgressBar connecting_progress;
    public InterstitialAd mInterstitialAd;
    AdRequest adRequest;
    View view;
    NativeAd native_refesh;
    public AdView adView;
    RelativeLayout startAppBanner_admob;
    StartAppAd startAppAd;
    protected OpenVPNService openvpn_service;
    ServiceConnection service_connection;
    public StartAppNativeAd startAppNativeAd;
    TextView textConnected;
    LottieAnimationView animationView;

    @Override
    public void onStart() {
        if (isAdded()) {
            LocalBroadcastManager.getInstance(requireActivity()).registerReceiver(server_model, new IntentFilter("server_model"));
            super.onStart();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        load_ovpn_service();
        setHasOptionsMenu(true);
    }


    public BroadcastReceiver server_model = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            getAvailableActivity(activity -> {
                if (activity != null) {
                    if (IS_RUN) {
                        if (getBestServer_after_calculation(sharedpreferences) != null) {
                            if (!Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getConfig().isEmpty()) {
                                byte[] data = Base64.decode(Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getConfig(), Base64.DEFAULT);
                                String config_server = new String(data, StandardCharsets.UTF_8);

                                if (!config_server.isEmpty()) {
                                    new Handler().postDelayed(() -> {
                                        String user_name = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getUsername();
                                        String password = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getPassword();

                                        connecttovpn(Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getCountry_name(), config_server, user_name, password);
                                        SharedPreferences sharedpreferences_updated = activity.getSharedPreferences("VPN", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences_updated.edit();

                                        if (sharedpreferences_updated.contains("server_selected_name")) {
                                            editor.remove("server_selected_name").apply();
                                        }
                                        Log.d("server_model_cache3", "   " + user_name + "     " + password);
                                        editor.putString("server_selected_name", Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getCountry_name()).apply();
                                    }, 500);
                                }
                            }
                        }
                    }
                }
            });
            IS_RUN = false;
        }
    };


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dummy, container, false);

        initView(view);

        vpnServerHelper = new VPNServerHelper(getContext());
        countryPrefs = new CountryPrefs(requireActivity());

        startAppBanner_admob = view.findViewById(R.id.native_ads);

        load_native_admob();
        load_native_admob_refresh();

        if (mInterstitialAd == null)
            loadIntertitial();
        return view;
    }

    public void load_ovpn_service() {
        service_connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                OpenVPNService.LocalBinder binder = (OpenVPNService.LocalBinder) service;
                openvpn_service = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        getAvailableActivity(activity -> {
            if (activity != null) {
                Intent intentss = new Intent(activity, OpenVPNService.class);
                intentss.setAction(OpenVPNService.START_SERVICE);
                activity.bindService(intentss, service_connection, Context.BIND_AUTO_CREATE);
            }
        });
    }


    private void load_start_app_intersetial() {
        getAvailableActivity(activity -> {
            if (activity != null) {
                startAppAd = new StartAppAd(activity);
                startAppAd.loadAd(new AdEventListener() {
                    @Override
                    public void onReceiveAd(com.startapp.sdk.adsbase.Ad ad) {

                    }

                    @Override
                    public void onFailedToReceiveAd(com.startapp.sdk.adsbase.Ad ad) {

                    }
                });
            }
        });


    }

    private void load_native_admob() {
        getAvailableActivity(activity -> {
            if (activity != null) {
                if (Constant.ADMOB_BANNER_AD != null && !Constant.ADMOB_BANNER_AD.isEmpty()) {
                    View adContainer = view.findViewById(R.id.native_ads);
                    AdView mAdView = new AdView(activity);
                    mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                    mAdView.setAdUnitId(Constant.ADMOB_BANNER_AD);
                    ((RelativeLayout) adContainer).addView(mAdView);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);

                    mAdView.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            // Code to be executed when an ad finishes loading.
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Code to be executed when an ad request fails.
                            //if (ad_type.equals("1")){
                            load_banner_start_app();
                            // }}
                        }

                        @Override
                        public void onAdOpened() {
                            // Code to be executed when an ad opens an overlay that
                            // covers the screen.
                        }

                        @Override
                        public void onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        @Override
                        public void onAdClosed() {
                            // Code to be executed when the user is about to return
                            // to the app after tapping on an ad.
                        }
                    });

                    mAdView.loadAd(adRequest);
                }
            }
        });
    }

    private void load_native_admob_refresh() {
        getAvailableActivity(activity -> {
            if (activity != null) {
                final AdLoader adLoader = new AdLoader.Builder(activity, ADMOB_NATIVE_TESTING)
                        .forNativeAd(ad -> {
                            native_refesh = ad;
                        })
                        .withAdListener(new AdListener() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                            }
                        }).build();
                adLoader.loadAds(new AdRequest.Builder().build(), 3);
            }
        });
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
        // The headline is guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
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


    private void load_banner_start_app() {
        show_banner();
    }

    private void show_banner() {
        getAvailableActivity(activity -> {
            if (activity != null) {
                RelativeLayout.LayoutParams bannerParameters = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                Banner startAppBanner = new Banner(activity);
                if (startAppBanner_admob != null) {
                    if (Constant.Startad_enable.equals("1")) ;
                    {
                        startAppBanner_admob.removeAllViews();
                        startAppBanner_admob.addView(startAppBanner, bannerParameters);
                    }
                }
            }
        });
    }


    private void initView(View view) {

        getAvailableActivity(activity -> {
            if (activity != null) {
                sharedpreferences = activity.getSharedPreferences("DATA", MODE_PRIVATE);

                type_Face1 = Typeface.createFromAsset(activity.getAssets(), "calibri.ttf");
                type_Face2 = Typeface.createFromAsset(activity.getAssets(), "calibri_bold.ttf");

                //txtConnectionStatus = view.findViewById(R.id.txtConnectionStatus);
                txtCountryName = view.findViewById(R.id.txtCountryName);
                connectButton = view.findViewById(R.id.ConstConnect);
                img_connected = view.findViewById(R.id.dummy_2);
                connecting_progress = view.findViewById(R.id.progress_circular);
                img_disconnected = view.findViewById(R.id.dummy);
                connecting_vpn = view.findViewById(R.id.connecting_vpn);
                proxy_apps = view.findViewById(R.id.proxy_apps);
                startAppNativeAd = new StartAppNativeAd(activity);
                //cv_location = view.findViewById(R.id.cardview_location);
                cv_location = view.findViewById(R.id.ivCountry);
                taplocation = view.findViewById(R.id.taplocation);
                btnDisConnect = view.findViewById(R.id.btnDisConnect_);
                connectNew = view.findViewById(R.id.connectNew);
                textConnected = view.findViewById(R.id.textConnected);
                animationView = view.findViewById(R.id.animationView);
                ivConnectionStatusCr = view.findViewById(R.id.ivConnectionStatusCr);
                ivConnectionStatusIc = view.findViewById(R.id.ivConnectionStatusIc);

                proxy_apps.setOnClickListener(view12 -> startActivity(new Intent(activity, Proxy_Apps_Screen.class)));


                btnDisConnect.setOnClickListener(view1 -> {
                    connectVpn();
                });


                connectButton.setOnClickListener(view1 -> {
                    showLocationSelectDialog();
                });
                connectNew.setOnClickListener(view1 -> {
                    showLocationSelectDialog();
                });

                cv_location.setOnClickListener(v -> {
                    Log.d("cv_location", "cv_location");
                    showLocationSelectDialog();
                });

                countryFlagPicker = new Country_Flag_Picker.Builder().with(activity).listener(this).build();
            }
        });


    }

    private void update_sp_values() {
        if (sharedpreferences != null) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            if (sharedpreferences.contains("fetched_ip")) {
                editor.remove("fetched_ip").apply();
            }
            editor.putString("fetched_ip", sharedpreferences.getString("server_based_location", null)).apply();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
        release();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }

        VpnStatus.addStateListener(this);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        removeCallbacks();
        LocalBroadcastManager.getInstance(requireActivity()).unregisterReceiver(server_model);
        release();

        super.onDestroy();
    }

    private void removeCallbacks() {
        try {
            handler.removeCallbacks(runnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void release() {
        VpnStatus.removeStateListener(this);
    }


    private void showLocationSelectDialog() {

        Intent intentServer = new Intent(requireActivity(), Server_Activity.class);
        startActivityForResult(intentServer, 110090);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 110090) {
            txtCountryName.setText(data.getStringExtra("countryName"));
        }
    }

    @Override
    public void updateState(String state, String logmessage, int localizedResId, final ConnectionStatus level, Intent intent) {

        getAvailableActivity(activity -> {
            if (activity != null) {
                activity.runOnUiThread(() -> {
                    String stateMessage = VpnStatus.getLastCleanLogMessage(getContext());
                    updateUI(stateMessage, level);

                });
            }
        });

    }

    @Override
    public void setConnectedVPN(String uuid) {
    }


    @SuppressLint("ResourceAsColor")
    private void updateUI(String stateMessage, ConnectionStatus level) {

        getAvailableActivity(activity -> {
            if (activity != null) {
                if (getBestServer_after_calculation(sharedpreferences) != null) {
                    String selectedLocation = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getCountry_name();

                    Log.d("update_ui_state", "" + stateMessage);

                    if (level == ConnectionStatus.LEVEL_CONNECTED) {
                        connectButton.setEnabled(true);
                        //txtConnectionStatus.setText("Connected");
                        showIntertitial();

                        img_connected.setVisibility(View.GONE);
                        img_disconnected.setVisibility(View.GONE);
                        connecting_vpn.setVisibility(View.GONE);
                        connecting_progress.setVisibility(View.GONE);

                        animationView.setVisibility(View.GONE);
                        ivConnectionStatusIc.setVisibility(View.VISIBLE);

                        ivConnectionStatusIc.setBackgroundResource(R.drawable.vpn_disconnected_icon);
                        ivConnectionStatusCr.setBackgroundResource(R.drawable.connect_circle_red);

                        textConnected.setText("Connected");
                        taplocation.setText(String.format(selectedLocation));

                        if (sharedpreferences != null) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            if (sharedpreferences.contains("fetched_ip")) {
                                editor.remove("fetched_ip").apply();
                            }

                            Log.d("fetched_home", "" + Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getIp_ping());
                            editor.putString("fetched_ip", Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getIp_ping()).apply();
                        }

                        SharedPreferences sharedpreferences = activity.getSharedPreferences("VPN", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        if (sharedpreferences.contains("server_selected_name")) {
                            editor.remove("server_selected_name").apply();
                        }
                        editor.putString("server_selected_name", selectedLocation).apply();

                        cv_location.setClickable(false);
                        if (!isConnectedForGoogleAd)

                            isConnected = true;
                        isConnectedForGoogleAd = true;
                        connectedCountry = stateMessage;

                        if (Constant.SHOW_CONNECT) {
                            Log.d("show_connected", "" + Constant.SHOW_CONNECT);
                            startActivity(new Intent(activity, Connect.class));
                        }
                    } else if (level == ConnectionStatus.LEVEL_NOTCONNECTED) {
                        //txtConnectionStatus.setText(getString(R.string.connect));
                        img_connected.setVisibility(View.GONE);
                        img_disconnected.setVisibility(View.GONE);
                        connecting_vpn.setVisibility(View.GONE);
                        connecting_progress.setVisibility(View.GONE);
                        connectButton.setEnabled(true);
                        taplocation.setText(getString(R.string.tap_region));
                        cv_location.setClickable(true);
                        connectedCountry = null;
                        textConnected.setText("Tap To Connect");
                        animationView.setVisibility(View.GONE);
                        ivConnectionStatusIc.setVisibility(View.VISIBLE);
                        ivConnectionStatusIc.setBackgroundResource(R.drawable.vpn_connected_icon);
                        ivConnectionStatusCr.setBackgroundResource(R.drawable.connect_circle);

                    } else {
                        connectButton.setEnabled(true);
                        //txtConnectionStatus.setText("Connecting ........");
                        img_connected.setVisibility(View.GONE);
                        connecting_progress.setVisibility(View.GONE);
                        img_disconnected.setVisibility(View.GONE);
                        connecting_vpn.setVisibility(View.GONE);
                        ivConnectionStatusIc.setVisibility(View.GONE);
                        animationView.setVisibility(View.VISIBLE);
                        connectedCountry = stateMessage;
                        textConnected.setText("Connecting ...");
                    }
                }
            }
        });
    }

    private void loadIntertitial() {
        getAvailableActivity(activity -> {
            adRequest = new AdRequest.Builder().build();
            if (ADMOB_INTERSETIAL_AD != null && !ADMOB_INTERSETIAL_AD.isEmpty()) {
                InterstitialAd.load(activity, ADMOB_INTERSETIAL_AD, adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = null;
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i("TAG", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
            } else {
                if (Constant.Startad_enable.equals("1")) ;
                {
                    load_start_app_intersetial();
                }
            }
        });
    }

    private void showIntertitial() {
        getAvailableActivity(activity -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(activity);
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                        loadIntertitial();
                        Log.d("TAG", "The ad was dismissed.");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            } else {
                if (Constant.Startad_enable.equals("1")) {
                    if (startAppAd != null && startAppAd.isReady()) {
                        startAppAd.showAd(new AdDisplayListener() {
                            @Override
                            public void adHidden(com.startapp.sdk.adsbase.Ad ad) {
                                load_start_app_intersetial();
                            }

                            @Override
                            public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {
                            }

                            @Override
                            public void adClicked(com.startapp.sdk.adsbase.Ad ad) {

                            }

                            @Override
                            public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {
//                                load_start_app_intersetial();
                            }

                        });
                    }
                }
            }
        });

    }

    private void connecttovpn(final String location, final String locationFileName, String username, String password) {
        countryPrefs.setSelectedLocation(location);
        VPNConnect(locationFileName, username, password);
    }


    protected interface IActivityEnabledListener {
        void onActivityEnabled(FragmentActivity activity);
    }

    protected void getAvailableActivity(IActivityEnabledListener listener) {
        if (getActivity() == null) {
            aeListener = listener;

        } else {
            listener.onActivityEnabled(getActivity());
        }
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);

        if (aeListener != null) {
            aeListener.onActivityEnabled((FragmentActivity) activity);
            aeListener = null;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (aeListener != null) {
            aeListener.onActivityEnabled((FragmentActivity) context);
            aeListener = null;
        }
    }


    private void VPNConnect(final String locationFileName, String user_name, String password) {

        getAvailableActivity(activity -> {

            if (activity != null) {
                String server_name = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getCountry_name();
                SharedPreferences sharedpreferences = activity.getSharedPreferences("VPN", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();

                if (sharedpreferences.contains("server_selected_name")) {
                    editor.remove("server_selected_name").apply();
                }
                editor.putString("server_selected_name", server_name).apply();

                vpnServerHelper.connectOrDisconnect(locationFileName, user_name, password);
                Log.e("Vidhu", "Location servername- TRUE------------" + locationFileName + "  " + user_name + "  " + password);
            }
        });
    }


    @Override
    public void onSelectCountry(Country_Names countryNames) {

    }

    public void connectVpn() {
        Constant.SHOW_CONNECT = true;
        Context activity = getActivity();
        boolean isActive = VpnStatus.isVPNActive();
        if (isNetworkAvailable(activity)) {
            if (!isActive) {

                isConnectedForGoogleAd = false;
                if (getBestServer_after_calculation(sharedpreferences) != null) {
                    if (!Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getConfig().isEmpty()) {
                        byte[] data = Base64.decode(Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getConfig(), Base64.DEFAULT);
                        String config = new String(data, StandardCharsets.UTF_8);

                        new Handler().postDelayed(() -> {
                            String user_name = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getUsername();
                            String password = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getPassword();
                            VPNConnect(config, user_name, password);
                        }, 500);
                    }
                }

                String server_name = Objects.requireNonNull(getBestServer_after_calculation(sharedpreferences)).getCountry_name();
                SharedPreferences sharedpreferences = activity.getSharedPreferences("VPN", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                if (sharedpreferences.contains("server_selected_name")) {
                    editor.remove("server_selected_name").apply();
                }
                editor.putString("server_selected_name", server_name).apply();
            } else {

                getAvailableActivity(activity1 -> {

                    disconnect_dialog = new Dialog(activity1);
                    disconnect_dialog.setContentView(R.layout.sample_dialog);

                    Button btn_disconnect = disconnect_dialog.findViewById(R.id.btn_disconnect);
                    Button btn_cancel = disconnect_dialog.findViewById(R.id.btn_cancel);

                    FrameLayout frameLayout = disconnect_dialog.findViewById(R.id.disconnect_native);
                    NativeAdView adView = (NativeAdView) getLayoutInflater().inflate(R.layout.native_ad, null);
                    if (native_refesh != null) {
                        frameLayout.setVisibility(View.VISIBLE);
                        populateNativeAdView(native_refesh, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    } else {
                        frameLayout.setVisibility(View.GONE);
                    }

                    btn_disconnect.setOnClickListener(view2 -> {
                        ProfileManager.setConntectedVpnProfileDisconnected(activity);

                        if (openvpn_service != null && openvpn_service.getManagement() != null) {
                            ProfileManager.setConntectedVpnProfileDisconnected(activity);
                            openvpn_service.getManagement().stopVPN(false);
                            update_sp_values();
                            showIntertitial();
                        }
                        disconnect_dialog.dismiss();
                    });

                    btn_cancel.setOnClickListener(view2 -> {
                        disconnect_dialog.dismiss();
                    });

                    disconnect_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    disconnect_dialog.setCancelable(false);
                });
                disconnect_dialog.show();
                Window window = disconnect_dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
        } else {
            Toast.makeText(getActivity(), "Network is required..!!", Toast.LENGTH_SHORT).show();
        }
    }

    interface onclick {
        void onItemClick(String value);
    }
}























