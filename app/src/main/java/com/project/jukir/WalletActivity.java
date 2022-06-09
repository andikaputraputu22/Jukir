package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.adapter.AdapterWallet;
import com.project.jukir.databinding.ActivityWalletBinding;
import com.project.jukir.models.DataWallet;
import com.project.jukir.models.WalletModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends AppCompatActivity {

    private Context context;
    private ActivityWalletBinding binding;
    private AdapterWallet adapterWallet;
    private List<DataWallet> itemWallet;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalletBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);

        initBack();
        initList();
        initAction();
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.black));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.e_wallet));
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

    private void initList() {
        itemWallet = new ArrayList<>();
        adapterWallet = new AdapterWallet(context, itemWallet);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapterWallet);

        getListWallet();
    }

    private void getListWallet() {
        StaticController.api.listWallet("Bearer " + token)
                .enqueue(new Callback<WalletModel>() {
                    @Override
                    public void onResponse(Call<WalletModel> call, Response<WalletModel> response) {
                        if (response.code() == 200) {
                            if (response.body().data.size() != 0) {
                                itemWallet.addAll(response.body().data);
                                adapterWallet.addItem(itemWallet);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WalletModel> call, Throwable t) {

                    }
                });
    }

    private void initAction() {
        adapterWallet.setOnItemClickListener(new AdapterWallet.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DataWallet obj, int position) {
                Intent intent = new Intent(context, TopUpActivity.class);
                intent.putExtra("dataWallet", obj);
                startActivity(intent);
            }
        });
    }
}