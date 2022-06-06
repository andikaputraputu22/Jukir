package com.project.jukir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.project.jukir.databinding.ActivityChangePasswordBinding;
import com.project.jukir.models.UpdateProfileModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private Context context;
    private ActivityChangePasswordBinding binding;
    private ProgressDialog progressDialog;
    private String my_password, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

        token = SharedPreference.getSharedPreference(context, StaticController.KEY_TOKEN);
        my_password = SharedPreference.getSharedPreference(context, StaticController.KEY_PASSWORD);

        initAction();
        initBack();
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

    private void initAction() {
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = binding.inputOldPassword.getText().toString();
                String newPassword = binding.inputNewPassword.getText().toString();
                String reNewPassword = binding.inputReNewPassword.getText().toString();

                if (oldPassword.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_old_password), Toast.LENGTH_SHORT).show();
                } else if (newPassword.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_new_password), Toast.LENGTH_SHORT).show();
                } else if (reNewPassword.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_re_new_password), Toast.LENGTH_SHORT).show();
                } else {
                    if (!oldPassword.equals(my_password)) {
                        Toast.makeText(context, getString(R.string.wrong_old_password), Toast.LENGTH_SHORT).show();
                    } else {
                        if (!newPassword.equals(reNewPassword)) {
                            Toast.makeText(context, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
                        } else {
                            changePassword(oldPassword, newPassword, reNewPassword);
                        }
                    }
                }
            }
        });
    }

    private void changePassword(String oldPassword, String newPassword, String reNewPassword) {
        progressDialog = StaticController.showProgressDialog(progressDialog, context, getString(R.string.loading));
        progressDialog.show();
        StaticController.api.updateProfile("Bearer " + token, oldPassword, newPassword, reNewPassword)
                .enqueue(new Callback<UpdateProfileModel>() {
                    @Override
                    public void onResponse(Call<UpdateProfileModel> call, Response<UpdateProfileModel> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            UpdateProfileModel data = response.body();
                            if (data.status == 200) {
                                SharedPreference.clearSharedPreference(context);
                                Intent intent = new Intent(context, AlertActivity.class);
                                intent.putExtra("fromWhere", StaticController.FROM_CHANGE_PASSWORD);
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
                    public void onFailure(Call<UpdateProfileModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}