package com.mykaribe.vendor.utils;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by mahmud on 14/4/2016.
 */
public class PreferenceHelper {

    private static final String PREFS_NAME = "android_pref";

    private static PreferenceHelper sInstance;

    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    private PreferenceHelper(Context context) {
        mPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }


    private static PreferenceHelper getInstance() {

        if (sInstance == null){
            sInstance = new PreferenceHelper(App.getContext());
        }
        return sInstance;
    }

    public static String getString(String key, String defValue) {
        return getInstance().mPref.getString(key, defValue);
    }

    public static void putString(String key, String value) {
         getInstance().mEditor.putString(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        return getInstance().mPref.getInt(key, defValue);
    }

    public static void putInt(String key, int value) {
         getInstance().mEditor.putInt(key, value).apply();
    }
    public static void removeAllData() {
        getInstance().mEditor.clear().apply();
    }


}