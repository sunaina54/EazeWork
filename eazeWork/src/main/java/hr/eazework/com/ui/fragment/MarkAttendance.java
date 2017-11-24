package hr.eazework.com.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import hr.eazework.com.MainActivity;
import hr.eazework.com.R;
import hr.eazework.com.model.CheckInOutModel;
import hr.eazework.com.model.EmployeeProfileModel;
import hr.eazework.com.model.GeoCoderModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.SimpleGeofence;
import hr.eazework.com.model.SimpleGeofenceStore;
import hr.eazework.com.ui.interfaces.IAction;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.AttendanceUtil;
import hr.eazework.com.ui.util.EventDataSource;
import hr.eazework.com.ui.util.GeoCoder;
import hr.eazework.com.ui.util.GeoUtil;
import hr.eazework.com.ui.util.ImageUtil;
import hr.eazework.com.ui.util.PermissionUtil;
import hr.eazework.com.ui.util.Preferences;
import hr.eazework.com.ui.util.StringUtils;
import hr.eazework.com.ui.util.Utility;
import hr.eazework.com.ui.util.custom.AlertCustomDialog;
import hr.eazework.mframe.communication.ResponseData;
import hr.eazework.selfcare.communication.CommunicationConstant;

import static hr.eazework.com.ui.util.AppsConstant.REQ_CAMERA;
import static hr.eazework.com.ui.util.GeoUtil.isUserInLocation;
import static hr.eazework.com.ui.util.PermissionUtil.REQ_PERMISSION;
import static hr.eazework.com.ui.util.PermissionUtil.askLocationPermision;
import static hr.eazework.com.ui.util.PermissionUtil.checkLocationPermission;

/**
 * Created by Manjunath on 20-03-2017.
 */

public class MarkAttendance extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {

    protected EventDataSource dataSource;
    private LocationRequest mLocationRequest;
    protected Marker myPositionMarker;
    protected SupportMapFragment mapFragment;
    private Preferences preferences;
    protected GoogleMap map;
    private GoogleApiClient mGoogleApiClient;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1500;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 5;
    private PendingIntent mPendingIntent;
    private static int counter = 0;
    public static String TAG = "MarkAttendance";
    private String photoPath = "";
    private MainActivity activity;

    private boolean isAtImageAvailable = false;
    private static Context context;

    LinearLayout llLogin_Logout, llShiftTime;
    TextView tvTimeInOut, tvTimeInOutLocation, tvTimeInOutMessage;
    ImageView ivCurrentLocation, ivTimeLineExpand;
    private boolean locationUpdateAtMain;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.setShowPlusMenu(true);
        super.onCreate(savedInstanceState);
        preferences = new Preferences(getContext());
        dataSource = new EventDataSource(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.map_fragment, container, false);
        context = getContext();
        if (preferences == null) {
            preferences = new Preferences(getContext());
        }
        if (dataSource == null) {
            dataSource = new EventDataSource(getContext());
        }

        activity = (MainActivity) getActivity();

        int bgColorCode = Utility.getBgColorCode(getContext(), preferences);
        int textColorCode = Utility.getTextColorCode(preferences);

        ivTimeLineExpand = (ImageView) rootView.findViewById(R.id.ivTimeLineExapand);
        llLogin_Logout = (LinearLayout) rootView.findViewById(R.id.llLogin_Logout);
        tvTimeInOutMessage = (TextView) rootView.findViewById(R.id.tvTimeInOutMessage);
        tvTimeInOut = (TextView) rootView.findViewById(R.id.tvTimeInOut);
        ivCurrentLocation = (ImageView) rootView.findViewById(R.id.ivCurrentLocation);
        tvTimeInOutLocation = (TextView) rootView.findViewById(R.id.tvTimeInOutLocation);
        llShiftTime = (LinearLayout) rootView.findViewById(R.id.llShiftTime);

        try {
            llLogin_Logout.setBackgroundColor(bgColorCode);
            tvTimeInOutMessage.setTextColor(textColorCode);
            tvTimeInOut.setTextColor(textColorCode);
            tvTimeInOutLocation.setTextColor(textColorCode);

        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
            Crashlytics.logException(e);
        }


        mapFragment = SupportMapFragment.newInstance();

