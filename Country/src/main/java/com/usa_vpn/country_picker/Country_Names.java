package com.usa_vpn.country_picker;

import android.content.Context;
import android.text.TextUtils;
import java.util.Locale;

public class Country_Names {

  // region Variables
  private String country_code;
  private String country_name;
  private String country_dialCode;
  private int country_flag;
  private String country_currency;
  // endregion

  // region Constructors
  Country_Names() {
  }

  Country_Names(String country_code, String country_name, String country_dialCode, int country_flag, String country_currency) {
    this.country_code = country_code;
    this.country_name = country_name;
    this.country_dialCode = country_dialCode;
    this.country_flag = country_flag;
    this.country_currency = country_currency;
  }
  // endregion

  // region Getter/Setter
  public String getCurrency() {
    return country_currency;
  }

  public void setCurrency(String country_currency) {
    this.country_currency = country_currency;
  }

  public String getCode() {
    return country_code;
  }

  public void setCode(String country_code) {
    this.country_code = country_code;
    if (TextUtils.isEmpty(country_name)) {
      country_name = new Locale("", country_code).getDisplayName();
    }
  }

  public String getName() {
    return country_name;
  }

  public void setName(String name) {
    this.country_name = name;
  }

  public String getDialCode() {
    return country_code;
  }

  public void setDialCode(String dialCode) {
    this.country_code = dialCode;
  }

  public int getFlag() {
    return country_flag;
  }

  public void setFlag(int flag) {
    this.country_flag = flag;
  }

  public void loadFlagByCode(Context context) {
    if (this.country_flag != -1) {
      return;
    }

    try {
      this.country_flag = context.getResources()
          .getIdentifier("flag_" + this.country_code.toLowerCase(Locale.ENGLISH), "drawable",
              context.getPackageName());
    } catch (Exception e) {
      e.printStackTrace();
      this.country_flag = -1;
    }
  }
  // endregion
}
