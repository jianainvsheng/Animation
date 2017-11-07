package com.gome.pull.down.widget.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yangjian on 2017/10/26.
 */

public class NetUtility {

    public static final String NO_CONN = "FAIL";

    public NetUtility() {
    }

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager e = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = e.getActiveNetworkInfo();
            return ni != null && ni.isConnectedOrConnecting();
        } catch (Exception var3) {
            return false;
        }
    }
}
