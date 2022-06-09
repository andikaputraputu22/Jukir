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

import com.project.jukir.databinding.ActivityRegisterBinding;
import com.project.jukir.models.RegisterModel;
import com.project.jukir.utils.StaticController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private Context context;
    private ActivityRegisterBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        context = this;

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
        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.inputUsername.getText().toString();
                String email = binding.inputEmail.getText().toString();
                String phoneNumber = binding.inputPhoneNumber.getText().toString();
                String password = binding.inputPassword.getText().toString();
                String rePassword = binding.inputRePassword.getText().toString();

                if (username.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_username), Toast.LENGTH_SHORT).show();
                } else if (email.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_email), Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_phone_number), Toast.LENGTH_SHORT).show();
                } else if (password.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
                } else if (rePassword.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_re_password), Toast.LENGTH_SHORT).show();
                } else {
                    if (!password.equals(rePassword)) {
                        Toast.makeText(context, getString(R.string.password_not_match), Toast.LENGTH_SHORT).show();
                    } else {
                        boolean isCheck = binding.checkAgreement.isChecked();
                        if (isCheck) {
                            register(username, email, phoneNumber, password, rePassword);
                        } else {
                            Toast.makeText(context, getString(R.string.must_agree), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void register(String username, String email, String phoneNumber, String password, String rePassword) {
        progressDialog = StaticController.showProgressDialog(progressDialog, context, getString(R.string.loading));
        progressDialog.show();
        StaticController.api.register(username, email, password, rePassword, phoneNumber)
                .enqueue(new Callback<RegisterModel>() {
                    @Override
                    public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            if (response.body().status == 200) {
                                Toast.makeText(context, getString(R.string.success_register), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, LoginActivity.class);
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
                    public void onFailure(Call<RegisterModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}