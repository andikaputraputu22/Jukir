package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.jukir.databinding.ActivityMainBinding;
import com.project.jukir.databinding.LayoutMainBinding;
import com.project.jukir.databinding.SideMenuBinding;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ActivityMainBinding binding;
    private LayoutMainBinding layoutMainBinding;
    private SideMenuBinding sideMenuBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        layoutMainBinding = binding.layoutMain;
        sideMenuBinding = binding.layoutSideMenu;
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        initAction();
    }

    private void initAction() {
        layoutMainBinding.photo.setOnClickListener(new View.OnClickListener() {
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
    }
}