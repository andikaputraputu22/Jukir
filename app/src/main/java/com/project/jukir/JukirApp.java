package com.project.jukir;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import timber.log.Timber;

public class JukirApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
