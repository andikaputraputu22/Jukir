package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.jukir.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

//        binding.helloWorld.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, RegisterActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}