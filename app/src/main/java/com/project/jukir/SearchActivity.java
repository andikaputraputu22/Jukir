package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.project.jukir.adapter.AdapterSearchBuilding;
import com.project.jukir.databinding.ActivitySearchBinding;
import com.project.jukir.models.DataLocation;
import com.project.jukir.models.LocationModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private Context context;
    private ActivitySearchBinding binding;
    private ProgressDialog progressDialog;
    private AdapterSearchBuilding adapterBuilding;
    private List<DataLocation> itemBuilding;
    private String keyword, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        keyword = (String) getIntent().getSerializableExtra("searchKeyword");
        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);

        initView();
        initList();
        initAction();
        initBack();
        getListBuilding(keyword);
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
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
        binding.search.setText(keyword);
    }

    private void initList() {
        itemBuilding = new ArrayList<>();
        adapterBuilding = new AdapterSearchBuilding(context, itemBuilding);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapterBuilding);
    }

    private void getListBuilding(String keyword) {
        progressDialog = StaticController.showProgressDialog(progressDialog, context, getString(R.string.loading));
        progressDialog.show();
        StaticController.api.listBuilding("Bearer " + token, keyword)
                .enqueue(new Callback<LocationModel>() {
                    @Override
                    public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            if (response.body().data.size() != 0) {
                                binding.recyclerView.setVisibility(View.VISIBLE);
                                itemBuilding.clear();
                                itemBuilding.addAll(response.body().data);
                                adapterBuilding.addItem(itemBuilding);
                            } else {
                                binding.recyclerView.setVisibility(View.GONE);
                            }
                        } else {
                            binding.recyclerView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationModel> call, Throwable t) {
                        progressDialog.dismiss();
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                });
    }

    private void initAction() {
        binding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    binding.search.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(binding.search.getWindowToken(), 0);
                    if (binding.search.getText().toString().length() != 0) {
                        String keyword = binding.search.getText().toString();
                        getListBuilding(keyword);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}