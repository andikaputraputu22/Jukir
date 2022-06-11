package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.jukir.databinding.ActivityEmployeeMainBinding;
import com.project.jukir.databinding.LayoutEmployeeMainBinding;
import com.project.jukir.databinding.SideMenuBinding;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

public class EmployeeMainActivity extends AppCompatActivity {

    private Context context;
    private ActivityEmployeeMainBinding binding;
    private LayoutEmployeeMainBinding layoutEmployeeMainBinding;
    private SideMenuBinding sideMenuBinding;
    private String name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeMainBinding.inflate(getLayoutInflater());
        layoutEmployeeMainBinding = binding.layoutMain;
        sideMenuBinding = binding.layoutSideMenu;
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        name = SharedPreference.getSharedPreference(context, StaticController.KEY_NAME);
        email = SharedPreference.getSharedPreference(context, StaticController.KEY_EMAIL);

        initView();
        initAction();
    }

    private void initView() {
        sideMenuBinding.name.setText(name);
        sideMenuBinding.email.setText(email);
        layoutEmployeeMainBinding.name.setText("Hello " + name + ",");
    }

    private void initAction() {
        layoutEmployeeMainBinding.scanTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScanEmployeeActivity.class);
                startActivity(intent);
            }
        });

        layoutEmployeeMainBinding.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.openDrawer(binding.navigationView, true);
            }
        });

        sideMenuBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(binding.navigationView, true);
            }
        });

        sideMenuBinding.signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(binding.navigationView, true);
                SharedPreference.clearSharedPreference(context);
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}