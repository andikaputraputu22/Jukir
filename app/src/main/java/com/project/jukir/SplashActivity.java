package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.project.jukir.utils.StaticController;

public class SplashActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;

        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                Intent intent;
                if (StaticController.isLogin(context)) {
                    intent = new Intent(context, MainActivity.class);
                } else {
                    intent = new Intent(context, WelcomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}