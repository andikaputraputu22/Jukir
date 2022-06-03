package com.project.jukir.api;

import com.project.jukir.utils.StaticController;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;
    private static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    private static final OkHttpClient okHttpClient = new OkHttpClient();

    public static Retrofit getClient() {
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(StaticController.SERVER)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
