package hr.eazework.com.ui.util;

import android.os.Build;

import hr.eazework.com.BuildConfig;

/**
 * Created by Manjunath on 01-08-2017.
 */

public class DeviceUtil {
    public static String getOsVersion(){
        return Build.VERSION.RELEASE;
    }

    public static int getAppVersion() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getDeviceMake() {
        return Build.MANUFACTURER + " " + Build.MODEL ;
    }
}
