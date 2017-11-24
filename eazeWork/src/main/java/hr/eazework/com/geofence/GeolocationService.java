package hr.eazework.com.geofence;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.HashMap;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.SimpleGeofence;
import hr.eazework.com.model.SimpleGeofenceStore;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.GeoUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.StringUtils;

public class GeolocationService extends Service implements ConnectionCallbacks,OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
	protected GoogleApiClient mGoogleApiClient;
	protected LocationRequest mLocationRequest;
	private Preferences preferences;
	private PendingIntent mPendingIntent;


	@Override
	public void onCreate() {
		preferences = new Preferences(getApplicationContext());
		super.onCreate();
		buildGoogleApiClient();

		mGoogleApiClient.connect();
		Log.d(MainActivity.TAG,"GeolocationService is started");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(MainActivity.TAG,"GeolocationService is destroyed");
		stopLocationUpdates();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

	}


	private PendingIntent requestPendingIntent() {
		if (null != mPendingIntent) {
			return mPendingIntent;
		} else {
			Intent intent = new Intent();
			return PendingIntent.getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		}
	}

    public void broadcastLocationFound(Location location) {
        Intent intent = new Intent(AppsConstant.GEOFENCE_ACTION);
		int geoEvent = GeoUtil.userLocationStatus(location,preferences);
		intent.putExtra(AppsConstant.GEOEVENT,geoEvent);
		sendBroadcast(intent);
	}

	protected void startLocationUpdates() {
		if (PermissionUtil.checkLocationPermission(this) ) {
			LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
		}
	}

	protected void stopLocationUpdates() {
		if (PermissionUtil.checkLocationPermission(this) ) {
            try {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            } catch (IllegalStateException e) {
                Crashlytics.logException(e);
            }
        }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		Log.i(MainActivity.TAG, "Connected to GoogleApiClient");
		if(mGoogleApiClient.isConnected()) {
			startLocationUpdates();
		} else {
			Log.d(MainActivity.TAG,"google api client not connected");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.d(MainActivity.TAG,"GeolocationService new location : " + location.getLatitude() + ", "+ location.getLongitude() + ". "+ location.getAccuracy());

        preferences.saveString(Preferences.USERLATITUDE,location.getLatitude()+"");
        preferences.saveString(Preferences.USERLONGITUDE,location.getLongitude()+"");
        preferences.commit();
        if (!MainActivity.geofencesAlreadyRegistered) {
			Log.d(MainActivity.TAG,"Before Geofence Register");
            if(preferences == null){
                preferences = new Preferences(this);
            }
            mPendingIntent = requestPendingIntent();
            try {
                GeoUtil.registerGeofences(this, this, preferences, mGoogleApiClient, mPendingIntent);
            } catch (IllegalArgumentException e) {
                Crashlytics.logException(e);
            }
		}
	//	broadcastLocationFound(location);
	}

	@Override
	public void onConnectionSuspended(int cause) {
		Log.i(MainActivity.TAG, "Connection suspended");
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.i(MainActivity.TAG,"Connection failed: ConnectionResult.getErrorCode() = "+ result.getErrorCode());
	}

	protected synchronized void buildGoogleApiClient() {
		Log.i(MainActivity.TAG, "Building GoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
		createLocationRequest();
	}

	protected void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onResult(Status status) {
		if (!status.isSuccess()) {
			MainActivity.geofencesAlreadyRegistered = false;
			String errorMessage = getErrorString(this, status.getStatusCode());
			Log.d(MainActivity.TAG,errorMessage);
		}
	}

	public static String getErrorString(Context context, int errorCode) {
		Resources mResources = context.getResources();
		switch (errorCode) {
		case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
			return mResources.getString(R.string.geofence_not_available);
		case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
			return mResources.getString(R.string.geofence_too_many_geofences);
		case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
			return mResources
					.getString(R.string.geofence_too_many_pending_intents);
		default:
			return mResources.getString(R.string.unknown_geofence_error);
		}
	}

}
