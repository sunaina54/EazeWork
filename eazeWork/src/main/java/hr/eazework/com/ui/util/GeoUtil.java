package hr.eazework.com.ui.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.geofence.GeolocationService;
import hr.eazework.com.model.GeoCoderModel;
import hr.eazework.com.model.SimpleGeofence;
import hr.eazework.com.model.SimpleGeofenceStore;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;

/**
 * Created by Manjunath on 27-04-2017.
 */

public class GeoUtil {

    private static final String GEO_CODER_API_URL = "https://maps.googleapis.com/maps/api/geocode/json?language=en-IN";
    private static final String TAG = GeoUtil.class.getName();

    public static int userLocationStatus(Location location, Preferences preferences) {
        float[] result = new float[1];
        float distance = 0;
        boolean isInLocation = false;
        int siteRadius = 0;
        double lat1 = location.getLatitude();
        double lon1 = location.getLongitude();
        double lat2 = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double lon2 = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));
        boolean isOnPremise = preferences.getBoolean(Preferences.ISONPREMISE, false);
        try {
            siteRadius = Integer.parseInt(preferences.getString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS));
        } catch (NumberFormatException e) {
            Crashlytics.logException(e);
        }
        if ((lat2 == 0 || lon2 == 0) && isOnPremise) {
            isInLocation = false;
        } else {
            Location.distanceBetween(lat1, lon1, lat2, lon2, result);
            distance = result[0];
            isInLocation = distance <= siteRadius;
        }

        if (isInLocation) {
            return 1;
        } else {
            return 2;
        }
    }


    public static boolean isUserInLocation(Preferences preferences, Location location, Activity activity) {

        float[] result = new float[1];
        double lat1;
        double lon1;
        if (location == null) {
            lat1 = StringUtils.getDouble(preferences.getString(Preferences.USERLATITUDE, ""));
            lon1 = StringUtils.getDouble(preferences.getString(Preferences.USERLONGITUDE, ""));
        } else {
            lat1 = location.getLatitude();
            lon1 = location.getLongitude();
        }
        double lat2 = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double lon2 = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));
        boolean isOnPremise = preferences.getBoolean(Preferences.ISONPREMISE, false);
        int siteRadius = 0;
        float distance = 0;
        boolean isInLocation = false;
        if ((lat2 == 0 || lon2 == 0) && isOnPremise) {
            if (activity != null) {
                Toast.makeText(activity, "Location co-ordinates are not defined", Toast.LENGTH_SHORT).show();
            }
            isInLocation = false;
        } else {
            try {
                siteRadius = Integer.parseInt(preferences.getString(Preferences.SITE_RADIUS, "0"));
            } catch (NumberFormatException e) {
                Crashlytics.logException(e);
            }
            Location.distanceBetween(lat1, lon1, lat2, lon2, result);
            distance = result[0];
            isInLocation = distance <= siteRadius;
        }
        //Double distance = distance(lat1, lon1, lat2, lon2);

        if (isInLocation) {
            preferences.saveBoolean(Preferences.INLOCATION, true);
            preferences.saveString(Preferences.LOCATIONSTATUS, "False");
        } else {
            preferences.saveBoolean(Preferences.INLOCATION, false);
            preferences.saveString(Preferences.LOCATIONSTATUS, "True");
        }
        preferences.commit();

        return isInLocation;
    }

    public static void registerGeofences(Context context, ResultCallback resultCallback, Preferences preferences, GoogleApiClient mGoogleApiClient, PendingIntent mPendingIntent) {
        if (MainActivity.geofencesAlreadyRegistered) {
            Log.d(MainActivity.TAG, "Within Geofence Register");
            return;
        }
        Log.d(MainActivity.TAG, "Outside Geofence Register");
        Log.d(MainActivity.TAG, "Registering Geofences");
        String siteName = preferences.getString(Preferences.SITENAME, "");
        double latitude = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double longitude = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));
        int radius = StringUtils.getInt(preferences.getString(Preferences.SITE_RADIUS, AppsConstant.DEFAULTRADIUS));

        SimpleGeofenceStore simpleGeofenceStore = SimpleGeofenceStore.getInstance(siteName, latitude, longitude, radius);
        HashMap<String, SimpleGeofence> geofences = simpleGeofenceStore.getSimpleGeofences();

        GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();
            Geofence geofence = sg.toGeofence();
            if (geofence != null) {
                geofencingRequestBuilder.addGeofence(geofence);
            }
        }


        try {
            GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();
            if (PermissionUtil.checkLocationPermission(context)) {
                GeofencingApi geofencingApi = LocationServices.GeofencingApi;
                PendingResult<Status> statusPendingResult = geofencingApi.addGeofences(mGoogleApiClient, geofencingRequest, mPendingIntent);
                statusPendingResult.setResultCallback(resultCallback);
            }
        } catch (IllegalArgumentException e) {
            Crashlytics.logException(e);
        }

        MainActivity.geofencesAlreadyRegistered = true;
    }

    public static String getGeoCoderUrl(double latitude, double longitude) {
        return GEO_CODER_API_URL + "&latlng=" + latitude + "," + longitude;
    }

    public static final String convert(double latitude) {
        StringBuilder sb = new StringBuilder(20);
        latitude = Math.abs(latitude);
        final int degree = (int) latitude;
        latitude *= 60;
        latitude -= degree * 60.0d;
        final int minute = (int) latitude;
        latitude *= 60;
        latitude -= minute * 60.0d;
        final int second = (int) (latitude * 1000.0d);
        sb.setLength(0);
        sb.append(degree);
        sb.append("/1,");
        sb.append(minute);
        sb.append("/1,");
        sb.append(second);
        sb.append("/1000,");
        return sb.toString();
    }

    private static Location getLocationForLollipop(Context context) {
        GPSTracker gpsTracker = new GPSTracker(context);
        return gpsTracker.getLocation();
    }

    private static Location getLocationForMarshmallow(Context context, GoogleApiClient googleApiClient, final Fragment fragment) {
        boolean gps_enabled = false;
        boolean network_enabled = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;

        String TAG = fragment.getClass().getName();
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Crashlytics.log(1, TAG, e.getMessage());
            Crashlytics.logException(e);
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception e) {

            Crashlytics.log(1, TAG, e.getMessage());
            Crashlytics.logException(e);
        }

        if (!googleApiClient.isConnected()) {
            googleApiClient.connect();
        }

        if (!gps_enabled && !network_enabled) {
            new AlertCustomDialog(context, "Location is not enabled",
                    "Open Settings",
                    context.getString(R.string.dlg_cancel),
                    context.getString(R.string.dlg_confirm), new AlertCustomDialog.AlertClickListener() {

                @Override
                public void onPositiveBtnListener() {
                    Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    fragment.startActivity(myIntent);
                }

                @Override
                public void onNegativeBtnListener() {
                    // TODO Auto-generated method stub

                }
            });
        } else {

            if (PermissionUtil.checkLocationPermission(context)) {
                loc = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            }

            if (gps_enabled) {
                if (loc == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) fragment);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    }
                }

            }
            if (network_enabled) {
                if (loc == null) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) fragment);
                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

                }
            }
        }
        return loc;
    }

    public static Location getLocation(Context context, GoogleApiClient googleApiClient, Fragment fragment) {
        return getLocationForMarshmallow(context, googleApiClient, fragment);
    }

    public static String fetchAddressFromGeoCoder(String[] params) {
        String response = "";
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "mozilla");//
            connection.setDoOutput(true);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                response = buffer.toString();
            } else {
                InputStream stream = connection.getErrorStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder buffer = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                response = buffer.toString();
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
                Crashlytics.logException(e);
            }
        }
        return response;
    }

    public static GeoCoderModel getGeoCoderModel(String geoCoderResponse) {
        String result = geoCoderResponse;
        GeoCoderModel address = null;
        JSONObject parentObj = null;
        try {
            parentObj = new JSONObject(result);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            Crashlytics.logException(e);
        }
        if(parentObj != null) {
            address = new GeoCoderModel(parentObj.optJSONArray("results"));
        }

        return address;
    }

    public static boolean isBetterLocation(String previousLocationAccuracy, String currentLocationAccuracy) {

        if (TextUtils.isEmpty(previousLocationAccuracy)) {
            return true;
        }

        int accPrevious = 0;
        int accCurrent = 0;
        try {
            accPrevious = (int) Double.parseDouble(previousLocationAccuracy);
            accCurrent = (int) Double.parseDouble(currentLocationAccuracy);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
            Crashlytics.logException(e);
        }

        boolean isMoreAccurate = accCurrent < accPrevious;

        if (accCurrent < 100 && isMoreAccurate) {
            return true;
        } else if (isMoreAccurate) {
            return true;
        } else if (accCurrent < 100) {
            return true;
        } else {
            return false;
        }
    }


}
