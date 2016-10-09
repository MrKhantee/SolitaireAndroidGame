package com.romiezone.solitirePirate;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {
    public static void setStr(String val, String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static String getStr(String val, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(val, "0");

    }

    public static void setInt(int val, String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public static int getInt(String val, Context context) {
        SharedPreferences SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return SharedPreferences.getInt(val, 0);

    }
}
