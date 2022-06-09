package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.adapter.AdapterLantai;
import com.project.jukir.adapter.AdapterSlot;
import com.project.jukir.databinding.ActivitySlotParkingBinding;
import com.project.jukir.models.DataLocation;
import com.project.jukir.models.LokasiLantai;
import com.project.jukir.models.LokasiLantaiParkir;

import java.util.ArrayList;
import java.util.List;

public class SlotParkingActivity extends AppCompatActivity {

    private Context context;
    private ActivitySlotParkingBinding binding;
    private AdapterLantai adapterLantai;
    private AdapterSlot adapterSlot1;
    private AdapterSlot adapterSlot2;
    private List<LokasiLantai> itemLantai;
    private List<LokasiLantaiParkir> itemSlot1;
    private List<LokasiLantaiParkir> itemSlot2;
    private List<LokasiLantaiParkir> itemFullSlot;
    private DataLocation dataLocation;
    private LokasiLantaiParkir reqData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySlotParkingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        dataLocation = (DataLocation) getIntent().getSerializableExtra("dataBuilding");

        initBack();
        initList();
        initDataLantai();
        initDataSlot(dataLocation.lokasi_lantai.get(0).id, dataLocation.lokasi_lantai.get(0).lokasi_lantai_parkir, true);
        initAction();
        displayInfo(dataLocation.nama_lokasi, dataLocation.lokasi_lantai.get(0).lantai);
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.parking_slots));
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

    private void displayInfo(String lokasi, String lantai) {
        binding.lantai.setText(lantai);
        binding.nameBuilding.setText(lokasi);
    }

    private void initList() {
        itemLantai = new ArrayList<>();
        adapterLantai = new AdapterLantai(context, itemLantai);
        binding.recyclerViewLantai.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewLantai.setHasFixedSize(true);
        binding.recyclerViewLantai.setAdapter(adapterLantai);

        itemSlot1 = new ArrayList<>();
        adapterSlot1 = new AdapterSlot(context, itemSlot1);
        binding.recyclerViewSlot1.setLayoutManager(new GridLayoutManager(context, 2));
        binding.recyclerViewSlot1.setHasFixedSize(true);
        binding.recyclerViewSlot1.setAdapter(adapterSlot1);

        itemSlot2 = new ArrayList<>();
        adapterSlot2 = new AdapterSlot(context, itemSlot2);
        binding.recyclerViewSlot2.setLayoutManager(new GridLayoutManager(context, 2));
        binding.recyclerViewSlot2.setHasFixedSize(true);
        binding.recyclerViewSlot2.setAdapter(adapterSlot2);
    }

    private void initDataLantai() {
        itemLantai.addAll(dataLocation.lokasi_lantai);
        adapterLantai.addItem(itemLantai);
    }

    private void initDataSlot(int lokasi_lantai_id, List<LokasiLantaiParkir> lokasi_lantai_parkir, boolean isFirstInit) {
        itemFullSlot = new ArrayList<>();
        List<LokasiLantaiParkir> dataList = lokasi_lantai_parkir;
        for (LokasiLantaiParkir data : dataList) {
            if (data.lokasi_lantai_id == lokasi_lantai_id) {
                reqData = data;
                itemFullSlot.add(reqData);
            }
        }

        processSlot(itemFullSlot, isFirstInit);
    }

    private void processSlot(List<LokasiLantaiParkir> itemFullSlot, boolean isFirstInit) {
        if (!isFirstInit) {
            itemSlot1.clear();
            itemSlot2.clear();
        }

        for (int i = 0; i < itemFullSlot.size(); i++) {
            if (i <= 19) {
                itemSlot1.add(itemFullSlot.get(i));
            } else {
                itemSlot2.add(itemFullSlot.get(i));
            }
        }

        adapterSlot1.addItem(itemSlot1);
        adapterSlot2.addItem(itemSlot2);
    }

    private void initAction() {
        adapterLantai.setOnItemClickListener(new AdapterLantai.OnItemClickListener() {
            @Override
            public void onItemClick(View view, LokasiLantai obj, int position) {
                initDataSlot(obj.id, obj.lokasi_lantai_parkir, false);
                displayInfo(dataLocation.nama_lokasi, obj.lantai);
            }
        });
    }
}