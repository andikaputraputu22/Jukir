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
    private String token;

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

        initAction();
    }

    @Override
    public void onStart() {
        super.onStart();
        initList();
        layoutMainBinding.search.setText("");
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