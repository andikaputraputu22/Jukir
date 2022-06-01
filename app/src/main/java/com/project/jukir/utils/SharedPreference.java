package com.project.jukir.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    private static final String PREFERENCE_NAME = "jukir_config";

    public static void writeSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getSharedPreference(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String text;
        text = settings.getString(key, null);
        return text;
    }

    public static void clearSharedPreference(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.apply();
    }
}
