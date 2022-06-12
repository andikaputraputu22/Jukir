package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.databinding.ActivityPaymentEmployeeBinding;
import com.project.jukir.models.DataPay;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

public class PaymentEmployeeActivity extends AppCompatActivity {

    private Context context;
    private ActivityPaymentEmployeeBinding binding;
    private String token, idBooking, totalHarga, dateMasuk, parkingTime, parkingSpot, parkingFee, location, titleToolbar;
    private boolean isCash;
    private int totalHargaInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentEmployeeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        idBooking = (String) getIntent().getSerializableExtra("idBooking");
        totalHarga = (String) getIntent().getSerializableExtra("totalHarga");
        dateMasuk = (String) getIntent().getSerializableExtra("dateMasuk");
        parkingTime = (String) getIntent().getSerializableExtra("parkingTime");
        parkingSpot = (String) getIntent().getSerializableExtra("parkingSpot");
        parkingFee = (String) getIntent().getSerializableExtra("parkingFee");
        location = (String) getIntent().getSerializableExtra("location");
        titleToolbar = (String) getIntent().getSerializableExtra("titleToolbar");
        isCash = (boolean) getIntent().getSerializableExtra("isCash");
        totalHargaInt = (int) getIntent().getSerializableExtra("totalHargaInt");

        initBack();
        initAction();
        initView();
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(titleToolbar);
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
        if (isCash) {
            binding.viewCash.setVisibility(View.VISIBLE);
        }

        binding.location.setText(location);
        binding.totalPrice.setText(totalHarga);
        binding.date.setText(dateMasuk);
        binding.parkingTime.setText(parkingTime);
        binding.parkingSpot.setText(parkingSpot);
        binding.parkingFee.setText(parkingFee);
    }

    private void initAction() {
        binding.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCash) {
                    String amount = binding.inputAmount.getText().toString();
                    if (amount.length() == 0) {
                        Toast.makeText(context, getString(R.string.empty_amount), Toast.LENGTH_SHORT).show();
                    } else {
                        DataPay dataPay = new DataPay();
                        dataPay.total_price = totalHarga;
                        dataPay.driver_money = StaticController.getFormatCurrency().format(Integer.parseInt(amount));
                        dataPay.total_change = StaticController.getFormatCurrency().format(Integer.parseInt(amount) - totalHargaInt);
                        dataPay.date = dateMasuk;
                        dataPay.parking_time = parkingTime;
                        dataPay.parking_spot = parkingSpot;
                        dataPay.building_name = location;

                        Intent intent = new Intent(context, AlertActivity.class);
                        intent.putExtra("fromWhere", StaticController.FROM_CASH_PAY_EMPLOYEE);
                        intent.putExtra("dataPay", dataPay);
                        startActivity(intent);
                        finishAffinity();
                    }
                } else {
                    Intent intent = new Intent(context, AlertActivity.class);
                    intent.putExtra("fromWhere", StaticController.FROM_ECARD_PAY_EMPLOYEE);
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });
    }
}