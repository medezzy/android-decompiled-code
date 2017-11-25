package in.mobiant.medical.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import in.mobiant.medical.db.MediDBHelper;
import in.mobiant.medical.models.UserInfoItem;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.UUID;

public class Utils {
    static final String monthDate = "monthDate";
    static final String todayDate = "todayDate";
    static final String weekDate = "weekDate";

    public static String getDeviceID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0);
        String uniqueID = sharedPreferences.getString(Variables.DEVICE_ID, "");
        if (uniqueID.equals("")) {
            uniqueID = UUID.randomUUID().toString();
            Log.v(Variables.TAG, "unique ID : " + uniqueID);
            Editor editor = sharedPreferences.edit();
            editor.putString(Variables.DEVICE_ID, uniqueID);
            editor.apply();
        }
        Log.d(Variables.TAG, "uniqueID : " + uniqueID);
        return uniqueID;
    }

    public static boolean isLogin(Context context) {
        return context.getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0).getBoolean(Variables.ISLOGIN, false);
    }

    public static void saveLogin(Context context, boolean isLogin, UserInfoItem userInfoItem) {
        saveData(context, userInfoItem);
        Editor editor = context.getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0).edit();
        editor.putBoolean(Variables.ISLOGIN, isLogin);
        editor.apply();
    }

    public static void saveData(Context context, UserInfoItem userInfoItem) {
        try {
            FileOutputStream fout = new FileOutputStream(new File(context.getFilesDir(), Variables.CUR_USER_OBJECT_FILE));
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(userInfoItem);
            out.flush();
            out.close();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMedicinesDownloaded(Context context) {
        return context.getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0).getBoolean(Variables.ISMEDICINESDOWNLOADED, false);
    }

    public static void saveMedicinesDownloaded(Context context, boolean isDownloaded) {
        Editor editor = context.getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0).edit();
        editor.putBoolean(Variables.ISMEDICINESDOWNLOADED, isDownloaded);
        editor.apply();
    }

    public static void resetReports(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Variables.SHARED_PREF_SUBSC, 0);
        Calendar calendar = Calendar.getInstance();
        if (sharedPreferences.getLong(todayDate, 0) < calendar.getTimeInMillis()) {
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.add(5, 1);
            sharedPreferences.edit().putLong(todayDate, calendar.getTimeInMillis()).apply();
            Log.d(Variables.TAG, "reset day : " + calendar.getTimeInMillis());
            MediDBHelper mediDBHelper = new MediDBHelper(context);
            mediDBHelper.resetBuySell(MediDBHelper.D_BUY);
            mediDBHelper.resetBuySell(MediDBHelper.D_SELL);
        }
        calendar = Calendar.getInstance();
        if (sharedPreferences.getLong(weekDate, 0) < calendar.getTimeInMillis() && calendar.get(7) == 2) {
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.add(5, 7);
            sharedPreferences.edit().putLong(weekDate, calendar.getTimeInMillis()).apply();
            Log.d(Variables.TAG, "reset week : " + calendar.getTimeInMillis());
            mediDBHelper = new MediDBHelper(context);
            mediDBHelper.resetBuySell(MediDBHelper.W_BUY);
            mediDBHelper.resetBuySell(MediDBHelper.W_SELL);
        }
        calendar = Calendar.getInstance();
        if (sharedPreferences.getLong(monthDate, 0) < calendar.getTimeInMillis() && calendar.get(5) == 1) {
            calendar.set(11, 0);
            calendar.set(12, 0);
            calendar.set(13, 0);
            calendar.add(2, 1);
            sharedPreferences.edit().putLong(monthDate, calendar.getTimeInMillis()).apply();
            Log.d(Variables.TAG, "reset month : " + calendar.getTimeInMillis());
            mediDBHelper = new MediDBHelper(context);
            mediDBHelper.resetBuySell(MediDBHelper.M_BUY);
            mediDBHelper.resetBuySell(MediDBHelper.M_SELL);
        }
    }
}
