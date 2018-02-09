package hr.eazework.com.ui.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import hr.eazework.com.TimeModificationActivity;
import hr.eazework.com.MainActivity;
import hr.eazework.com.ui.fragment.BaseFragment;
import hr.eazework.com.ui.fragment.MyBaseFragment;

/**
 * Created by Manjunath on 27-04-2017.
 */

public class PermissionUtil {

    public final static int REQ_PERMISSION = 1001;
    public final static int REQ_CAMERA_PERMISSION = 1002;
    public final static int REQ_Storage_PERMISSION = 1000;
    public final static int REQ_LOCATION_PERMISSION = 1004;

    // Check for permission to access Location
    public static boolean checkLocationPermission(Context context) {
        Log.d(MainActivity.TAG, "checkLocationPermission()");
        // Ask for permission if it wasn't granted yet
        if (context != null) {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        } else {
            return false;
        }
    }

    public static void askLocationPermision(BaseFragment baseFragment) {
        if (baseFragment != null) {
            try {
                baseFragment.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_LOCATION_PERMISSION);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
    }

    public static boolean checkCameraPermission(Context context) {
        // Ask for permission if it wasn't granted yet
        if (context != null) {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        } else {
            return false;
        }
    }

    public static boolean checkStoragePermission(Context context) {
        // Ask for permission if it wasn't granted yet
        if (context != null) {
            return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        } else {
            return false;
        }
    }

    public static void askStoragePermission(BaseFragment baseFragment) {
        if (baseFragment != null) {
            baseFragment.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_Storage_PERMISSION);
        }
    }

    public static void askCameraPermission(BaseFragment baseFragment) {
        if (baseFragment != null) {
            baseFragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQ_CAMERA_PERMISSION);
        }
    }

    public static void askAllPermission(BaseFragment baseFragment) {
        if (baseFragment != null) {
            try {
                baseFragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
    }

    public static void askAllPermission(MyBaseFragment baseFragment) {
        if (baseFragment != null) {
            try {
                baseFragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, REQ_PERMISSION);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
    }

    public static void askAllPermissionCamera(BaseFragment baseFragment) {
        if (baseFragment != null) {
            try {
                baseFragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
    }

    public static void askAllPermissionCamera(MyBaseFragment baseFragment) {
        if (baseFragment != null) {
            try {
                baseFragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
    }

    public static void askAllPermissionCamera(TimeModificationActivity baseFragment) {
        if (baseFragment != null) {
            try {
                baseFragment.requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, REQ_PERMISSION);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
    }
}
