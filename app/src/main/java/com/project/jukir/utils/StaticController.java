package com.project.jukir.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.project.jukir.api.ApiInterface;
import com.project.jukir.api.Client;

public class StaticController {

    public final static String SERVER = "https://jukir.softmedia.id/api/";
    public final static String URL_PHOTO = "https://jukir.softmedia.id/photo/lokasi/";
    public final static ApiInterface api = Client.getClient().create(ApiInterface.class);

    public static String KEY_IS_LOGIN = "loginStatus";
    public static String KEY_TOKEN = "tokenUser";

    public static Boolean isLogin(Context context) {
        try {
            return SharedPreference.getSharedPreference(context, KEY_IS_LOGIN).equals("1");
        } catch (Exception e) {
            return false;
        }
    }

    public static ProgressDialog showProgressDialog(ProgressDialog progressDialog, Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
