package com.project.jukir.api;

import com.project.jukir.utils.StaticController;
import com.project.jukir.utils.TLSSocketFactory;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {

    private static Retrofit retrofit = null;
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
    private static OkHttpClient okHttpClient = new OkHttpClient();

    static {
        try {
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(new TLSSocketFactory())
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(httpLoggingInterceptor)
                    .build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

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
