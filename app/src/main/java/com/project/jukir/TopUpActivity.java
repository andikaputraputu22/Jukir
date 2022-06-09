package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.databinding.ActivityTopUpBinding;
import com.project.jukir.models.DataWallet;
import com.project.jukir.models.TopupModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopUpActivity extends AppCompatActivity {

    private Context context;
    private ActivityTopUpBinding binding;
    private ProgressDialog progressDialog;
    private DataWallet dataWallet;
    private String token;
    private String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        dataWallet = (DataWallet) getIntent().getSerializableExtra("dataWallet");

        initBack();
        initView();
        initAction();
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.black));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.topup));
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
        Picasso.with(context).load(StaticController.URL_PHOTO_WALLET + dataWallet.icon).into(binding.iconTopup);
        binding.saldo.setText(StaticController.getFormatCurrency().format(dataWallet.saldo));
    }

    private void topUp(String amount) {
        progressDialog = StaticController.showProgressDialog(progressDialog, context, getString(R.string.loading));
        progressDialog.show();
        StaticController.api.topupSaldo("Bearer " + token, String.valueOf(dataWallet.id), amount)
                .enqueue(new Callback<TopupModel>() {
                    @Override
                    public void onResponse(Call<TopupModel> call, Response<TopupModel> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            if (response.body().status == 200) {
                                Intent intent = new Intent(context, AlertActivity.class);
                                intent.putExtra("fromWhere", StaticController.FROM_TOPUP);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TopupModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initAction() {
        binding.inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    amount = "";
                }
                binding.price1.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price2.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price3.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price4.setBackgroundResource(R.drawable.custom_btn_price);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.topupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!amount.equals("")) {
                    topUp(amount);
                } else {
                    if (binding.inputAmount.getText().toString().length() == 0) {
                        Toast.makeText(context, getString(R.string.empty_amount), Toast.LENGTH_SHORT).show();
                    } else {
                        amount = binding.inputAmount.getText().toString();
                        topUp(amount);
                    }
                }
            }
        });

        binding.price1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = "50000";
                binding.inputAmount.setText("");
                binding.price1.setBackgroundResource(R.drawable.custom_btn_auth);
                binding.price2.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price3.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price4.setBackgroundResource(R.drawable.custom_btn_price);
            }
        });

        binding.price2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = "100000";
                binding.inputAmount.setText("");
                binding.price2.setBackgroundResource(R.drawable.custom_btn_auth);
                binding.price1.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price3.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price4.setBackgroundResource(R.drawable.custom_btn_price);
            }
        });

        binding.price3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = "200000";
                binding.inputAmount.setText("");
                binding.price3.setBackgroundResource(R.drawable.custom_btn_auth);
                binding.price1.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price2.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price4.setBackgroundResource(R.drawable.custom_btn_price);
            }
        });

        binding.price4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount = "500000";
                binding.inputAmount.setText("");
                binding.price4.setBackgroundResource(R.drawable.custom_btn_auth);
                binding.price1.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price3.setBackgroundResource(R.drawable.custom_btn_price);
                binding.price2.setBackgroundResource(R.drawable.custom_btn_price);
            }
        });
    }
}