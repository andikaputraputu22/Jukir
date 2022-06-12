package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.jukir.databinding.ActivityMoneyChangeBinding;
import com.project.jukir.models.DataPay;

public class MoneyChangeActivity extends AppCompatActivity {

    private Context context;
    private ActivityMoneyChangeBinding binding;
    private DataPay dataPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoneyChangeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        dataPay = (DataPay) getIntent().getSerializableExtra("dataPay");

        initView();
        initAction();
    }

    private void initView() {
        binding.location.setText(dataPay.building_name);
        binding.totalChange.setText(dataPay.total_change);
        binding.date.setText(dataPay.date);
        binding.parkingTime.setText(dataPay.parking_time);
        binding.parkingSpot.setText(dataPay.parking_spot);
        binding.driverMoney.setText(dataPay.driver_money);
        binding.parkingFee.setText(dataPay.total_price);
    }

    private void initAction() {
        binding.doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EmployeeMainActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }
}