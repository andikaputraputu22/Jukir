package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.project.jukir.databinding.ActivityAlertBinding;
import com.project.jukir.utils.StaticController;

public class AlertActivity extends AppCompatActivity {

    private Context context;
    private ActivityAlertBinding binding;
    private String fromWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlertBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        fromWhere = (String) getIntent().getSerializableExtra("fromWhere");

        initView();
        initAction();
    }

    private void initView() {
        if (fromWhere.equals(StaticController.FROM_CHANGE_PASSWORD)) {
            binding.messageAlert.setText(getString(R.string.success_change_password));
        }
    }

    private void initAction() {
        new Handler().postDelayed(new Thread() {
            @Override
            public void run() {
                if (fromWhere.equals(StaticController.FROM_CHANGE_PASSWORD)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        }, 2000);
    }
}