package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.adapter.AdapterReport;
import com.project.jukir.adapter.AdapterWallet;
import com.project.jukir.databinding.ActivityDetailReportBinding;
import com.project.jukir.models.DataLocation;
import com.project.jukir.models.DataReport;
import com.project.jukir.models.DataWallet;
import com.project.jukir.models.ListLocation;
import com.project.jukir.models.ReportModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailReportActivity extends AppCompatActivity {

    private Context context;
    private ActivityDetailReportBinding binding;
    private DataReport dataReport;
    private DataLocation dataLocation;
    private AdapterReport adapterReport;
    private List<ListLocation> itemReport;
    private String token, detailDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        detailDate = (String) getIntent().getSerializableExtra("detailDate");
        dataReport = (DataReport) getIntent().getSerializableExtra("dataReport");
        dataLocation = (DataLocation) getIntent().getSerializableExtra("dataLocation");

        initBack();
        initList();
        initView();
        initAction();
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.total_transaction));
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
        binding.date.setText(detailDate);
    }

    private void initList() {
        itemReport = new ArrayList<>();
        adapterReport = new AdapterReport(context, itemReport);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapterReport);

        getListReport();
    }

    private void getListReport() {
        List<ListLocation> dataList = dataReport.list;
        if (dataList.size() != 0) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            itemReport.clear();
            itemReport.addAll(dataList);
            adapterReport.addItem(itemReport);
        } else {
            binding.recyclerView.setVisibility(View.GONE);
        }
    }

    private void initAction() {
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        binding.print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(dataReport.url));
                startActivity(intent);
            }
        });
    }

    private void showDatePicker() {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        binding.date.setText(StaticController.getFormattedDateSimple(date_ship_millis, "dd-MM-yyyy"));
                        getData(StaticController.getFormattedDateSimple(date_ship_millis, "yyyy-MM-dd"));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
//        datePicker.setMinDate(cur_calender);
        datePicker.show(getSupportFragmentManager(), "Datepickerdialog");
    }

    private void getData(String date) {
        StaticController.api.adminReport("Bearer " + token, date, String.valueOf(dataLocation.id))
                .enqueue(new Callback<ReportModel>() {
                    @Override
                    public void onResponse(Call<ReportModel> call, Response<ReportModel> response) {
                        if (response.code() == 200) {
                            ReportModel data = response.body();
                            if (data.status == 200) {
                                dataReport = data.data;
                                getListReport();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportModel> call, Throwable t) {

                    }
                });
    }
}