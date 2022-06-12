package com.project.jukir.api;

import com.project.jukir.models.DetailBooking;
import com.project.jukir.models.LocationDetailModel;
import com.project.jukir.models.LocationModel;
import com.project.jukir.models.LoginModel;
import com.project.jukir.models.RegisterModel;
import com.project.jukir.models.ReportModel;
import com.project.jukir.models.ResponseSuccess;
import com.project.jukir.models.TopupModel;
import com.project.jukir.models.UpdateProfileModel;
import com.project.jukir.models.WalletModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("report/list-by-date/{date}/{id}")
    Call<ReportModel> adminReport(@Header("Authorization") String token,
                                  @Path("date") String date,
                                  @Path("id") String id);

    @GET("booking/find/{id}")
    Call<DetailBooking> findBooking(@Header("Authorization") String token,
                                    @Path("id") String id);

    @GET("booking/find-by-kode-booking/{code}")
    Call<DetailBooking> findBookingByCode(@Header("Authorization") String token,
                                          @Path("code") String code);

    @Multipart
    @POST("booking/update/{id}")
    Call<ResponseSuccess> updateEmployee(@Header("Authorization") String token,
                                         @Part("lokasi_lantai_parkir_id") RequestBody lokasi_lantai_parkir_id,
                                         @Part("keluar") RequestBody keluar,
                                         @Part("total_harga") RequestBody total_harga,
                                         @Part("employee_id") RequestBody employee_id,
                                         @Path("id") String id);
}
