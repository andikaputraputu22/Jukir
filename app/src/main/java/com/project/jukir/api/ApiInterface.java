package com.project.jukir.api;

import com.project.jukir.models.LocationModel;
import com.project.jukir.models.LoginModel;
import com.project.jukir.models.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("lokasi/list")
    Call<LocationModel> listBuilding(@Header("Authorization") String token,
                                     @Query("keyword") String keyword);
}
