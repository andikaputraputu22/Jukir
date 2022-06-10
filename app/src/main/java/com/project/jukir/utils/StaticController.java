package com.project.jukir.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.project.jukir.api.ApiInterface;
import com.project.jukir.api.Client;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StaticController {

    public final static String SERVER = "https://jukir.softmedia.id/api/";
    public final static String URL_PHOTO = "https://jukir.softmedia.id/photo/lokasi/";
    public final static String URL_PHOTO_WALLET = "https://jukir.softmedia.id/photo/metode-pembayaran/";
    public final static ApiInterface api = Client.getClient().create(ApiInterface.class);

    public static String KEY_IS_LOGIN = "loginStatus";
    public static String KEY_TOKEN = "tokenUser";
    public static String KEY_ROLE = "roleUser";
    public static String KEY_NAME = "nameUser";
    public static String KEY_EMAIL = "emailUser";
    public static String KEY_PASSWORD = "passwordUser";
    public static String KEY_ID_USER = "idUser";

    public static String FROM_CHANGE_PASSWORD = "changePasswordPage";
    public static String FROM_TOPUP = "topupPage";

    public static int IS_ADMIN = 0;
    public static int IS_EMPLOYEE = 1;
    public static int IS_USER = 2;

    public static String ROLE_ADMIN = "0";
    public static String ROLE_EMPLOYEE = "1";
    public static String ROLE_USER = "2";

    public static Boolean isLogin(Context context) {
        try {
            return SharedPreference.getSharedPreference(context, KEY_IS_LOGIN).equals("1");
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isUser(Context context) {
        try {
            return SharedPreference.getSharedPreference(context, KEY_ROLE).equals("2");
        } catch (Exception e) {
            return false;
        }
    }

    public static String roleUser(Context context) {
        try {
            return SharedPreference.getSharedPreference(context, KEY_ROLE);
        } catch (Exception e) {
            return "";
        }
    }

    public static ProgressDialog showProgressDialog(ProgressDialog progressDialog, Context context, String message) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static NumberFormat getFormatCurrency() {
        NumberFormat numberFormat;
        Locale locale = new Locale("in", "ID");
        numberFormat = NumberFormat.getCurrencyInstance(locale);
        return numberFormat;
    }

    public static String getCurrentDate(String pattern) {
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date());
    }

    public static String getFormattedDateSimple(Long dateTime, String pattern) {
        SimpleDateFormat newFormat = new SimpleDateFormat(pattern);
        return newFormat.format(new Date(dateTime));
    }

    public static String dateFormatted(String date) {
        String result = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parseDate = sdf1.parse(date);
            long timestamp = parseDate.getTime();
            result = sdf2.format(new Date(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
