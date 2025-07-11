package com.mzhslab.koreavpn.Proxy;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mzhslab.koreavpn.R;
import com.mzhslab.koreavpn.adapter.proxy_adapter;
import com.mzhslab.koreavpn.utils.Apps_Packages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.mzhslab.koreavpn.utils.Apps_Packages.getPref;
import static com.mzhslab.koreavpn.utils.Apps_Packages.getUnSelectedPackageNamesFromString;

public class Proxy_Apps_Screen extends AppCompatActivity {

    ImageView proxy_apps_back_pressed;
    private proxy_adapter SmartProxyActivity_adapter;
    public static CheckBox checkbox_checked_all;
    HashSet<String> unselectedApplication;
    String unTickedApps;
    Dialog dialog;
    ProgressBar progress;

    public static final String PREF_ENABLED_ALL = "PREF_ENABLED_ALL";
    public static final String TICKED_APP_PACKAGES = "TICKED_APP_PACKAGES";
    public static final String UNTICKED_APP_PACKAGES = "UNTICKED_APP_PACKAGES";
    public static final String PREF_EXCLUDE_THIS_APP = "PREF_EXCLUDE_THIS_APP";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.proxy_apps);

        dialog = new Dialog(Proxy_Apps_Screen.this);
        dialog.setContentView(R.layout.getting_all_proxy_apps);
        progress = dialog.findViewById(R.id.imageDialog);
        progress.setVisibility(View.VISIBLE);
        dialog.setCancelable(false);

        if (!(isFinishing())) {
            dialog.show();
        }

        AppLoadTask task = new AppLoadTask(getApplicationContext());
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        runOnUiThread(() -> {

        });

        checkbox_checked_all = findViewById(R.id.chkAll);
        proxy_apps_back_pressed = findViewById(R.id.proxy_apps_back_pressed);

        proxy_apps_back_pressed.setOnClickListener(view -> onBackPressed());


        checkbox_checked_all.setOnCheckedChangeListener((compoundButton, b) -> {

            if (b) {
                Apps_Packages.putPref(PREF_ENABLED_ALL, true, getApplicationContext());

                unTickedApps = getPref(UNTICKED_APP_PACKAGES, "", Proxy_Apps_Screen.this);
                unselectedApplication = getUnSelectedPackageNamesFromString(unTickedApps);

                SmartProxyActivity_adapter.notifyDataSetChanged();
            } else {
                Apps_Packages.putPref(PREF_ENABLED_ALL, false, getApplicationContext());
                SmartProxyActivity_adapter.notifyDataSetChanged();
            }
        });


        RecyclerView proxy_recyclerView = findViewById(R.id.proxy_recyclerView);
        LinearLayoutManager proxy_layoutManager = new LinearLayoutManager(getApplicationContext());
        proxy_recyclerView.setLayoutManager(proxy_layoutManager);
        proxy_recyclerView.addItemDecoration(new DividerItemDecorations(Proxy_Apps_Screen.this));
        SmartProxyActivity_adapter = new proxy_adapter(getApplicationContext(), null,dialog, progress);
        proxy_recyclerView.setAdapter(SmartProxyActivity_adapter);
        loadSettings();
    }


    @Override
    protected void onResume() {

        super.onResume();
    }


    private void loadSettings() {

        boolean enableAllApps = getPref(PREF_ENABLED_ALL, true, getApplicationContext());

        if (enableAllApps) {
            checkbox_checked_all.setChecked(enableAllApps);
        }


        String tickedApps = getPref(TICKED_APP_PACKAGES, "", getApplicationContext());
        if (tickedApps.equals("")) return;

        SmartProxyActivity_adapter.setCheckedBoxes(Apps_Packages.getPackageNamesFromString(tickedApps));
    }

    public static class DividerItemDecorations extends RecyclerView.ItemDecoration {
        private final Drawable mDivider;

        public DividerItemDecorations(Context context) {

            mDivider = context.getResources().getDrawable((R.drawable.recyclerview_list_divider));
        }

        @Override
        public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {

            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }


    private class AppLoadTask extends AsyncTask<Void, Integer, List<ApplicationInfo>> {
        private Context context;


        AppLoadTask(Context context) {

            this.context = context;
        }


        @Override
        protected List<ApplicationInfo> doInBackground(Void... voids) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

            return loadAppList(context);
        }


        @Override
        protected void onPostExecute(List<ApplicationInfo> applicationInfos) {

            super.onPostExecute(applicationInfos);
            SmartProxyActivity_adapter.setDataSet(applicationInfos);
            SmartProxyActivity_adapter.notifyDataSetChanged();
        }
    }


    public List<ApplicationInfo> loadAppList(Context context) {

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> installedPackages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        int androidSystemUid = 0;
        ApplicationInfo system;
        final List<ApplicationInfo> apps = new ArrayList<>();

        try {

            system = pm.getApplicationInfo("android", PackageManager.GET_META_DATA);
            androidSystemUid = system.uid;


            apps.add(system);
        } catch (PackageManager.NameNotFoundException ignored) {

        }

        for (ApplicationInfo app : installedPackages) {
            if (pm.checkPermission(Manifest.permission.INTERNET, app.packageName) == PackageManager.PERMISSION_GRANTED && app.uid != androidSystemUid) {
                apps.add(app);
            }
        }

        Collections.sort(apps, new ApplicationInfo.DisplayNameComparator(pm));
        return apps;
    }
}
