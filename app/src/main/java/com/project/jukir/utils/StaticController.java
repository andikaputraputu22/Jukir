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
    public static String FROM_ECARD_PAY_EMPLOYEE = "ecardPayEmployeePage";
    public static String FROM_CASH_PAY_EMPLOYEE = "cashPayEmployeePage";

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

    public static String dateFormatted(String date, String oldPattern, String newPattern) {
        String result = "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern);
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern);
        try {
            Date parseDate = sdf1.parse(date);
            long timestamp = parseDate.getTime();
            result = sdf2.format(new Date(timestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int checkDuration(String currentDate, String lastDate) {
        int result = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date oldDate = dateFormat.parse(lastDate);
            Date newDate = dateFormat.parse(currentDate);
            double diff = newDate.getTime() - oldDate.getTime();
            double seconds = diff/1000;
            double minutes = seconds/60;
            double hours = minutes/60;
            result = round(hours);

            return result;
        } catch (ParseException e) {
            return result;
        }
    }

    public static int timeToSecond(String currentDate, String lastDate) {
        int result = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date oldDate = dateFormat.parse(lastDate);
            Date newDate = dateFormat.parse(currentDate);
            long diff = newDate.getTime() - oldDate.getTime();
            long seconds = diff/1000;
            result = (int) seconds;

            return result;
        } catch (ParseException e) {
            return result;
        }
    }

    public static int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - (double) i;
        if (result < 0.1) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    public static String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    public static String getDurationString(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }
}
