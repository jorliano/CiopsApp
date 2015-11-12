package br.com.jortec.ciopsapp.extra;

import android.content.Context;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Jorliano on 02/11/2015.
 */
public class VerificaConecao {

    public static boolean verifyConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean verifyGps(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isOn = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isOn) {
            return true;
        } else {
            return false;
        }
    }
}
