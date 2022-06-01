package com.project.jukir.api;

import com.project.jukir.models.LoginModel;
import com.project.jukir.models.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> login(@Field("username") String username,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<RegisterModel> register(@Field("nama") String nama,
                                 @Field("username") String username,
                                 @Field("email") String email,
                                 @Field("password") String password,
                                 @Field("password_confirmation") String password_confirmation);
}
