package com.mzhslab.koreavpn.Activity.Location_Feature;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.utils.Location_Google_Url;

import org.json.JSONException;
import org.json.JSONObject;

import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;


public class Location_Screen extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {
    protected GoogleMap google_map;
    LocationGooglePlayServicesWithFallbackProvider provider;
    ImageView backbtn_location;
    TextView lat_value, long_value, my_ip;
    LatLng latlong;
    SharedPreferences Location_sharedPreference;
    Dialog dialog;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_feature);

        Location_sharedPreference = getSharedPreferences("DATA", MODE_PRIVATE);

        dialog = new Dialog(Location_Screen.this);

        backbtn_location = findViewById(R.id.backbtn_location);
        backbtn_location.setOnClickListener(view -> onBackPressed());

        lat_value = findViewById(R.id.lat_value);
        long_value = findViewById(R.id.long_value);

        my_ip = findViewById(R.id.my_ip);


        if (Location_sharedPreference.contains("fetched_ip")) {
            my_ip.setText(Location_sharedPreference.getString("fetched_ip", ""));

            provider = new LocationGooglePlayServicesWithFallbackProvider(Location_Screen.this);

            SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
            if (supportMapFragment != null) {
                supportMapFragment.getMapAsync(Location_Screen.this);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (google_map != null) {
            google_map.clear();
        }
        Log.d("onmap_ready", "onmap");
        google_map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        zoomToCurrentPosition();
    }

    private void zoomToCurrentPosition() {

        final Dialog dialog = new Dialog(Location_Screen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.get_location_progress);
        final ProgressBar progress = dialog.findViewById(R.id.imageDialog);
        progress.setVisibility(View.VISIBLE);
        dialog.setCancelable(true);

        if (!isFinishing()) {
            dialog.show();
        }

        Log.d("zoomToCurrent", "" + my_ip);
        RequestQueue queue = Volley.newRequestQueue(Location_Screen.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Location_Google_Url.GEO_API_URI + Location_sharedPreference.getString("fetched_ip", ""), response ->
        {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String lat = jsonObject.getString("lat");
                String longitude = jsonObject.getString("lon");
                if (!lat.isEmpty()) {

                    latlong = new LatLng(Double.parseDouble(lat), Double.parseDouble(longitude));
                    google_map.moveCamera(CameraUpdateFactory.newLatLng(latlong));
                    google_map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlong, 14));

                    google_map.addCircle(new CircleOptions().center(latlong).radius(200.0).strokeWidth(3f).strokeColor(Color.RED).fillColor(Color.argb(70, 150, 50, 50)));

                    lat_value.setText(lat);
                    long_value.setText(longitude);

                    runOnUiThread(() -> {

                        try {
                            if (!isFinishing() && dialog.isShowing()) {
                                dialog.dismiss();
                                progress.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }

            } catch (JSONException e) {
                if (!isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                    progress.setVisibility(View.INVISIBLE);
                }
            }
        }, error -> {
            try {
                if (!isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                    progress.setVisibility(View.INVISIBLE);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, 2, 2));
        queue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        dismiss_dialog();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        dismiss_dialog();
        super.onStop();
    }

    @Override
    protected void onPause() {
        dismiss_dialog();
        super.onPause();
    }

    private void dismiss_dialog() {
        if (!isFinishing() && dialog != null) {
            if (!dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;

                if (progress != null) {
                    progress.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
