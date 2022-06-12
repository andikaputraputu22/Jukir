package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.databinding.ActivityChoosePaymentBinding;
import com.project.jukir.utils.StaticController;

public class ChoosePaymentActivity extends AppCompatActivity {

    private Context context;
    private ActivityChoosePaymentBinding binding;
    private String idBooking, totalHarga, dateMasuk, parkingTime, parkingSpot, parkingFee, location;
    private int totalHargaInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoosePaymentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        idBooking = (String) getIntent().getSerializableExtra("idBooking");
        totalHarga = (String) getIntent().getSerializableExtra("totalHarga");
        dateMasuk = (String) getIntent().getSerializableExtra("dateMasuk");
        parkingTime = (String) getIntent().getSerializableExtra("parkingTime");
        parkingSpot = (String) getIntent().getSerializableExtra("parkingSpot");
        parkingFee = (String) getIntent().getSerializableExtra("parkingFee");
        location = (String) getIntent().getSerializableExtra("location");
        totalHargaInt = (int) getIntent().getSerializableExtra("totalHargaInt");

        initBack();
        initView();
        initAction();
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.choose_payment));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        binding.totalCost.setText(totalHarga);
    }

    private void initAction() {
        binding.ecardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaymentEmployeeActivity.class);
                intent.putExtra("idBooking", idBooking);
                intent.putExtra("totalHarga", totalHarga);
                intent.putExtra("dateMasuk", dateMasuk);
                intent.putExtra("parkingTime", parkingTime);
                intent.putExtra("parkingSpot", parkingSpot);
                intent.putExtra("parkingFee", parkingFee);
                intent.putExtra("location", location);
                intent.putExtra("isCash", false);
                intent.putExtra("titleToolbar", getString(R.string.ecard_payment));
                intent.putExtra("totalHargaInt", totalHargaInt);
                startActivity(intent);
            }
        });

        binding.cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PaymentEmployeeActivity.class);
                intent.putExtra("idBooking", idBooking);
                intent.putExtra("totalHarga", totalHarga);
                intent.putExtra("dateMasuk", dateMasuk);
                intent.putExtra("parkingTime", parkingTime);
                intent.putExtra("parkingSpot", parkingSpot);
                intent.putExtra("parkingFee", parkingFee);
                intent.putExtra("location", location);
                intent.putExtra("isCash", true);
                intent.putExtra("titleToolbar", getString(R.string.cash_payment));
                intent.putExtra("totalHargaInt", totalHargaInt);
                startActivity(intent);
            }
        });
    }
}