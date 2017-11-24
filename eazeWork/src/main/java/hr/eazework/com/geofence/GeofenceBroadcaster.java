package hr.eazework.com.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import hr.eazework.com.MainActivity;
import hr.eazework.com.model.CheckInOutModel;
import hr.eazework.com.model.ModelManager;
import hr.eazework.com.model.TimeInOutDetails;
import hr.eazework.com.ui.services.UploadInOutLocationService;
import hr.eazework.com.ui.util.AppsConstant;
import hr.eazework.com.ui.util.CalenderUtils;
import hr.eazework.com.ui.util.EventDataSource;
import hr.eazework.com.ui.util.Preferences;

/**
 * Created by allsmartlt218 on 16-01-2017.
 */

public class GeofenceBroadcaster extends BroadcastReceiver {
    protected Preferences preferences;
    protected EventDataSource dataSource;
    protected Context context;

    private TimeInOutDetails getTimeInOutDetails(String strComments, String strType) {
        String clockDate = CalenderUtils.getCurrentDateWithZone();
        TimeInOutDetails details = new TimeInOutDetails();
        details.setmUsername(preferences.getString(Preferences.USERNAME, ""));
        details.setmClockDate(clockDate);
        details.setmActionType(strType);
        details.setmComments(strComments);
        details.setmLatitude(preferences.getString(Preferences.USERLATITUDE, ""));
        details.setmLongitude(preferences.getString(Preferences.USERLONGITUDE, ""));
        details.setmActionImage("");
        details.setIsPushed("false");
        return details;
    }

    public void uploadData() {
        Intent uploadTransactionIntent = new Intent(context, UploadInOutLocationService.class);
        context.startService(uploadTransactionIntent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = new Preferences(context);
        this.context = context;
//        GeofencingEvent geoEvent = GeofencingEvent.fromIntent(intent);
        int geoEvent = intent.getIntExtra(AppsConstant.GEOEVENT, -1);

        dataSource = new EventDataSource(context);

        if (geoEvent != 1 && geoEvent != 2) {
            Log.d(MainActivity.TAG, "Error GeofenceBroadcaster.onHandleIntent");
        } else {
            Log.d(MainActivity.TAG, "GeofenceBroadcaster : Transition -> " + geoEvent);

            int transitionType = geoEvent;

            if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER || transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
                String strComments = "";
                String clockType = "";
                switch (transitionType) {
                    case Geofence.GEOFENCE_TRANSITION_ENTER:
                        strComments = "InLocation";
                        clockType = "clockIn";
                        preferences.saveBoolean(Preferences.INLOCATION, true);
                        break;
                    case Geofence.GEOFENCE_TRANSITION_EXIT:
                        strComments = "OutLocation";
                        clockType = "clockOut";
                        preferences.saveBoolean(Preferences.INLOCATION, false);
                        break;
                }

                CheckInOutModel checkInOutModel = ModelManager.getInstance().getCheckInOutModel();
                boolean isOnPremise = preferences.getBoolean(Preferences.ISONPREMISE, false);
                if (!TextUtils.isEmpty(clockType) && isOnPremise) {

                    TimeInOutDetails details = dataSource.getLastEntry();
                    if (details != null && TextUtils.isEmpty(details.getmClockDate())) {

                        if (checkInOutModel != null && checkInOutModel.isCheckedIn() && !checkInOutModel.isCheckedOut()) {
     //                       insertAndUploadTimeInOutEntry(strComments, clockType);
                        }

                    } else {
                        String clockDate = CalenderUtils.getCurrentDate(CalenderUtils.DateMonthDashedFormate);
                        if (details != null && !TextUtils.isEmpty(details.getmClockDate())) {
                            String lastDate = CalenderUtils.getDateMethod(details.getmClockDate(), CalenderUtils.DateMonthDashedFormate);
                            String comments = details.getmComments();
                            Log.d(MainActivity.TAG, clockDate + "            from db" + lastDate);

                            if (clockDate.equalsIgnoreCase(lastDate)) {
                                if (strComments.equalsIgnoreCase("InLocation") && comments.equalsIgnoreCase("OutLocation")) {
                                    if (checkInOutModel != null && checkInOutModel.isCheckedIn() && !checkInOutModel.isCheckedOut()) {
       //                                 insertAndUploadTimeInOutEntry(strComments, clockType);
                                    }
                                } else if (strComments.equalsIgnoreCase("OutLocation") && (comments.equalsIgnoreCase("TimeIn") || comments.equalsIgnoreCase("InLocation"))) {
                                    if (checkInOutModel != null && checkInOutModel.isCheckedIn() && !checkInOutModel.isCheckedOut()) {
       //                                 insertAndUploadTimeInOutEntry(strComments, clockType);
                                    }
                                }
                            }
                        }

                    }
                }

            }
            preferences.commit();
        }
    }


    private void insertAndUploadTimeInOutEntry(String strComments, String clockType) {
        dataSource.insertTimeInOutDetails(getTimeInOutDetails(strComments, clockType));
        uploadData();
    }
}

