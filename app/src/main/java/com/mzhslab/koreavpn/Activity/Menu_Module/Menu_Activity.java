package com.mzhslab.koreavpn.Activity.Menu_Module;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mzhslab.koreavpn.R;


public class Menu_Activity extends AppCompatActivity {
    public static LinearLayout privacy;
    public static LinearLayout rate;
    public static LinearLayout aboutus;
    public static LinearLayout contactus;
    public static LinearLayout share;
    SharedPreferences sharedPreferences;
    boolean is_premium;
    ImageView close;
    Dialog privacy_dialog;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ObsoleteSdkInt")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        Window window = this.getWindow();

        sharedPreferences = getSharedPreferences("DATA", MODE_PRIVATE);

        if (sharedPreferences != null) {
            if (sharedPreferences.contains("premium_status")) {
                is_premium = sharedPreferences.getBoolean("premium_status", false);
            } else {
                is_premium = false;
            }
        }

        Log.d("premium_status", "" + is_premium);

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));

        privacy = findViewById(R.id.frame_1);
        rate = findViewById(R.id.frame_2);
        aboutus = findViewById(R.id.frame_3);
        contactus = findViewById(R.id.frame_4);
        share = findViewById(R.id.frame_5);
        close = findViewById(R.id.close);

        close.setOnClickListener(v -> onBackPressed());

        rate.setOnClickListener(v -> {
            Intent UriIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(UriIntent);
        });

        privacy.setOnClickListener(v -> {
            privacy();
        });


        aboutus.setOnClickListener(v -> {

            About_us();

        });


        contactus.setOnClickListener(v -> {

            {

                Intent intent = new Intent(Intent.ACTION_SEND);
                String[] recipients = {"smkokiapps@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback For " + Menu_Activity.this.getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, "______________________________________" + "\n" +
                        "VersionName" + ":" + "  " + Build.VERSION.RELEASE + "\n" +
                        "App Version" + ":" + "  " + "19.0" + "\n" +
                        "Device Brand/Model: " + "  " + Build.MODEL + "\n" +
                        "System Version" + "  " + Build.VERSION.SDK + "\n" +
                        "You can send problems or suggestions to us." + "\n" + "_______________________________________" + "\n" + "");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }


        });


        share.setOnClickListener(v -> {
            Log.d("Share_text", "" + is_premium);

            {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download FastVpn For Android : " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }


    public void About_us() {
        privacy_dialog = new Dialog(this);
        privacy_dialog.setContentView(R.layout.privacy);
        ImageView close_privacy = privacy_dialog.findViewById(R.id.close_privacy);
        TextView privacy_text = privacy_dialog.findViewById(R.id.privacy_text);
        TextView privacy_headline = privacy_dialog.findViewById(R.id.header_title);
        close_privacy.setOnClickListener(v -> {
            privacy_dialog.dismiss();
        });

        //	title_tv.setText("Privacy Policy");
        privacy_text.setText("             Korea VPN  \n\nContact Details: smkokiapps@gmail.com ");
        privacy_headline.setText("About Us");
        //PTDiaglogbox.show();
        privacy_dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        privacy_dialog.show();


        //Window window = privacy_dialog.getWindow();
        //window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    }


    public void privacy() {
        privacy_dialog = new Dialog(this);
        privacy_dialog.setContentView(R.layout.privacy);
        ImageView close_privacy = (ImageView) privacy_dialog.findViewById(R.id.close_privacy);
        TextView privacy_text = (TextView) privacy_dialog.findViewById(R.id.privacy_text);
        close_privacy.setOnClickListener(v -> {
            privacy_dialog.dismiss();
        });

        Spanned sp;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sp = Html.fromHtml("<p><strong>Privacy Policy</strong></p>\n" +
                    "<p>MZH SECURITY LAP Built the Korea VPN Unblock app as a Free app. This SERVICE is provided by MZH SECURITY LAP at no cost and is intended for use as is.</p>\n" +
                    "<p>This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.</p>\n" +
                    "<p>If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy.</p>\n" +
                    "<p>The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at Korea VPN Unblock unless otherwise defined in this Privacy Policy.</p>\n" +
                    "<p><strong>Information Collection and Use</strong></p>\n" +
                    "<p>For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way.</p></p>\n" +
                    "<div>\n" +
                    "<p>The app does use third party services that may collect information used to identify you.</p>\n" +
                    "<p>Link to privacy policy of third party service providers used by the app</p>\n" +
                    "<ul>\n" +
                    "<li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Google Play Services</a></li>\n" +
                    "<li><a href=\"https://support.google.com/admob/answer/6128543?hl=en\" target=\"_blank\" rel=\"noopener noreferrer\">AdMob</a></li>\n" +
                    "<li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\" rel=\"noopener noreferrer\">Google Analytics for Firebase</a></li>\n" +
                    "<li><a href=\"https://firebase.google.com/support/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Firebase Crashlytics</a></li>\n" +
                    "<li><a href=\"https://www.facebook.com/about/privacy/update/printable\" target=\"_blank\" rel=\"noopener noreferrer\">Facebook</a></li>\n" +
                    "<li><a href=\"https://unity3d.com/legal/privacy-policy\" target=\"_blank\" rel=\"noopener noreferrer\">Unity</a></li>\n" +
                    "</ul>\n" +
                    "</div>\n" +
                    "<p><strong>Log Data</strong></p>\n" +
                    "<p>I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (&ldquo;IP&rdquo;) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.</p>\n" +
                    "<p><strong>Cookies</strong></p>\n" +
                    "<p>Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.</p>\n" +
                    "<p>This Service does not use these &ldquo;cookies&rdquo; explicitly. However, the app may use third party code and libraries that use &ldquo;cookies&rdquo; to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.</p>\n" +
                    "<p><strong>Service Providers</strong></p>\n" +
                    "<p>I may employ third-party companies and individuals due to the following reasons:</p>\n" +
                    "<ul>\n" +
                    "<li>To facilitate our Service;</li>\n" +
                    "<li>To provide the Service on our behalf;</li>\n" +
                    "<li>To perform Service-related services; or</li>\n" +
                    "<li>To assist us in analyzing how our Service is used.</li>\n" +
                    "</ul>\n" +
                    "<p>I want to inform users of this Service that these third parties have access to your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.</p>\n" +
                    "<p><strong>Security</strong></p>\n" +
                    "<p>I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.</p>\n" +
                    "<p><strong>Links to Other Sites</strong></p>\n" +
                    "<p>This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.</p>\n" +
                    "<p><strong>Children&rsquo;s Privacy</strong></p>\n" +
                    "<p>These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do necessary actions.</p>\n" +
                    "<p><strong>Changes to This Privacy Policy</strong></p>\n" +
                    "<p>I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page.</p>\n" +
                    "<p>This policy is effective as of 2021-11-03</p>\n" +
                    "<p><strong>Contact Us</strong></p>\n" +
                    "<p>If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at smkokiapps@gmail.com .</p>", Html.FROM_HTML_MODE_COMPACT);
        } else {
            sp = Html.fromHtml("<p><strong>Privacy Policy</strong></p>\n" +
                    "<p>MZH SECURITY LAP built the Korea VPN Unblock app as a Free app. This SERVICE is provided by Super AppsTech at no cost and is intended for use as is.</p>\n" +
                    "<p>This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service.</p>\n" +
                    "<p>If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that I collect is used for providing and improving the Service. I will not use or share your information with anyone except as described in this Privacy Policy.</p>\n" +
                    "<p>The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is accessible at Korea VPN Unblock unless otherwise defined in this Privacy Policy.</p>\n" +
                    "<p><strong>Information Collection and Use</strong></p>\n" +
                    "<p>For a better experience, while using our Service, I may require you to provide us with certain personally identifiable information. The information that I request will be retained on your device and is not collected by me in any way.</p>\n" +
                    "<div>\n" +
                    "<p>The app does use third party services that may collect information used to identify you.</p>\n" +
                    "<p>Link to privacy policy of third party service providers used by the app</p>\n" +
                    "<ul>\n" +
                    "<li><a href=\"https://www.google.com/policies/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Google Play Services</a></li>\n" +
                    "<li><a href=\"https://support.google.com/admob/answer/6128543?hl=en\" target=\"_blank\" rel=\"noopener noreferrer\">AdMob</a></li>\n" +
                    "<li><a href=\"https://firebase.google.com/policies/analytics\" target=\"_blank\" rel=\"noopener noreferrer\">Google Analytics for Firebase</a></li>\n" +
                    "<li><a href=\"https://firebase.google.com/support/privacy/\" target=\"_blank\" rel=\"noopener noreferrer\">Firebase Crashlytics</a></li>\n" +
                    "<li><a href=\"https://www.facebook.com/about/privacy/update/printable\" target=\"_blank\" rel=\"noopener noreferrer\">Facebook</a></li>\n" +
                    "<li><a href=\"https://unity3d.com/legal/privacy-policy\" target=\"_blank\" rel=\"noopener noreferrer\">Unity</a></li>\n" +
                    "</ul>\n" +
                    "</div>\n" +
                    "<p><strong>Log Data</strong></p>\n" +
                    "<p>I want to inform you that whenever you use my Service, in a case of an error in the app I collect data and information (through third party products) on your phone called Log Data. This Log Data may include information such as your device Internet Protocol (&ldquo;IP&rdquo;) address, device name, operating system version, the configuration of the app when utilizing my Service, the time and date of your use of the Service, and other statistics.</p>\n" +
                    "<p><strong>Cookies</strong></p>\n" +
                    "<p>Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers. These are sent to your browser from the websites that you visit and are stored on your device's internal memory.</p>\n" +
                    "<p>This Service does not use these &ldquo;cookies&rdquo; explicitly. However, the app may use third party code and libraries that use &ldquo;cookies&rdquo; to collect information and improve their services. You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device. If you choose to refuse our cookies, you may not be able to use some portions of this Service.</p>\n" +
                    "<p><strong>Service Providers</strong></p>\n" +
                    "<p>I may employ third-party companies and individuals due to the following reasons:</p>\n" +
                    "<ul>\n" +
                    "<li>To facilitate our Service;</li>\n" +
                    "<li>To provide the Service on our behalf;</li>\n" +
                    "<li>To perform Service-related services; or</li>\n" +
                    "<li>To assist us in analyzing how our Service is used.</li>\n" +
                    "</ul>\n" +
                    "<p>I want to inform users of this Service that these third parties have access to your Personal Information. The reason is to perform the tasks assigned to them on our behalf. However, they are obligated not to disclose or use the information for any other purpose.</p>\n" +
                    "<p><strong>Security</strong></p>\n" +
                    "<p>I value your trust in providing us your Personal Information, thus we are striving to use commercially acceptable means of protecting it. But remember that no method of transmission over the internet, or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.</p>\n" +
                    "<p><strong>Links to Other Sites</strong></p>\n" +
                    "<p>This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site. Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy of these websites. I have no control over and assume no responsibility for the content, privacy policies, or practices of any third-party sites or services.</p>\n" +
                    "<p><strong>Children&rsquo;s Privacy</strong></p>\n" +
                    "<p>These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information from children under 13 years of age. In the case I discover that a child under 13 has provided me with personal information, I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child has provided us with personal information, please contact me so that I will be able to do necessary actions.</p>\n" +
                    "<p><strong>Changes to This Privacy Policy</strong></p>\n" +
                    "<p>I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes. I will notify you of any changes by posting the new Privacy Policy on this page.</p>\n" +
                    "<p>This policy is effective as of 2024-11-03</p>\n" +
                    "<p><strong>Contact Us</strong></p>\n" +
                    "<p>If you have any questions or suggestions about my Privacy Policy, do not hesitate to contact me at smkokiapps@gmail.com .</p>");
        }
        privacy_text.setText(sp);
        privacy_dialog.show();
        Window window = privacy_dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

    }


}