        final FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map_container, mapFragment);
        fragmentTransaction.commit();

        buildGoogleApiClient();

        ivCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isLocationEnabled(getContext())) {
                    if (PermissionUtil.checkLocationPermission(getContext())) {
                        Fragment f = getFragmentManager().findFragmentById(R.id.content_frame);
                        if (f instanceof MarkAttendance) {
                            ((MarkAttendance) f).moveToCurentLocation();
                        }
                    } else {
                        PermissionUtil.askLocationPermision(MarkAttendance.this);
                    }
                } else {
                    Utility.requestToEnableGPS(getContext(), preferences);
                }
            }
        });
        llLogin_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isNetworkAvailable(getContext())) {
                    if (preferences == null) {
                        preferences = new Preferences(getContext());
                    }

                    boolean isOnPremise = preferences.getBoolean(Preferences.ISONPREMISE, false);
                    boolean isSelfieRequired = preferences.getBoolean(Preferences.SELFIEYN, false);
                    if (isOnPremise) {
                        String workOrOfficeLocation = preferences.getString(Preferences.SITENAME, "");
                        String workLocationYN = preferences.getString(Preferences.WORKLOCATION_YN, "N");
                        boolean isWorkLocationDefined = workLocationYN.equalsIgnoreCase("Y") && !TextUtils.isEmpty(workOrOfficeLocation);
                        boolean isOfficeLocationDefined = workLocationYN.equalsIgnoreCase("N") && !TextUtils.isEmpty(workOrOfficeLocation);
                        if (isWorkLocationDefined || isOfficeLocationDefined) {
                            boolean isLatitudeEmpty = TextUtils.isEmpty(preferences.getString(Preferences.LATITUDE, ""));
                            if (!isLatitudeEmpty) {
                                if (Utility.isLocationEnabled(getContext())) {

                                    if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext()) || !checkLocationPermission(getContext())) {
                                        PermissionUtil.askAllPermission(MarkAttendance.this);
                                    }

                                    if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext()) && checkLocationPermission(getContext())) {
                                        if (isUserInLocation(preferences, null, getActivity())) {
                                            if (isSelfieRequired) {
                                                Utility.openCamera(getActivity(), MarkAttendance.this, AppsConstant.FRONT_CAMREA_OPEN, "ForPhoto");
                                            } else {
                                                UploadTimeInOut("");
                                            }
                                        } else {
                                            new AlertCustomDialog(getContext(), "Not at location", "Please go to location and try again");
                                        }
                                    } else {
                                        Utility.displayMessage(getContext(), "Please provide all permission");
                                    }

                                } else {
                                    Utility.requestToEnableGPS(getContext(), preferences);
                                }
                            } else {
                                new AlertCustomDialog(getContext(), "Location coordinates not defined");
                            }
                        } else {
                            new AlertCustomDialog(getContext(), "Location not defined");
                        }
                    } else {

                        if (Utility.isLocationEnabled(getContext())) {

                            if (!PermissionUtil.checkCameraPermission(getContext()) || !PermissionUtil.checkStoragePermission(getContext()) || !checkLocationPermission(getContext())) {
                                PermissionUtil.askAllPermission(MarkAttendance.this);
                            }

                            if (PermissionUtil.checkCameraPermission(getContext()) && PermissionUtil.checkStoragePermission(getContext()) && checkLocationPermission(getContext())) {
                                if (isSelfieRequired) {
                                    Utility.openCamera(getActivity(), MarkAttendance.this, AppsConstant.FRONT_CAMREA_OPEN, "ForPhoto");
                                } else {
                                    UploadTimeInOut("");
                                }

                            } else {
                                Utility.displayMessage(getContext(), "Please provide all permission");
                            }
                        } else {
                            Utility.requestToEnableGPS(getContext(), preferences);
                        }
                    }
                } else {
                    Utility.showNetworkNotAvailableDialog(getContext());
                }
            }
        });


        isUserInLocation(preferences, null, getActivity());
        UpdateLocationStatus();

        CheckInOutModel checkInOutModel = ModelManager.getInstance().getCheckInOutModel();
        if (checkInOutModel != null) {

            if (checkInOutModel.isCheckedIn() && !checkInOutModel.isCheckedOut()) {
                tvTimeInOut.setText("Time-Out");
            } else {
                tvTimeInOut.setText("Time-In");
            }
            upDateInOutData(checkInOutModel.isCheckedIn(), checkInOutModel.isCheckedOut());
        }
        return rootView;
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(MainActivity.TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
                return mResources.getString(R.string.geofence_too_many_pending_intents);
            default:
                return mResources.getString(R.string.unknown_geofence_error);
        }
    }

    protected void updateMarker(Double latitude, Double longitude) {
        createMarker(latitude, longitude);
    }

    public void broadcastLocationFound(Location location) {
        if (preferences == null) {
            preferences = new Preferences(getContext());
        }
        preferences.saveString(Preferences.USERLATITUDE, location.getLatitude() + ""); // value to store
        preferences.saveString(Preferences.USERLONGITUDE, location.getLongitude() + ""); // value to store
        preferences.commit();

        updateMarker(location.getLatitude(), location.getLongitude());
    }

    protected void createMarker(Double latitude, Double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        if (myPositionMarker != null) {
            myPositionMarker.remove();
        }
        if (map != null) {
            myPositionMarker = map.addMarker((new MarkerOptions().position(latLng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.user)));
        }
        Log.d(MainActivity.TAG, "User Pointer Lat Long  :  " + latitude + "  " + longitude);

    }


    public void UpdateLocationStatus() {
        boolean inLocation = preferences.getBoolean(Preferences.INLOCATION, false);
        if (TextUtils.isEmpty(preferences.getString(Preferences.LOCATIONSTATUS, ""))) {
            if (inLocation) {
                tvTimeInOutLocation.setText("at " + (preferences.getString(Preferences.SITENAME, "")));
                preferences.saveString(Preferences.LOCATIONSTATUS, "False"); // value to store
                preferences.commit();
            } else {
                tvTimeInOutLocation.setText("(Not at location)");
                preferences.saveString(Preferences.LOCATIONSTATUS, "True"); // value to store
                preferences.commit();
            }
        } else if (inLocation && !preferences.getString(Preferences.LOCATIONSTATUS, "").equalsIgnoreCase("True")) {
            tvTimeInOutLocation.setText("at " + preferences.getString(Preferences.SITENAME, ""));
            preferences.saveString(Preferences.LOCATIONSTATUS, "True"); // value to store
            preferences.commit();
        } else if (!inLocation && !preferences.getString(Preferences.LOCATIONSTATUS, "").equalsIgnoreCase("False")) {
            tvTimeInOutLocation.setText("(Not at location)");
            preferences.saveString(Preferences.LOCATIONSTATUS, "False"); // value to store
            preferences.commit();
        }

        if (preferences.getBoolean(Preferences.ISONPREMISE, false)) {
            tvTimeInOutLocation.setVisibility(View.VISIBLE);
        } else {
            tvTimeInOutLocation.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else {
            Log.e(TAG, "google Api client not connected");
            Crashlytics.log("google Api client not connected");
        }
    }


    protected void startLocationUpdates() {
        if (checkLocationPermission(getContext())) {
            if(mGoogleApiClient != null && mGoogleApiClient.isConnected() && mLocationRequest != null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else if(mGoogleApiClient != null) {
                locationUpdateAtMain = true;
                mGoogleApiClient.connect();
            }
        } else {
            askLocationPermision(this);
        }
    }


    private void getLastKnownLocation() {
        Location lastLocation;
        if (checkLocationPermission(getContext())) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null) {
                updateMarker(lastLocation.getLatitude(), lastLocation.getLongitude());
                if (map != null) {
                    map.animateCamera(CameraUpdateFactory.zoomTo(18));
                }
            }
            startLocationUpdates();
        } else {
            askLocationPermision(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQ_CAMERA) {
                final String strFile = data.getStringExtra("response");
                if (!TextUtils.isEmpty(strFile)) {
                    photoPath = strFile;
                    if(Utility.isLocationEnabled(getContext())) {
                        if (Utility.isNetworkAvailable(getContext())) {
                            boolean geofence = preferences.getBoolean(Preferences.ISONPREMISE, false);
                            if (geofence) {
                                if (checkLocationPermission(getContext())) {
                                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {

                                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                                            @Override
                                            public void onLocationChanged(Location location) {
                                                counter++;
                                                if(counter == 1) {
                                                    counter = 0;
                                                    doUploadAction(location);
                                                    return;
                                                }

                                                if (location != null && location.getAccuracy() <= 50) {
                                                    doUploadAction(location);
                                                } else {
                                                    ((MainActivity) getActivity()).showHideProgress(true);
                                                }
                                            }

                                            private void doUploadAction(Location location) {
                                                ((MainActivity) getActivity()).showHideProgress(false);
                                                if (isUserInLocation(preferences, location, getActivity())) {
                                                    preferences.saveString(Preferences.USERLATITUDE, location.getLatitude() + "");
                                                    preferences.saveString(Preferences.USERLONGITUDE, location.getLongitude() + "");
                                                    preferences.commit();
                                                    UploadTimeInOut(strFile);
                                                } else {
                                                    new AlertCustomDialog(getContext(), "Not at location", "please go to location and try again!");
                                                }
                                            }
                                        });
                                    } else {
                                        isAtImageAvailable = true;
                                        ((MainActivity) getActivity()).showHideProgress(true);
                                        mGoogleApiClient.connect();
                                    }
                                } else {
                                    askLocationPermision(this);
                                }
                            } else {
                                UploadTimeInOut(strFile);
                            }

                            Log.d(MainActivity.TAG, "This was in mark attendance after Upload TimeIn Timeout");
                        } else {
                            new AlertCustomDialog(
                                    getContext(),
                                    getString(R.string.msg_internet_connection));
                        }
                    } else {
                    //    Utility.requestToEnableGPS(getContext(),preferences);
                        new AlertCustomDialog(getContext(), "GPS not enabled");
                    }
                }
            }
        }
    }

    private void UploadTimeInOut(String strFile) {

        photoPath = strFile;
        if (Utility.isNetworkAvailable(getContext())) {

            try {
                String latitude = preferences.getString(Preferences.USERLATITUDE, "");
                String longitude = preferences.getString(Preferences.USERLONGITUDE, "");
                if (!TextUtils.isEmpty(latitude)) {
                    GeoCoderOnAttendance coder = new GeoCoderOnAttendance();
                    coder.execute(GeoUtil.getGeoCoderUrl(StringUtils.getDouble(latitude), StringUtils.getDouble(longitude)));
                } else {
                    Log.d(MarkAttendance.TAG, "Couldn't get location");
                    Crashlytics.log("Couldn't get location");
                    Utility.displayMessage(getContext(),"Couldn't get location, Please try again");
                }

            } catch (Exception e) {
                Crashlytics.log(e.getMessage());
                Crashlytics.logException(e);
            }

        }

    }

    private void upDateInOutData(boolean isCheckedIn, boolean isCheckedOut) {

        tvTimeInOutMessage.setVisibility(View.GONE);
        tvTimeInOut.setVisibility(View.VISIBLE);


        CheckInOutModel model = ModelManager.getInstance().getCheckInOutModel();

        if (isCheckedIn && !isCheckedOut) {
            if (model != null && model.isMarkAttandanceEnable()) {
                tvTimeInOut.setText("Time-Out");
                llLogin_Logout.setVisibility(View.VISIBLE);
                llShiftTime.setVisibility(View.VISIBLE);
                tvTimeInOut.setTag(3);
            }
        } else if (isCheckedOut) {
            if (model != null && model.isMarkAttandanceEnable()) {
                tvTimeInOut.setText("Time-In");
                llLogin_Logout.setVisibility(View.VISIBLE);
                llShiftTime.setVisibility(View.GONE);
                tvTimeInOut.setTag(4);
            }
        } else {
            tvTimeInOut.setTag(0);
            llShiftTime.setVisibility(View.GONE);
            tvTimeInOut.setText("Time-In");
            llLogin_Logout.setVisibility(View.VISIBLE);
        }
    }

    public void moveToCurentLocation() {
        if (checkLocationPermission(getContext())) {
            if (map != null) {
                String currentLat = preferences.getString(Preferences.USERLATITUDE, "");
                String currentLong = preferences.getString(Preferences.USERLONGITUDE, "");
                double latitude = StringUtils.getDouble(currentLat);
                double longitude = StringUtils.getDouble(currentLong);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
                Log.d(MainActivity.TAG, currentLat + "   " + currentLong + " moveToCurrentLocation");
            }
        } else {
            askLocationPermision(this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                    //       moveToCurentLocation();
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    double latitude = StringUtils.getDouble(preferences.getString(Preferences.USERLATITUDE, ""));
                    double longitude = StringUtils.getDouble(preferences.getString(Preferences.USERLONGITUDE, ""));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                            LatLng(latitude, longitude), 16));
                    boolean isOnPremise = preferences.getBoolean(Preferences.ISONPREMISE, true);
                    if (isOnPremise) {
                        displayGeofences();
                    }
                }
            });
        }

        if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    protected void displayGeofences() {
        double radiusLat = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double radiusLong = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));
        int siteRadius = StringUtils.getInt(preferences.getString(Preferences.SITE_RADIUS, "0"));
        String siteName = preferences.getString(Preferences.SITENAME, " ");
        HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore.getInstance(siteName, radiusLat, radiusLong, siteRadius).getSimpleGeofences();

        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(sg.getmLatitude(), sg.getmLongitude()))
                    .strokeColor(AppsConstant.GEOFENCE_STROKE_COLOR)
                    .fillColor(AppsConstant.GEOFENCE_FILL_COLOR)
                    .radius(sg.getmRadius());
            map.addCircle(circleOptions);
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            counter ++;
            if(counter == 1) {
                counter = 0;
                doUploadAction(location);
                return;
            }
            if (location != null && location.getAccuracy() <= 50) {
                doUploadAction(location);
            }
        }

        private void doUploadAction(Location location) {
            if (activity != null) {
                activity.showHideProgress(false);
            }
            isAtImageAvailable = false;
            if (isUserInLocation(preferences, location, getActivity())) {
                preferences.saveString(Preferences.USERLATITUDE, location.getLatitude() + "");
                preferences.saveString(Preferences.USERLONGITUDE, location.getLongitude() + "");
                preferences.commit();
                UploadTimeInOut(photoPath);
            } else {
                new AlertCustomDialog(getContext(), "Not at location", "please go to location and try again!");
                LocationServices.FusedLocationApi.flushLocations(mGoogleApiClient);
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener);
            }
        }
    };


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            if (map != null) {
                map.animateCamera(CameraUpdateFactory.zoomTo(18));
            }
            getLastKnownLocation();

            if(locationUpdateAtMain) {
                locationUpdateAtMain = false;
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }

            if (isAtImageAvailable && checkLocationPermission(getContext())) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);
            }

        } else {
            Log.d(MainActivity.TAG, "google api not connected");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        preferences.remove(Preferences.USERLATITUDE);
        preferences.remove(Preferences.USERLONGITUDE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        preferences.remove(Preferences.USERLATITUDE);
        preferences.remove(Preferences.USERLONGITUDE);
        Log.i(MainActivity.TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation();
                }
                break;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        double lat2 = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double lon2 = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));


        if (!(lat2 == 0 || lon2 == 0)) {
            isUserInLocation(preferences, location, getActivity());
        }

        UpdateLocationStatus();
        if (location.getAccuracy() <= 50) {
            broadcastLocationFound(location);
        }
        String details = location.getLatitude() + " " + location.getLongitude() + " " + location.getAccuracy() + " " + location.getProvider();
        Log.d(MainActivity.TAG, "new location : " + details);

        if (!MainActivity.geofencesAlreadyRegistered) {
            registerGeofences();
        }
    }


    protected void registerGeofences() {
        if (MainActivity.geofencesAlreadyRegistered) {
            return;
        }

        Log.d(MainActivity.TAG, "Registering Geofences");

        double siteLatitude = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, ""));
        double siteLongitude = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, ""));
        int siteRadius = StringUtils.getInt(preferences.getString(Preferences.SITE_RADIUS, "0"));
        String siteName = preferences.getString(Preferences.SITENAME, " ");
        HashMap<String, SimpleGeofence> geofences = SimpleGeofenceStore.getInstance(siteName, siteLatitude, siteLongitude, siteRadius).getSimpleGeofences();

        GeofencingRequest.Builder geofencingRequestBuilder = new GeofencingRequest.Builder();
        geofencingRequestBuilder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        for (Map.Entry<String, SimpleGeofence> item : geofences.entrySet()) {
            SimpleGeofence sg = item.getValue();
            Geofence geofence = sg.toGeofence();
            if (geofence !=  null) {
                geofencingRequestBuilder.addGeofence(geofence);
            }
        }

        try {
            GeofencingRequest geofencingRequest = geofencingRequestBuilder.build();

            mPendingIntent = requestPendingIntent();
            if (checkLocationPermission(getContext())) {
                try {
                    LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofencingRequest, mPendingIntent).setResultCallback(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Crashlytics.log(e.getMessage());
                    Crashlytics.logException(e);
                }

            } else {
                askLocationPermision(this);
            }
        } catch (IllegalArgumentException e) {
            Crashlytics.logException(e);
        }

        MainActivity.geofencesAlreadyRegistered = true;
    }

    private PendingIntent requestPendingIntent() {

        if (null != mPendingIntent) {
            return mPendingIntent;
        } else {
            Intent intent = new Intent(AppsConstant.GEOFENCE_ACTION);
            // getContext returning null causing crash
            PendingIntent pendingIntent = null;
            try {
                pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            } catch (Exception e) {
                e.printStackTrace();
                Crashlytics.log(e.getMessage());
            }
            return pendingIntent;
        }
    }

    @Override
    public void validateResponse(ResponseData response) {
        super.validateResponse(response);
        String responseDataStr = response.getResponseData();
        this.showHideProgressView(false);
        MainActivity.isAnimationLoaded = true;
        ((MainActivity) getActivity()).showHideProgress(false);

        try {
            JSONObject responseJSON = new JSONObject(responseDataStr);
            int reqApiId = response.getRequestData().getReqApiId();
            if (reqApiId == CommunicationConstant.API_MARK_ATTANDANCE) {
                JSONObject markAttendanceResultJSON = responseJSON.getJSONObject("MarkAttendanceResult");
                int errorCode = markAttendanceResultJSON.optInt("ErrorCode");
                if (errorCode == 0) {
                    //referesh home fragment now
                    Log.d(MarkAttendance.TAG, "After Map action, view found as " + rootView);
                    mUserActionListener.performUserAction(IAction.HOME_VIEW, null, null);
                } else {
                    new AlertCustomDialog(getContext(), markAttendanceResultJSON.optString("ErrorMessage"), "Ok", true, new AlertCustomDialog.AlertClickListener() {
                        @Override
                        public void onPositiveBtnListener() {
                            mUserActionListener.performUserAction(IAction.HOME_VIEW,null,null);
                        }

                        @Override
                        public void onNegativeBtnListener() {

                        }
                    });
                }
            }
        } catch (JSONException e) {
            Crashlytics.logException(e);
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private class GeoCoderOnAttendance extends GeoCoder {

        private boolean isCriticalDataMissing(String lat, String lon, GeoCoderModel address) {
            if (TextUtils.isEmpty(lat) || TextUtils.isEmpty(lon)) {
                return true;
            } else if (address == null || (address != null && TextUtils.isEmpty(address.getmCompleteAddress()))) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(String geoCoderResponse) {
            //super call will fetch the address
            super.onPostExecute(geoCoderResponse);

            String latitude = preferences.getString(Preferences.USERLATITUDE, "");
            String longitude = preferences.getString(Preferences.USERLONGITUDE, "");
            boolean isSelfieRequired = preferences.getBoolean(Preferences.SELFIEYN, true);
            int reqType = preferences.getInt("currentReqType", -999);

            if (!isCriticalDataMissing(latitude, longitude, address)) {
                EmployeeProfileModel employeeProfileModel = ModelManager.getInstance().getEmployeeProfileModel();
                if (!TextUtils.isEmpty(photoPath) && employeeProfileModel != null) {
                    AttendanceUtil.performAttendanceAction(getActivity(), MarkAttendance.this, reqType, latitude, longitude, "ForPhoto.jpg", ".jpg", photoPath, address.getmCompleteAddress(), employeeProfileModel.getmRadius(), getDistance(latitude, longitude));
                } else {
                    if (isSelfieRequired) {
                        Utility.displayMessage(getContext(), "Selfie required please try again");
                    } else if (employeeProfileModel != null) {
                        AttendanceUtil.performAttendanceAction(getActivity(), MarkAttendance.this, reqType, latitude, longitude, "", "", "", address.getmCompleteAddress(), employeeProfileModel.getmRadius(), getDistance(latitude, longitude));
                    }
                }
            } else {
                if (TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)) {
                    Utility.displayMessage(activity, "Latitude and Longitude not captured please try again");
                } else if (address == null || (address != null && TextUtils.isEmpty(address.getmCompleteAddress()))) {
                    Utility.displayMessage(activity, "Geo address not captured please try again");
                } else {
                    Utility.displayMessage(activity, "Critical data is missing please try again");
                }
            }
        }

        private String getDistance(String lat, String lon) {
            float[] result = new float[1];
            double userLat = StringUtils.getDouble(lat);
            double userLon = StringUtils.getDouble(lon);
            double geoLat = StringUtils.getDouble(preferences.getString(Preferences.LATITUDE, "0"));
            double geoLon = StringUtils.getDouble(preferences.getString(Preferences.LONGITUDE, "0"));

            Location.distanceBetween(userLat, userLon, geoLat, geoLon, result);
            return String.valueOf(result[0]);
        }
    }
}
