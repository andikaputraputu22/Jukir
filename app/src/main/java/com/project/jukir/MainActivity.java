package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.project.jukir.adapter.AdapterBuilding;
import com.project.jukir.databinding.ActivityMainBinding;
import com.project.jukir.databinding.LayoutMainBinding;
import com.project.jukir.databinding.SideMenuBinding;
import com.project.jukir.models.DataLocation;
import com.project.jukir.models.LocationModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ActivityMainBinding binding;
    private LayoutMainBinding layoutMainBinding;
    private SideMenuBinding sideMenuBinding;
    private AdapterBuilding adapterBuilding;
    private List<DataLocation> itemBuilding;
    private String token, name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        layoutMainBinding = binding.layoutMain;
        sideMenuBinding = binding.layoutSideMenu;
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        name = SharedPreference.getSharedPreference(context, StaticController.KEY_NAME);
        email = SharedPreference.getSharedPreference(context, StaticController.KEY_EMAIL);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
        initAction();
        layoutMainBinding.search.setText("");
    }

    private void initView() {
        sideMenuBinding.name.setText(name);
        sideMenuBinding.email.setText(email);
        layoutMainBinding.name.setText(name + ",");

        if (StaticController.isUser(context)) {
            sideMenuBinding.changePassword.setVisibility(View.VISIBLE);
            sideMenuBinding.topUp.setVisibility(View.VISIBLE);
        }
    }

    private void initList() {
        itemBuilding = new ArrayList<>();
        adapterBuilding = new AdapterBuilding(context, itemBuilding);
        layoutMainBinding.recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        layoutMainBinding.recyclerView.setHasFixedSize(true);
        layoutMainBinding.recyclerView.setAdapter(adapterBuilding);

        getListBuilding();
    }

    private void getListBuilding() {
        StaticController.api.listBuilding("Bearer " + token, "")
                .enqueue(new Callback<LocationModel>() {
                    @Override
                    public void onResponse(Call<LocationModel> call, Response<LocationModel> response) {
                        if (response.code() == 200) {
                            if (response.body().data.size() != 0) {
                                itemBuilding.addAll(response.body().data);
                                adapterBuilding.addItem(itemBuilding);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationModel> call, Throwable t) {

                    }
                });
    }

    private void initAction() {
        adapterBuilding.setOnItemClickListener(new AdapterBuilding.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DataLocation obj, int position) {
                Intent intent = new Intent(context, SlotParkingActivity.class);
                intent.putExtra("dataBuilding", obj);
                startActivity(intent);
            }
        });

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

        sideMenuBinding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(binding.navigationView, true);
                Intent intent = new Intent(context, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        sideMenuBinding.topUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawerLayout.closeDrawer(binding.navigationView, true);
                Intent intent = new Intent(context, WalletActivity.class);
                startActivity(intent);
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

        layoutMainBinding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    layoutMainBinding.search.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(layoutMainBinding.search.getWindowToken(), 0);
                    if (layoutMainBinding.search.getText().toString().length() != 0) {
                        Intent intent = new Intent(context, SearchActivity.class);
                        intent.putExtra("searchKeyword", layoutMainBinding.search.getText().toString());
                        startActivity(intent);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}