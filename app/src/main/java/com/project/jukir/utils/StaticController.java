package com.project.jukir.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class StaticController {

    public final static String SERVER = "https://jukir.softmedia.id/api/";

    public static Boolean isLogin(Context context) {
        try {
            return SharedPreference.getSharedPreference(context, "loginStatus").equals("1");
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
