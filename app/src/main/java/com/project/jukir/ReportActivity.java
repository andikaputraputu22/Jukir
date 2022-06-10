package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.databinding.ActivityReportBinding;
import com.project.jukir.models.DataLocation;
import com.project.jukir.models.DataReport;
import com.project.jukir.models.ReportModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity {

    private Context context;
    private ActivityReportBinding binding;
    private DataLocation dataLocation;
    private DataReport dataReport;
    private String token, date, detailDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        dataLocation = (DataLocation) getIntent().getSerializableExtra("dataBuilding");
        date = StaticController.getCurrentDate("yyyy-MM-dd");
        detailDate = StaticController.getCurrentDate("dd-MM-yyyy");

        initBack();
        initView();
        initAction();
        getData(date);
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.report));
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
        binding.date.setText(StaticController.getCurrentDate("dd-MM-yyyy"));
    }

    private void initAction() {
        binding.filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        binding.detailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailReportActivity.class);
                intent.putExtra("detailDate", detailDate);
                intent.putExtra("dataReport", dataReport);
                intent.putExtra("dataLocation", dataLocation);
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
                        detailDate = StaticController.getFormattedDateSimple(date_ship_millis, "dd-MM-yyyy");
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
                                binding.totalCar.setText(String.valueOf(data.data.total_mobil));
                                binding.totalTransaction.setText(String.valueOf(data.data.total_trasaksi));
                                binding.totalIncome.setText(StaticController.getFormatCurrency().format(data.data.total_penghasilan));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ReportModel> call, Throwable t) {

                    }
                });
    }
}