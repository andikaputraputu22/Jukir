package com.project.jukir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.project.jukir.databinding.ActivityScanEmployeeBinding;
import com.project.jukir.models.DetailBooking;
import com.project.jukir.models.ResponseSuccess;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class ScanEmployeeActivity extends AppCompatActivity {

    private Context context;
    private ActivityScanEmployeeBinding binding;
    private CodeScanner codeScanner;
    private String token, idUser;

    private final int PERMISSION_CAMERA = 1;
    private final int DEFAULT_PRICE = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanEmployeeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        idUser = SharedPreference.getSharedPreference(context, StaticController.KEY_ID_USER);

        cameraPermission();
        initBack();
        initScanner();
        initAction();
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private void initBack() {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back);
        binding.toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.btn_auth), PorterDuff.Mode.SRC_ATOP);
        binding.toolbar.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.scan));
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

    private void cameraPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(ScanEmployeeActivity.this, new String[] {Manifest.permission.CAMERA}, PERMISSION_CAMERA);
        }
    }

    private void initScanner() {
        codeScanner = new CodeScanner(context, binding.scannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findDataBooking(result.getText());
                    }
                });
            }
        });
    }

    private void updateEmployee(String lokasi_lantai_parkir_id, String keluar, int total_harga, String idBooking, String dateMasuk, String parkingTime, String parkingSpot, String parkingFee, String total_harga_formatted, String location) {
        RequestBody reqLokasiLantaiParkirId = RequestBody.create(MultipartBody.FORM, lokasi_lantai_parkir_id);
        RequestBody reqKeluar = RequestBody.create(MultipartBody.FORM, keluar);
        RequestBody reqTotalHarga = RequestBody.create(MultipartBody.FORM, String.valueOf(total_harga));
        RequestBody reqEmployeeId = RequestBody.create(MultipartBody.FORM, idUser);

        StaticController.api.updateEmployee("Bearer " + token, reqLokasiLantaiParkirId, reqKeluar, reqTotalHarga, reqEmployeeId, idBooking)
                .enqueue(new Callback<ResponseSuccess>() {
                    @Override
                    public void onResponse(Call<ResponseSuccess> call, Response<ResponseSuccess> response) {
                        if (response.code() == 200) {
                            if (response.body().status == 200) {
                                Intent intent = new Intent(context, ChoosePaymentActivity.class);
                                intent.putExtra("idBooking", idBooking);
                                intent.putExtra("totalHarga", total_harga_formatted);
                                intent.putExtra("dateMasuk", dateMasuk);
                                intent.putExtra("parkingTime", parkingTime);
                                intent.putExtra("parkingSpot", parkingSpot);
                                intent.putExtra("parkingFee", parkingFee);
                                intent.putExtra("location", location);
                                intent.putExtra("totalHargaInt", total_harga);
                                startActivity(intent);
                            } else {
                                Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseSuccess> call, Throwable t) {
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void findDataBooking(String id) {
        StaticController.api.findBooking("Bearer " + token, id)
                .enqueue(new Callback<DetailBooking>() {
                    @Override
                    public void onResponse(Call<DetailBooking> call, Response<DetailBooking> response) {
                        if (response.code() == 200) {
                            DetailBooking data = response.body();
                            if (data.data != null) {
                                processData(data);
                            } else {
                                Toast.makeText(context, getString(R.string.data_not_found), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailBooking> call, Throwable t) {
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void processData(DetailBooking data) {
        String idBooking = String.valueOf(data.data.id);
        String keluar = StaticController.getCurrentDate("yyyy-MM-dd HH:mm:ss");
        String lokasi_lantai_parkir_id = String.valueOf(data.data.lokasi_lantai_parkir.id);
        int hour = StaticController.checkDuration(keluar, data.data.masuk);
        int second = StaticController.timeToSecond(keluar, data.data.masuk);
        int total_harga = DEFAULT_PRICE * hour;
        String dateMasuk = StaticController.dateFormatted(data.data.masuk, "yyyy-MM-dd HH:mm:ss", "d MMM yyyy");
        String parkingTime = StaticController.getDurationString(second);
        String parkingSpot = data.data.lokasi_lantai_parkir.spot;
        String parkingFee = StaticController.getFormatCurrency().format(DEFAULT_PRICE) + " x " + hour;
        String total_harga_formatted = StaticController.getFormatCurrency().format(DEFAULT_PRICE * hour);
        String location = data.data.lokasi.nama_lokasi;

        updateEmployee(lokasi_lantai_parkir_id, keluar, total_harga, idBooking, dateMasuk, parkingTime, parkingSpot, parkingFee, total_harga_formatted, location);
    }

    private void getDataBooking(String code) {
        StaticController.api.findBookingByCode("Bearer " + token, code)
                .enqueue(new Callback<DetailBooking>() {
                    @Override
                    public void onResponse(Call<DetailBooking> call, Response<DetailBooking> response) {
                        if (response.code() == 200) {
                            DetailBooking data = response.body();
                            if (data.data != null) {
                                processData(data);
                            } else {
                                Toast.makeText(context, getString(R.string.data_not_found), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DetailBooking> call, Throwable t) {
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initAction() {
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.inputTicketNumber.getText().toString();

                if (code.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_ticket_number), Toast.LENGTH_SHORT).show();
                } else {
                    getDataBooking(code);
                }
            }
        });
    }
}