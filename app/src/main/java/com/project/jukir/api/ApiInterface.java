package com.project.jukir.api;

import com.project.jukir.models.LocationDetailModel;
import com.project.jukir.models.LocationModel;
import com.project.jukir.models.LoginModel;
import com.project.jukir.models.RegisterModel;
import com.project.jukir.models.TopupModel;
import com.project.jukir.models.UpdateProfileModel;
import com.project.jukir.models.WalletModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> login(@Field("username") String username,
                           @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<RegisterModel> register(@Field("username") String username,
                                 @Field("email") String email,
                                 @Field("password") String password,
                                 @Field("password_confirmation") String password_confirmation,
                                 @Field("telepon") String telepon);

    @GET("lokasi/list")
    Call<LocationModel> listBuilding(@Header("Authorization") String token,
                                     @Query("keyword") String keyword);

    @GET("lokasi/find/{id}")
    Call<LocationDetailModel> findBuilding(@Header("Authorization") String token,
                                           @Path("id") String id);

    @FormUrlEncoded
    @POST("profile/update")
    Call<UpdateProfileModel> updateProfile(@Header("Authorization") String token,
                                           @Field("old_password") String old_password,
                                           @Field("password") String password,
                                           @Field("password_confirmation") String password_confirmation);

    @GET("metode-pembayaran/list")
    Call<WalletModel> listWallet(@Header("Authorization") String token);

    @FormUrlEncoded
    @POST("profile/topup")
    Call<TopupModel> topupSaldo(@Header("Authorization") String token,
                                @Field("metode_pembayaran_id") String metode_pembayaran_id,
                                @Field("jumlah") String jumlah);
}
