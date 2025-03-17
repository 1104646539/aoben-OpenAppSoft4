package com.open.soft.openappsoft.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.open.soft.openappsoft.jinbiao.model.SharedPreferencesUtil;

public class SharedPreferences2 {
    SharedPreferences sharedPreferences;

    public SharedPreferences2(Context context, String name) {
        sharedPreferences = SharedPreferencesUtil.getSharedPreferences(context, name);
    }


    public String query(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public String query(String key) {
        return query(key, "");
    }

    public boolean save(String key, String value) {
        return sharedPreferences.edit().putString(key, value).commit();
    }
}
