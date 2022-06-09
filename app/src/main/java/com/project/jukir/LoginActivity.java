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

import com.project.jukir.databinding.ActivityLoginBinding;
import com.project.jukir.models.LoginModel;
import com.project.jukir.utils.SharedPreference;
import com.project.jukir.utils.StaticController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private ActivityLoginBinding binding;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
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
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.inputUsername.getText().toString();
                String password = binding.inputPassword.getText().toString();

                if (username.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_username), Toast.LENGTH_SHORT).show();
                } else if (password.length() == 0) {
                    Toast.makeText(context, getString(R.string.empty_password), Toast.LENGTH_SHORT).show();
                } else {
                    login(username, password);
                }
            }
        });
    }

    private void login(String username, String password) {
        progressDialog = StaticController.showProgressDialog(progressDialog, context, getString(R.string.loading));
        progressDialog.show();
        StaticController.api.login(username, password)
                .enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            LoginModel data = response.body();
                            if (data.status == 200) {
                                Toast.makeText(context, getString(R.string.success_login), Toast.LENGTH_SHORT).show();
                                SharedPreference.writeSharedPreference(context, StaticController.KEY_IS_LOGIN, "1");
                                SharedPreference.writeSharedPreference(context, StaticController.KEY_ROLE, String.valueOf(data.data.user.role));
                                SharedPreference.writeSharedPreference(context, StaticController.KEY_TOKEN, data.data.token);
                                SharedPreference.writeSharedPreference(context, StaticController.KEY_NAME, data.data.user.username);
                                SharedPreference.writeSharedPreference(context, StaticController.KEY_EMAIL, data.data.user.email);
                                SharedPreference.writeSharedPreference(context, StaticController.KEY_PASSWORD, password);
                                if (data.data.user.role == StaticController.IS_ADMIN) {

                                } else if (data.data.user.role == StaticController.IS_EMPLOYEE) {

                                } else {
                                    Intent intent = new Intent(context, MainActivity.class);
                                    startActivity(intent);
                                    finishAffinity();
                                }
                            } else {
                                Toast.makeText(context, getString(R.string.wrong_account), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, getString(R.string.wrong_account), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}