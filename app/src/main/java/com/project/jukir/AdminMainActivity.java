package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.project.jukir.databinding.ActivityAdminMainBinding;
import com.project.jukir.databinding.LayoutAdminMainBinding;
import com.project.jukir.databinding.SideMenuBinding;
import com.project.jukir.models.DataLocation;
import com.project.jukir.models.LocationDetailModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMainActivity extends AppCompatActivity {

    private Context context;
    private ActivityAdminMainBinding binding;
    private LayoutAdminMainBinding layoutAdminMainBinding;
    private SideMenuBinding sideMenuBinding;
    private String token, name, email, idUser;
    private DataLocation dataLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        layoutAdminMainBinding = binding.layoutMain;
        sideMenuBinding = binding.layoutSideMenu;
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        name = SharedPreference.getSharedPreference(context, StaticController.KEY_NAME);
        email = SharedPreference.getSharedPreference(context, StaticController.KEY_EMAIL);
        idUser = SharedPreference.getSharedPreference(context, StaticController.KEY_ID_USER);

        getData();
        initView();
        initAction();
    }

    private void initView() {
        sideMenuBinding.name.setText(name);
        sideMenuBinding.email.setText(email);
        layoutAdminMainBinding.name.setText("Hello " + name + ",");
    }

    private void getData() {
        StaticController.api.findBuilding("Bearer " + token, idUser)
                .enqueue(new Callback<LocationDetailModel>() {
                    @Override
                    public void onResponse(Call<LocationDetailModel> call, Response<LocationDetailModel> response) {
                        if (response.code() == 200) {
                            LocationDetailModel data = response.body();
                            if (data.status == 200) {
                                String slot = "Slot: " + data.data.slot_tersedia;
                                layoutAdminMainBinding.namePlace.setText(data.data.nama_lokasi);
                                layoutAdminMainBinding.slotPlace.setText(slot);
                                Picasso.with(context).load(StaticController.URL_PHOTO + data.data.gambar).into(layoutAdminMainBinding.photoPlace);
                                dataLocation = data.data;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationDetailModel> call, Throwable t) {

                    }
                });
    }

    private void initAction() {
        layoutAdminMainBinding.seeSlotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SlotParkingActivity.class);
                intent.putExtra("dataBuilding", dataLocation);
                startActivity(intent);
            }
        });

        layoutAdminMainBinding.reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReportActivity.class);
                intent.putExtra("dataBuilding", dataLocation);
                startActivity(intent);
            }
        });

        layoutAdminMainBinding.photo.setOnClickListener(new View.OnClickListener() {
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
    }
}