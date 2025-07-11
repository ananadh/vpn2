package com.mzhslab.koreavpn.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mzhslab.koreavpn.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.mzhslab.koreavpn.Proxy.Proxy_Apps_Screen.PREF_EXCLUDE_THIS_APP;
import static com.mzhslab.koreavpn.Proxy.Proxy_Apps_Screen.TICKED_APP_PACKAGES;
import static com.mzhslab.koreavpn.Proxy.Proxy_Apps_Screen.UNTICKED_APP_PACKAGES;
import static com.mzhslab.koreavpn.Proxy.Proxy_Apps_Screen.checkbox_checked_all;
import static com.mzhslab.koreavpn.utils.Apps_Packages.getPref;
import static com.mzhslab.koreavpn.utils.Apps_Packages.putPref;


public class proxy_adapter extends RecyclerView.Adapter<proxy_adapter.SimpleViewHolder> {

    public static int LOAD_NUMBER = 0;
    private final Context mcontext;
    private List<ApplicationInfo> proxy_dataSet;
    private List<ApplicationInfo> proxy_displaySet;
    private int numOfLoadedApplication = 0;
    private SimpleViewHolder proxy_viewHolder;
    private final PackageManager package_manager;
    private HashSet<String> selectedApps;
    private HashSet<String> unselectedApps;
    Dialog dialog;
    ProgressBar progressBar;
    private String oldStringFilter = "";


    public proxy_adapter(Context mcontext, List<ApplicationInfo> objects, Dialog dialog, ProgressBar progressBar) {

        this.mcontext = mcontext;
        this.proxy_dataSet = objects;
        this.dialog = dialog;
        this.progressBar = progressBar;
        package_manager = mcontext.getPackageManager();
        selectedApps = new HashSet<>();
        unselectedApps = new HashSet<>();
    }

    @Override
    public @NonNull SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allow_apps_item, parent, false);
        proxy_viewHolder = new SimpleViewHolder(view);
        return proxy_viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder viewHolder, int position) {

        if (dialog.isShowing()) {
            dialog.dismiss();
            progressBar.setVisibility(View.INVISIBLE);
        }


        ApplicationInfo item = proxy_displaySet.get(position);
        CharSequence appName = item.loadLabel(package_manager);

        if (checkbox_checked_all.isChecked()) {
            String packageName = "app.witworkvpn.strongikev2";

            if (TextUtils.isEmpty(appName))

                appName = item.packageName;
            viewHolder.appName.setText(appName);
            viewHolder.appIcon.setImageDrawable(item.loadIcon(package_manager));
            viewHolder.checkBox.setTag(item.packageName);
            viewHolder.checkBox.setChecked(true);

            unselectedApps.clear();
            selectedApps.add(packageName);
            saveCheckedBoxes();
        } else {
            if (TextUtils.isEmpty(appName))

                appName = item.packageName;
            viewHolder.appName.setText(appName);
            viewHolder.appIcon.setImageDrawable(item.loadIcon(package_manager));
            viewHolder.checkBox.setChecked(selectedApps.contains(item.packageName));
            viewHolder.checkBox.setTag(item.packageName);
        }
    }

    @Override
    public int getItemCount() {

        if (proxy_displaySet == null) return 0;
        return numOfLoadedApplication;
    }


    public void setCheckedBoxes(HashSet<String> selectedApps) {

        this.selectedApps = selectedApps;
    }

    public void setUnCheckedBoxes(HashSet<String> unselectedApps) {

        this.unselectedApps = unselectedApps;
    }


    public boolean filterData(String stringFilter) {

        if (proxy_dataSet == null) {
            proxy_dataSet = new ArrayList<>();
        }
        oldStringFilter = stringFilter;

        List<ApplicationInfo> tempSet = new ArrayList<>();
        if (stringFilter != null && !stringFilter.equals("")) {
            for (ApplicationInfo dataItem : proxy_dataSet) {
                if (dataItem.loadLabel(package_manager).toString().toLowerCase().contains(stringFilter.toLowerCase())) {
                    tempSet.add(dataItem);
                }
            }
        } else {
            tempSet.addAll(proxy_dataSet);
        }

        boolean excludeThisApp = getPref(PREF_EXCLUDE_THIS_APP, true, mcontext);
        if (excludeThisApp) {
            for (ApplicationInfo dataItem : tempSet) {
                if (dataItem.packageName.equals(com.mzhslab.koreavpn.BuildConfig.APPLICATION_ID)) {
                    tempSet.remove(dataItem);
                    break;
                }
            }
        }

        proxy_displaySet(tempSet);
        return true;
    }


    private void proxy_displaySet(List<ApplicationInfo> tempSet) {

        proxy_displaySet = tempSet;
        if (this.proxy_displaySet == null) {
            numOfLoadedApplication = 0;
            return;
        }
        if (LOAD_NUMBER == 0) {
            numOfLoadedApplication = this.proxy_displaySet.size();
        } else {
            if (this.proxy_displaySet.size() < LOAD_NUMBER) {
                numOfLoadedApplication = this.proxy_displaySet.size();
            } else {
                numOfLoadedApplication = LOAD_NUMBER;
            }
        }
    }


    public void setDataSet(List<ApplicationInfo> proxy_dataSet) {

        this.proxy_dataSet = proxy_dataSet;
        filterData(oldStringFilter);
    }


    private void saveCheckedBoxes() {

        String fullStr = "";
        for (String data : selectedApps) {
            fullStr += data + ",";
        }
        if (fullStr.lastIndexOf(",") > 0) {
            fullStr = fullStr.substring(0, fullStr.length() - 1);
        }
        putPref(TICKED_APP_PACKAGES, fullStr, mcontext);
    }


    private void saveUnCheckedBoxes() {

        StringBuilder fullStr = new StringBuilder();
        for (String data : unselectedApps) {
            fullStr.append(data).append(",");
        }
        if (fullStr.lastIndexOf(",") > 0) {
            fullStr = new StringBuilder(fullStr.substring(0, fullStr.length() - 1));
        }
        putPref(UNTICKED_APP_PACKAGES, fullStr.toString(), mcontext);
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public TextView appName;
        public ImageView appIcon;
        public CheckBox checkBox;


        public SimpleViewHolder(View itemView) {

            super(itemView);
            appName = itemView.findViewById(R.id.app_name);
            appIcon = itemView.findViewById(R.id.app_icon);
            checkBox = itemView.findViewById(R.id.app_selected);


            checkBox.setOnClickListener(view -> {

                String packageName = (String) view.getTag();
                if (packageName == null || packageName.equals("")) return;

                if (checkBox.isChecked()) {

                    if (!selectedApps.contains(packageName)) {
                        selectedApps.add(packageName);
                        saveCheckedBoxes();
                    }

                    if (unselectedApps.contains(packageName)) {
                        unselectedApps.remove(packageName);
                        saveUnCheckedBoxes();
                    }
                } else {

                    if (selectedApps.contains(packageName)) {
                        selectedApps.remove(packageName);
                        saveCheckedBoxes();
                    }

                    if (!unselectedApps.contains(packageName)) {
                        unselectedApps.add(packageName);
                        saveUnCheckedBoxes();
                    }
                }
            });
        }
    }
}
