package hr.eazework.com.model;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.Geofence;

public class SimpleGeofence {
    private final String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;
    private int mLoiteringDelay = 60000;

    public SimpleGeofence(String geofenceId, double latitude, double longitude,
                          float radius, long expiration, int transition) {
        this.mId = geofenceId;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mRadius = radius;
        this.mExpirationDuration = expiration;
        this.mTransitionType = transition;
    }

    public String getmId() {
        return mId;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public float getmRadius() {
        return mRadius;
    }

    public long getmExpirationDuration() {
        return mExpirationDuration;
    }

    public int getmTransitionType() {
        return mTransitionType;
    }

    public Geofence toGeofence() {
        Geofence g = null;
        try {
            g = new Geofence.Builder().setRequestId(getmId())
                    .setTransitionTypes(mTransitionType)
                    .setCircularRegion(getmLatitude(), getmLongitude(), getmRadius())
                    .setExpirationDuration(mExpirationDuration)
                    .setLoiteringDelay(mLoiteringDelay).build();
        } catch (IllegalArgumentException e) {
            Crashlytics.logException(e);
        } finally {
            return g;
        }
    }
}
