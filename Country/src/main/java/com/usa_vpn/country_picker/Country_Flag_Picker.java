package com.usa_vpn.country_picker;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mukesh.countrypicker.R;
import com.usa_vpn.country_picker.listeners.DialogInteractionListener;
import com.usa_vpn.country_picker.listeners.Country_Picker_Listener_Interface;
import com.usa_vpn.country_picker.listeners.OnItemClickListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Country_Flag_Picker implements DialogInteractionListener, LifecycleObserver {

  // region Countries
  private final Country_Names[] COUNTRIES = {
      new Country_Names("AD", "Andorra", "+376", R.drawable.flag_ad, "EUR"),
      new Country_Names("AE", "United Arab Emirates", "+971", R.drawable.flag_ae, "AED"),
      new Country_Names("AF", "Afghanistan", "+93", R.drawable.flag_af, "AFN"),
      new Country_Names("AG", "Antigua and Barbuda", "+1", R.drawable.flag_ag, "XCD"),
      new Country_Names("AI", "Anguilla", "+1", R.drawable.flag_ai, "XCD"),
      new Country_Names("AL", "Albania", "+355", R.drawable.flag_al, "ALL"),
      new Country_Names("AM", "Armenia", "+374", R.drawable.flag_am, "AMD"),
      new Country_Names("AO", "Angola", "+244", R.drawable.flag_ao, "AOA"),
      new Country_Names("AQ", "Antarctica", "+672", R.drawable.flag_aq, "USD"),
      new Country_Names("AR", "Argentina", "+54", R.drawable.flag_ar, "ARS"),
      new Country_Names("AS", "American Samoa", "+1", R.drawable.flag_as, "USD"),
      new Country_Names("AT", "Austria", "+43", R.drawable.flag_at, "EUR"),
      new Country_Names("AU", "Australia", "+61", R.drawable.flag_au, "AUD"),
      new Country_Names("AW", "Aruba", "+297", R.drawable.flag_aw, "AWG"),
      new Country_Names("AX", "Aland Islands", "+358", R.drawable.flag_ax, "EUR"),
      new Country_Names("AZ", "Azerbaijan", "+994", R.drawable.flag_az, "AZN"),
      new Country_Names("BA", "Bosnia and Herzegovina", "+387", R.drawable.flag_ba, "BAM"),
      new Country_Names("BB", "Barbados", "+1", R.drawable.flag_bb, "BBD"),
      new Country_Names("BD", "Bangladesh", "+880", R.drawable.flag_bd, "BDT"),
      new Country_Names("BE", "Belgium", "+32", R.drawable.flag_be, "EUR"),
      new Country_Names("BF", "Burkina Faso", "+226", R.drawable.flag_bf, "XOF"),
      new Country_Names("BG", "Bulgaria", "+359", R.drawable.flag_bg, "BGN"),
      new Country_Names("BH", "Bahrain", "+973", R.drawable.flag_bh, "BHD"),
      new Country_Names("BI", "Burundi", "+257", R.drawable.flag_bi, "BIF"),
      new Country_Names("BJ", "Benin", "+229", R.drawable.flag_bj, "XOF"),
      new Country_Names("BL", "Saint Barthelemy", "+590", R.drawable.flag_bl, "EUR"),
      new Country_Names("BM", "Bermuda", "+1", R.drawable.flag_bm, "BMD"),
      new Country_Names("BN", "Brunei Darussalam", "+673", R.drawable.flag_bn, "BND"),
      new Country_Names("BO", "Bolivia, Plurinational State of", "+591", R.drawable.flag_bo, "BOB"),
      new Country_Names("BQ", "Bonaire", "+599", R.drawable.flag_bq, "USD"),
      new Country_Names("BR", "Brazil", "+55", R.drawable.flag_br, "BRL"),
      new Country_Names("BS", "Bahamas", "+1", R.drawable.flag_bs, "BSD"),
      new Country_Names("BT", "Bhutan", "+975", R.drawable.flag_bt, "BTN"),
      new Country_Names("BV", "Bouvet Island", "+47", R.drawable.flag_bv, "NOK"),
      new Country_Names("BW", "Botswana", "+267", R.drawable.flag_bw, "BWP"),
      new Country_Names("BY", "Belarus", "+375", R.drawable.flag_by, "BYR"),
      new Country_Names("BZ", "Belize", "+501", R.drawable.flag_bz, "BZD"),
      new Country_Names("CA", "Canada", "+1", R.drawable.flag_ca, "CAD"),
      new Country_Names("CC", "Cocos (Keeling) Islands", "+61", R.drawable.flag_cc, "AUD"),
      new Country_Names("CD", "Congo, The Democratic Republic of the", "+243", R.drawable.flag_cd, "CDF"),
      new Country_Names("CF", "Central African Republic", "+236", R.drawable.flag_cf, "XAF"),
      new Country_Names("CG", "Congo", "+242", R.drawable.flag_cg, "XAF"),
      new Country_Names("CH", "Switzerland", "+41", R.drawable.flag_ch, "CHF"),
      new Country_Names("CI", "Ivory Coast", "+225", R.drawable.flag_ci, "XOF"),
      new Country_Names("CK", "Cook Islands", "+682", R.drawable.flag_ck, "NZD"),
      new Country_Names("CL", "Chile", "+56", R.drawable.flag_cl, "CLP"),
      new Country_Names("CM", "Cameroon", "+237", R.drawable.flag_cm, "XAF"),
      new Country_Names("CN", "China", "+86", R.drawable.flag_cn, "CNY"),
      new Country_Names("CO", "Colombia", "+57", R.drawable.flag_co, "COP"),
      new Country_Names("CR", "Costa Rica", "+506", R.drawable.flag_cr, "CRC"),
      new Country_Names("CU", "Cuba", "+53", R.drawable.flag_cu, "CUP"),
      new Country_Names("CV", "Cape Verde", "+238", R.drawable.flag_cv, "CVE"),
      new Country_Names("CW", "Curacao", "+599", R.drawable.flag_cw, "ANG"),
      new Country_Names("CX", "Christmas Island", "+61", R.drawable.flag_cx, "AUD"),
      new Country_Names("CY", "Cyprus", "+357", R.drawable.flag_cy, "EUR"),
      new Country_Names("CZ", "Czech Republic", "+420", R.drawable.flag_cz, "CZK"),
      new Country_Names("DE", "Hesse", "+49", R.drawable.flag_de, "EUR"),
      new Country_Names("DJ", "Djibouti", "+253", R.drawable.flag_dj, "DJF"),
      new Country_Names("DK", "Denmark", "+45", R.drawable.flag_dk, "DKK"),
      new Country_Names("DM", "Dominica", "+1", R.drawable.flag_dm, "XCD"),
      new Country_Names("DO", "Dominican Republic", "+1", R.drawable.flag_do, "DOP"),
      new Country_Names("DZ", "Algeria", "+213", R.drawable.flag_dz, "DZD"),
      new Country_Names("EC", "Ecuador", "+593", R.drawable.flag_ec, "USD"),
      new Country_Names("EE", "Estonia", "+372", R.drawable.flag_ee, "EUR"),
      new Country_Names("EG", "Egypt", "+20", R.drawable.flag_eg, "EGP"),
      new Country_Names("EH", "Western Sahara", "+212", R.drawable.flag_eh, "MAD"),
      new Country_Names("ER", "Eritrea", "+291", R.drawable.flag_er, "ERN"),
      new Country_Names("ES", "Spain", "+34", R.drawable.flag_es, "EUR"),
      new Country_Names("ET", "Ethiopia", "+251", R.drawable.flag_et, "ETB"),
      new Country_Names("FI", "Finland", "+358", R.drawable.flag_fi, "EUR"),
      new Country_Names("FJ", "Fiji", "+679", R.drawable.flag_fj, "FJD"),
      new Country_Names("FK", "Falkland Islands (Malvinas)", "+500", R.drawable.flag_fk, "FKP"),
      new Country_Names("FM", "Micronesia, Federated States of", "+691", R.drawable.flag_fm, "USD"),
      new Country_Names("FO", "Faroe Islands", "+298", R.drawable.flag_fo, "DKK"),
      new Country_Names("FR", "France", "+33", R.drawable.flag_fr, "EUR"),
      new Country_Names("GA", "Gabon", "+241", R.drawable.flag_ga, "XAF"),
      new Country_Names("GB", "United Kingdom", "+44", R.drawable.flag_gb, "GBP"),
      new Country_Names("GD", "Grenada", "+1", R.drawable.flag_gd, "XCD"),
      new Country_Names("GE", "Georgia", "+995", R.drawable.flag_ge, "GEL"),
      new Country_Names("GF", "French Guiana", "+594", R.drawable.flag_gf, "EUR"),
      new Country_Names("GG", "Guernsey", "+44", R.drawable.flag_gg, "GGP"),
      new Country_Names("GH", "Ghana", "+233", R.drawable.flag_gh, "GHS"),
      new Country_Names("GI", "Gibraltar", "+350", R.drawable.flag_gi, "GIP"),
      new Country_Names("GL", "Greenland", "+299", R.drawable.flag_gl, "DKK"),
      new Country_Names("GM", "Gambia", "+220", R.drawable.flag_gm, "GMD"),
      new Country_Names("GN", "Guinea", "+224", R.drawable.flag_gn, "GNF"),
      new Country_Names("GP", "Guadeloupe", "+590", R.drawable.flag_gp, "EUR"),
      new Country_Names("GQ", "Equatorial Guinea", "+240", R.drawable.flag_gq, "XAF"),
      new Country_Names("GR", "Greece", "+30", R.drawable.flag_gr, "EUR"),
      new Country_Names("GS", "South Georgia and the South Sandwich Islands", "+500", R.drawable.flag_gs,
          "GBP"),
      new Country_Names("GT", "Guatemala", "+502", R.drawable.flag_gt, "GTQ"),
      new Country_Names("GU", "Guam", "+1", R.drawable.flag_gu, "USD"),
      new Country_Names("GW", "Guinea-Bissau", "+245", R.drawable.flag_gw, "XOF"),
      new Country_Names("GY", "Guyana", "+595", R.drawable.flag_gy, "GYD"),
      new Country_Names("HK", "Hong Kong", "+852", R.drawable.flag_hk, "HKD"),
      new Country_Names("HM", "Heard Island and McDonald Islands", "+000", R.drawable.flag_hm, "AUD"),
      new Country_Names("HN", "Honduras", "+504", R.drawable.flag_hn, "HNL"),
      new Country_Names("HR", "Croatia", "+385", R.drawable.flag_hr, "HRK"),
      new Country_Names("HT", "Haiti", "+509", R.drawable.flag_ht, "HTG"),
      new Country_Names("HU", "Hungary", "+36", R.drawable.flag_hu, "HUF"),
      new Country_Names("ID", "Indonesia", "+62", R.drawable.flag_id, "IDR"),
      new Country_Names("IE", "Ireland", "+353", R.drawable.flag_ie, "EUR"),
      new Country_Names("IL", "Israel", "+972", R.drawable.flag_il, "ILS"),
      new Country_Names("IM", "Isle of Man", "+44", R.drawable.flag_im, "GBP"),
      new Country_Names("IN", "India", "+91", R.drawable.flag_in, "INR"),
      new Country_Names("IO", "British Indian Ocean Territory", "+246", R.drawable.flag_io, "USD"),
      new Country_Names("IQ", "Iraq", "+964", R.drawable.flag_iq, "IQD"),
      new Country_Names("IR", "Iran, Islamic Republic of", "+98", R.drawable.flag_ir, "IRR"),
      new Country_Names("IS", "Iceland", "+354", R.drawable.flag_is, "ISK"),
      new Country_Names("IT", "Italy", "+39", R.drawable.flag_it, "EUR"),
      new Country_Names("JE", "Jersey", "+44", R.drawable.flag_je, "JEP"),
      new Country_Names("JM", "Jamaica", "+1", R.drawable.flag_jm, "JMD"),
      new Country_Names("JO", "Jordan", "+962", R.drawable.flag_jo, "JOD"),
      new Country_Names("JP", "Japan", "+81", R.drawable.flag_jp, "JPY"),
      new Country_Names("KE", "Kenya", "+254", R.drawable.flag_ke, "KES"),
      new Country_Names("KG", "Kyrgyzstan", "+996", R.drawable.flag_kg, "KGS"),
      new Country_Names("KH", "Cambodia", "+855", R.drawable.flag_kh, "KHR"),
      new Country_Names("KI", "Kiribati", "+686", R.drawable.flag_ki, "AUD"),
      new Country_Names("KM", "Comoros", "+269", R.drawable.flag_km, "KMF"),
      new Country_Names("KN", "Saint Kitts and Nevis", "+1", R.drawable.flag_kn, "XCD"),
      new Country_Names("KP", "North Korea", "+850", R.drawable.flag_kp, "KPW"),
      new Country_Names("KR", "South Korea", "+82", R.drawable.flag_kr, "KRW"),
      new Country_Names("KW", "Kuwait", "+965", R.drawable.flag_kw, "KWD"),
      new Country_Names("KY", "Cayman Islands", "+345", R.drawable.flag_ky, "KYD"),
      new Country_Names("KZ", "Kazakhstan", "+7", R.drawable.flag_kz, "KZT"),
      new Country_Names("LA", "Lao People's Democratic Republic", "+856", R.drawable.flag_la, "LAK"),
      new Country_Names("LB", "Lebanon", "+961", R.drawable.flag_lb, "LBP"),
      new Country_Names("LC", "Saint Lucia", "+1", R.drawable.flag_lc, "XCD"),
      new Country_Names("LI", "Liechtenstein", "+423", R.drawable.flag_li, "CHF"),
      new Country_Names("LK", "Sri Lanka", "+94", R.drawable.flag_lk, "LKR"),
      new Country_Names("LR", "Liberia", "+231", R.drawable.flag_lr, "LRD"),
      new Country_Names("LS", "Lesotho", "+266", R.drawable.flag_ls, "LSL"),
      new Country_Names("LT", "Lithuania", "+370", R.drawable.flag_lt, "LTL"),
      new Country_Names("LU", "Luxembourg", "+352", R.drawable.flag_lu, "EUR"),
      new Country_Names("LV", "Latvia", "+371", R.drawable.flag_lv, "LVL"),
      new Country_Names("LY", "Libyan Arab Jamahiriya", "+218", R.drawable.flag_ly, "LYD"),
      new Country_Names("MA", "Morocco", "+212", R.drawable.flag_ma, "MAD"),
      new Country_Names("MC", "Monaco", "+377", R.drawable.flag_mc, "EUR"),
      new Country_Names("MD", "Moldova, Republic of", "+373", R.drawable.flag_md, "MDL"),
      new Country_Names("ME", "Montenegro", "+382", R.drawable.flag_me, "EUR"),
      new Country_Names("MF", "Saint Martin", "+590", R.drawable.flag_mf, "EUR"),
      new Country_Names("MG", "Madagascar", "+261", R.drawable.flag_mg, "MGA"),
      new Country_Names("MH", "Marshall Islands", "+692", R.drawable.flag_mh, "USD"),
      new Country_Names("MK", "Macedonia, The Former Yugoslav Republic of", "+389", R.drawable.flag_mk,
          "MKD"),
      new Country_Names("ML", "Mali", "+223", R.drawable.flag_ml, "XOF"),
      new Country_Names("MM", "Myanmar", "+95", R.drawable.flag_mm, "MMK"),
      new Country_Names("MN", "Mongolia", "+976", R.drawable.flag_mn, "MNT"),
      new Country_Names("MO", "Macao", "+853", R.drawable.flag_mo, "MOP"),
      new Country_Names("MP", "Northern Mariana Islands", "+1", R.drawable.flag_mp, "USD"),
      new Country_Names("MQ", "Martinique", "+596", R.drawable.flag_mq, "EUR"),
      new Country_Names("MR", "Mauritania", "+222", R.drawable.flag_mr, "MRO"),
      new Country_Names("MS", "Montserrat", "+1", R.drawable.flag_ms, "XCD"),
      new Country_Names("MT", "Malta", "+356", R.drawable.flag_mt, "EUR"),
      new Country_Names("MU", "Mauritius", "+230", R.drawable.flag_mu, "MUR"),
      new Country_Names("MV", "Maldives", "+960", R.drawable.flag_mv, "MVR"),
      new Country_Names("MW", "Malawi", "+265", R.drawable.flag_mw, "MWK"),
      new Country_Names("MX", "Mexico", "+52", R.drawable.flag_mx, "MXN"),
      new Country_Names("MY", "Malaysia", "+60", R.drawable.flag_my, "MYR"),
      new Country_Names("MZ", "Mozambique", "+258", R.drawable.flag_mz, "MZN"),
      new Country_Names("NA", "Namibia", "+264", R.drawable.flag_na, "NAD"),
      new Country_Names("NC", "New Caledonia", "+687", R.drawable.flag_nc, "XPF"),
      new Country_Names("NE", "Niger", "+227", R.drawable.flag_ne, "XOF"),
      new Country_Names("NF", "Norfolk Island", "+672", R.drawable.flag_nf, "AUD"),
      new Country_Names("NG", "Nigeria", "+234", R.drawable.flag_ng, "NGN"),
      new Country_Names("NI", "Nicaragua", "+505", R.drawable.flag_ni, "NIO"),
      new Country_Names("NL", "Netherlands", "+31", R.drawable.flag_nl, "EUR"),
      new Country_Names("NO", "Norway", "+47", R.drawable.flag_no, "NOK"),
      new Country_Names("NP", "Nepal", "+977", R.drawable.flag_np, "NPR"),
      new Country_Names("NR", "Nauru", "+674", R.drawable.flag_nr, "AUD"),
      new Country_Names("NU", "Niue", "+683", R.drawable.flag_nu, "NZD"),
      new Country_Names("NZ", "New Zealand", "+64", R.drawable.flag_nz, "NZD"),
      new Country_Names("OM", "Oman", "+968", R.drawable.flag_om, "OMR"),
      new Country_Names("PA", "Panama", "+507", R.drawable.flag_pa, "PAB"),
      new Country_Names("PE", "Peru", "+51", R.drawable.flag_pe, "PEN"),
      new Country_Names("PF", "French Polynesia", "+689", R.drawable.flag_pf, "XPF"),
      new Country_Names("PG", "Papua New Guinea", "+675", R.drawable.flag_pg, "PGK"),
      new Country_Names("PH", "Philippines", "+63", R.drawable.flag_ph, "PHP"),
      new Country_Names("PK", "Pakistan", "+92", R.drawable.flag_pk, "PKR"),
      new Country_Names("PL", "Poland", "+48", R.drawable.flag_pl, "PLN"),
      new Country_Names("PM", "Saint Pierre and Miquelon", "+508", R.drawable.flag_pm, "EUR"),
      new Country_Names("PN", "Pitcairn", "+872", R.drawable.flag_pn, "NZD"),
      new Country_Names("PR", "Puerto Rico", "+1", R.drawable.flag_pr, "USD"),
      new Country_Names("PS", "Palestinian Territory, Occupied", "+970", R.drawable.flag_ps, "ILS"),
      new Country_Names("PT", "Portugal", "+351", R.drawable.flag_pt, "EUR"),
      new Country_Names("PW", "Palau", "+680", R.drawable.flag_pw, "USD"),
      new Country_Names("PY", "Paraguay", "+595", R.drawable.flag_py, "PYG"),
      new Country_Names("QA", "Qatar", "+974", R.drawable.flag_qa, "QAR"),
      new Country_Names("RE", "Reunion", "+262", R.drawable.flag_re, "EUR"),
      new Country_Names("RO", "Romania", "+40", R.drawable.flag_ro, "RON"),
      new Country_Names("RS", "Serbia", "+381", R.drawable.flag_rs, "RSD"),
      new Country_Names("RU", "Russia", "+7", R.drawable.flag_ru, "RUB"),
      new Country_Names("RW", "Rwanda", "+250", R.drawable.flag_rw, "RWF"),
      new Country_Names("SA", "Saudi Arabia", "+966", R.drawable.flag_sa, "SAR"),
      new Country_Names("SB", "Solomon Islands", "+677", R.drawable.flag_sb, "SBD"),
      new Country_Names("SC", "Seychelles", "+248", R.drawable.flag_sc, "SCR"),
      new Country_Names("SD", "Sudan", "+249", R.drawable.flag_sd, "SDG"),
      new Country_Names("SE", "Sweden", "+46", R.drawable.flag_se, "SEK"),
      new Country_Names("SG", "Singapore", "+65", R.drawable.flag_sg, "SGD"),
      new Country_Names("SH", "Saint Helena, Ascension and Tristan Da Cunha", "+290", R.drawable.flag_sh,
          "SHP"),
      new Country_Names("SI", "Slovenia", "+386", R.drawable.flag_si, "EUR"),


          new Country_Names("DE", "Germany", "+49", R.drawable.flag_ye, "EUR"),






          new Country_Names("SJ", "Svalbard and Jan Mayen", "+47", R.drawable.flag_sj, "NOK"),
      new Country_Names("SK", "Slovakia", "+421", R.drawable.flag_sk, "EUR"),
      new Country_Names("SL", "Sierra Leone", "+232", R.drawable.flag_sl, "SLL"),
      new Country_Names("SM", "San Marino", "+378", R.drawable.flag_sm, "EUR"),
      new Country_Names("SN", "Senegal", "+221", R.drawable.flag_sn, "XOF"),
      new Country_Names("SO", "Somalia", "+252", R.drawable.flag_so, "SOS"),
      new Country_Names("SR", "Suriname", "+597", R.drawable.flag_sr, "SRD"),
      new Country_Names("SS", "South Sudan", "+211", R.drawable.flag_ss, "SSP"),
      new Country_Names("ST", "Sao Tome and Principe", "+239", R.drawable.flag_st, "STD"),
      new Country_Names("SV", "El Salvador", "+503", R.drawable.flag_sv, "SVC"),
      new Country_Names("SX", "Sint Maarten", "+1", R.drawable.flag_sx, "ANG"),
      new Country_Names("SY", "Syrian Arab Republic", "+963", R.drawable.flag_sy, "SYP"),
      new Country_Names("SZ", "Swaziland", "+268", R.drawable.flag_sz, "SZL"),
      new Country_Names("TC", "Turks and Caicos Islands", "+1", R.drawable.flag_tc, "USD"),
      new Country_Names("TD", "Chad", "+235", R.drawable.flag_td, "XAF"),
      new Country_Names("TF", "French Southern Territories", "+262", R.drawable.flag_tf, "EUR"),
      new Country_Names("TG", "Togo", "+228", R.drawable.flag_tg, "XOF"),
      new Country_Names("TH", "Thailand", "+66", R.drawable.flag_th, "THB"),
      new Country_Names("TJ", "Tajikistan", "+992", R.drawable.flag_tj, "TJS"),
      new Country_Names("TK", "Tokelau", "+690", R.drawable.flag_tk, "NZD"),
      new Country_Names("TL", "East Timor", "+670", R.drawable.flag_tl, "USD"),
      new Country_Names("TM", "Turkmenistan", "+993", R.drawable.flag_tm, "TMT"),
      new Country_Names("TN", "Tunisia", "+216", R.drawable.flag_tn, "TND"),
      new Country_Names("TO", "Tonga", "+676", R.drawable.flag_to, "TOP"),
      new Country_Names("TR", "Turkey", "+90", R.drawable.flag_tr, "TRY"),
      new Country_Names("TT", "Trinidad and Tobago", "+1", R.drawable.flag_tt, "TTD"),
      new Country_Names("TV", "Tuvalu", "+688", R.drawable.flag_tv, "AUD"),
      new Country_Names("TW", "Taiwan", "+886", R.drawable.flag_tw, "TWD"),
      new Country_Names("TZ", "Tanzania, United Republic of", "+255", R.drawable.flag_tz, "TZS"),
      new Country_Names("UA", "Ukraine", "+380", R.drawable.flag_ua, "UAH"),
      new Country_Names("UG", "Uganda", "+256", R.drawable.flag_ug, "UGX"),
      new Country_Names("UM", "U.S. Minor Outlying Islands", "+1", R.drawable.flag_um, "USD"),
//      new Country_Names("US", "USA", "+1", R.drawable.flag_us, "USD"),
      new Country_Names("US", "United States", "+1", R.drawable.flag_us, "USD"),


      new Country_Names("US", "US", "+1", R.drawable.flag_us, "USD"),

      new Country_Names("UY", "Uruguay", "+598", R.drawable.flag_uy, "UYU"),
      new Country_Names("UZ", "Uzbekistan", "+998", R.drawable.flag_uz, "UZS"),
      new Country_Names("VA", "Holy See (Vatican City State)", "+379", R.drawable.flag_va, "EUR"),
      new Country_Names("VC", "Saint Vincent and the Grenadines", "+1", R.drawable.flag_vc, "XCD"),
      new Country_Names("VE", "Venezuela, Bolivarian Republic of", "+58", R.drawable.flag_ve, "VEF"),
      new Country_Names("VG", "Virgin Islands, British", "+1", R.drawable.flag_vg, "USD"),
      new Country_Names("VI", "Virgin Islands, U.S.", "+1", R.drawable.flag_vi, "USD"),
      new Country_Names("VN", "Vietnam", "+84", R.drawable.flag_vn, "VND"),
      new Country_Names("VU", "Vanuatu", "+678", R.drawable.flag_vu, "VUV"),
      new Country_Names("WF", "Wallis and Futuna", "+681", R.drawable.flag_wf, "XPF"),
      new Country_Names("WS", "Samoa", "+685", R.drawable.flag_ws, "WST"),
      new Country_Names("XK", "Kosovo", "+383", R.drawable.flag_xk, "EUR"),
      new Country_Names("YE", "Yemen", "+967", R.drawable.flag_ye, "YER"),
      new Country_Names("YT", "Mayotte", "+262", R.drawable.flag_yt, "EUR"),
      new Country_Names("ZA", "South Africa", "+27", R.drawable.flag_za, "ZAR"),
      new Country_Names("ZM", "Zambia", "+260", R.drawable.flag_zm, "ZMW"),
      new Country_Names("ZW", "Zimbabwe", "+263", R.drawable.flag_zw, "USD"),
  };

  // endregion

  // region Variables
  public static final int SORT_BY_NONE = 0;
  public static final int SORT_BY_NAME = 1;
  public static final int SORT_BY_ISO = 2;
  public static final int SORT_BY_DIAL_CODE = 3;
  public static final int THEME_OLD = 1;
  public static final int THEME_NEW = 2;
  private int theme;

  private int style;
  private Context context;
  private int sortBy = SORT_BY_NONE;
  private Country_Picker_Listener_Interface countryPickerListenerInterface;
  private boolean canSearch = true;

  private List<Country_Names> countries;
  private EditText searchEditText;
  private RecyclerView countriesRecyclerView;
  private LinearLayout rootView;
  private int textColor;
  private int hintColor;
  private int backgroundColor;
  private int searchIconId;
  private Drawable searchIcon;
  private Country_Flag_Picker_Adapter adapter;
  private List<Country_Names> searchResults;
  private DialogView bottomSheetDialog;
  private Dialog dialog;
  // endregion

//  // region Constructors
  private Country_Flag_Picker() {
  }

  Country_Flag_Picker(Builder builder) {
    sortBy = builder.sortBy;
    if (builder.countryPickerListenerInterface != null) {
      countryPickerListenerInterface = builder.countryPickerListenerInterface;
    }
    style = builder.style;
    context = builder.context;
    canSearch = builder.canSearch;
    theme = builder.theme;
    countries = new ArrayList<>(Arrays.asList(COUNTRIES));
    sortCountries(countries);
  }
  // endregion

  // region Listeners
  private void sortCountries(@NonNull List<Country_Names> countries) {
    if (sortBy == SORT_BY_NAME) {
      Collections.sort(countries, new Comparator<Country_Names>() {
        @Override
        public int compare(Country_Names countryNames1, Country_Names countryNames2) {
          return countryNames1.getName().trim().compareToIgnoreCase(countryNames2.getName().trim());
        }
      });
    } else if (sortBy == SORT_BY_ISO) {
      Collections.sort(countries, new Comparator<Country_Names>() {
        @Override
        public int compare(Country_Names countryNames1, Country_Names countryNames2) {
          return countryNames1.getCode().trim().compareToIgnoreCase(countryNames2.getCode().trim());
        }
      });
    } else if (sortBy == SORT_BY_DIAL_CODE) {
      Collections.sort(countries, new Comparator<Country_Names>() {
        @Override
        public int compare(Country_Names countryNames1, Country_Names countryNames2) {
          return countryNames1.getDialCode().trim().compareToIgnoreCase(countryNames2.getDialCode().trim());
        }
      });
    }
  }
  // endregion

  // region Utility Methods
  public void showDialog(@NonNull AppCompatActivity activity) {
    if (countries == null || countries.isEmpty()) {
      throw new IllegalArgumentException(context.getString(R.string.error_no_countries_found));
    } else {
      activity.getLifecycle().addObserver(this);
      dialog = new Dialog(activity);
      View dialogView = activity.getLayoutInflater().inflate(R.layout.country_picker_layout, null);
      initiateUi(dialogView);
      setCustomStyle(dialogView);
      setSearchEditText();
      setupRecyclerView(dialogView);
      dialog.setContentView(dialogView);
      if (dialog.getWindow() != null) {
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);
        if (theme == THEME_NEW) {
          Drawable background =
              ContextCompat.getDrawable(context, R.drawable.ic_dialog_background);
          if (background != null) {
            background.setColorFilter(
                new PorterDuffColorFilter(backgroundColor, PorterDuff.Mode.SRC_ATOP));
          }
          rootView.setBackgroundDrawable(background);
          dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
      }
      dialog.show();
    }
  }

  // region BottomSheet Methods
  public void showBottomSheet(AppCompatActivity activity) {
    if (countries == null || countries.isEmpty()) {
      throw new IllegalArgumentException(context.getString(R.string.error_no_countries_found));
    } else {
      activity.getLifecycle().addObserver(this);
      bottomSheetDialog = DialogView.newInstance(theme);
      bottomSheetDialog.setListener(this);
      bottomSheetDialog.show(activity.getSupportFragmentManager(), "bottomsheet");
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  private void dismissDialogs() {
    if (bottomSheetDialog != null) {
      bottomSheetDialog.dismiss();
    }
    if(dialog!=null && dialog.isShowing()) {
      dialog.dismiss();
    }
  }

  @Override public void setupRecyclerView(View sheetView) {
    searchResults = new ArrayList<>();
    searchResults.addAll(countries);
    adapter = new Country_Flag_Picker_Adapter(sheetView.getContext(), searchResults,
        new OnItemClickListener() {
          @Override public void onItemClicked(Country_Names countryNames) {
            if (countryPickerListenerInterface != null) {
              countryPickerListenerInterface.onSelectCountry(countryNames);
              if (bottomSheetDialog != null) {
                bottomSheetDialog.dismiss();
              }
              if(dialog!=null && dialog.isShowing()) {
                dialog.dismiss();
              }
              dialog = null;
              bottomSheetDialog = null;
              textColor = 0;
              hintColor = 0;
              backgroundColor = 0;
              searchIconId = 0;
              searchIcon = null;
            }
          }
        },
        textColor);
    countriesRecyclerView.setHasFixedSize(true);
    LinearLayoutManager layoutManager = new LinearLayoutManager(sheetView.getContext());
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    countriesRecyclerView.setLayoutManager(layoutManager);
    countriesRecyclerView.setAdapter(adapter);
  }

  @Override public void setSearchEditText() {
    if (canSearch) {
      searchEditText.addTextChangedListener(new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
          // Intentionally Empty
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
          // Intentionally Empty
        }

        @Override
        public void afterTextChanged(Editable searchQuery) {
          search(searchQuery.toString());
        }
      });
      searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        @Override public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
          InputMethodManager imm = (InputMethodManager) searchEditText.getContext()
              .getSystemService(Context.INPUT_METHOD_SERVICE);
          if (imm != null) {
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
          }
          return true;
        }
      });
    } else {
      searchEditText.setVisibility(View.GONE);
    }
  }

  private void search(String searchQuery) {
    searchResults.clear();
    for (Country_Names countryNames : countries) {
      if (countryNames.getName().toLowerCase(Locale.ENGLISH).contains(searchQuery.toLowerCase())) {
        searchResults.add(countryNames);
      }
    }
    sortCountries(searchResults);
    adapter.notifyDataSetChanged();
  }

  @SuppressWarnings("ResourceType")
  @Override public void setCustomStyle(View sheetView) {
    if (style != 0) {
      int[] attrs =
          {
              android.R.attr.textColor, android.R.attr.textColorHint, android.R.attr.background,
              android.R.attr.drawable
          };
      TypedArray ta = sheetView.getContext().obtainStyledAttributes(style, attrs);
      textColor = ta.getColor(0, Color.BLACK);
      hintColor = ta.getColor(1, Color.GRAY);
      backgroundColor = ta.getColor(2, Color.WHITE);
      searchIconId = ta.getResourceId(3, R.drawable.search_icon);
      searchEditText.setTextColor(textColor);
      searchEditText.setHintTextColor(hintColor);
      searchIcon = ContextCompat.getDrawable(searchEditText.getContext(), searchIconId);
      if (searchIconId == R.drawable.search_icon) {
        searchIcon.setColorFilter(new PorterDuffColorFilter(hintColor, PorterDuff.Mode.SRC_ATOP));
      }
      searchEditText.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null);
      rootView.setBackgroundColor(backgroundColor);
      ta.recycle();
    }
  }

  @Override public void initiateUi(View sheetView) {
    searchEditText = sheetView.findViewById(R.id.country_code_picker_search);
    countriesRecyclerView = sheetView.findViewById(R.id.countries_recycler_view);
    rootView = sheetView.findViewById(R.id.rootView);
  }
  // endregion

  public Country_Names getCountryFromSIM() {
    TelephonyManager telephonyManager =
        (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    if (telephonyManager != null
        && telephonyManager.getSimState() != TelephonyManager.SIM_STATE_ABSENT) {
      return getCountryByISO(telephonyManager.getSimCountryIso());
    }
    return null;
  }

  public Country_Names getCountryByLocale(@NonNull Locale locale) {
    String countryIsoCode = locale.getISO3Country().substring(0, 2).toLowerCase();
    return getCountryByISO(countryIsoCode);
  }

  public Country_Names getCountryByName(@NonNull String countryName) {
    Collections.sort(countries, new NameComparator());
    Country_Names countryNames = new Country_Names();
    countryNames.setName(countryName);
    int i = Collections.binarySearch(countries, countryNames, new NameComparator());
    if (i < 0) {
      return null;
    } else {
      return countries.get(i);
    }
  }

  public Country_Names getCountryByISO(@NonNull String countryIsoCode) {
    Collections.sort(countries, new ISOCodeComparator());
    Country_Names countryNames = new Country_Names();
    countryNames.setCode(countryIsoCode);
    int i = Collections.binarySearch(countries, countryNames, new ISOCodeComparator());
    if (i < 0) {
      return null;
    } else {
      return countries.get(i);
    }
  }
  // endregion

  // region Builder
  public static class Builder {
    private Context context;
    private int sortBy = SORT_BY_NONE;
    private boolean canSearch = true;
    private Country_Picker_Listener_Interface countryPickerListenerInterface;
    private int style;
    private int theme = THEME_NEW;

    public Builder with(@NonNull Context context) {
      this.context = context;
      return this;
    }

    public Builder style(@NonNull @StyleRes int style) {
      this.style = style;
      return this;
    }

    public Builder sortBy(@NonNull int sortBy) {
      this.sortBy = sortBy;
      return this;
    }

    public Builder listener(@NonNull Country_Picker_Listener_Interface countryPickerListenerInterface) {
      this.countryPickerListenerInterface = countryPickerListenerInterface;
      return this;
    }

    public Builder canSearch(@NonNull boolean canSearch) {
      this.canSearch = canSearch;
      return this;
    }

    public Builder theme(@NonNull int theme) {
      this.theme = theme;
      return this;
    }

    public Country_Flag_Picker build() {
      return new Country_Flag_Picker(this);
    }
  }
  // endregion

  // region Comparators
  public static class ISOCodeComparator implements Comparator<Country_Names> {
    @Override
    public int compare(Country_Names countryNames, Country_Names nextCountryNames) {
      return countryNames.getCode().compareToIgnoreCase(nextCountryNames.getCode());
    }
  }

  public static class NameComparator implements Comparator<Country_Names> {
    @Override
    public int compare(Country_Names countryNames, Country_Names nextCountryNames) {
      return countryNames.getName().compareToIgnoreCase(nextCountryNames.getName());
    }
  }
  // endregion
}
