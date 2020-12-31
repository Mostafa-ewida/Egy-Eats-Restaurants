package com.itechpro.egyeatsrestaurant.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.itechpro.egyeatsrestaurant.Model.EndUser;
import com.itechpro.egyeatsrestaurant.Model.Request;
import com.itechpro.egyeatsrestaurant.Model.Table;

import java.util.Locale;

public class Common {

    public static EndUser currentUser;

    public static Locale appLocale;

    public static String androidId;
    public static Table table;
    public static String tableId;
    public static Request request;


    //checks the Internet connection within App usage
    public static boolean isConnectedToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info : infos) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Locale getAppLocale(Context context){
        appLocale  = context.getResources().getConfiguration().locale;
        return appLocale;
    }

    public static void setAppLocale(Locale locale){
        appLocale = locale;
    }


}
