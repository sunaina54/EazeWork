package hr.eazework.com.model;

import android.database.Cursor;

import hr.eazework.com.ui.util.SqliteHelper;


/**
 * Created by AllSmart-LT008 on 12/15/2016.
 */

public class TimeInOutDetails {

    private int id;
    private String mUsername;
    private String mClockDate;
    private String mActionType;
    private String mComments;
    private String mLatitude;
    private String mLongitude;
    private String mActionImage;
    private String isPushed;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getmUsername() {
        return mUsername;
    }
    public void setmUsername(String mUsername) {
        this.mUsername = mUsername;
    }
    public String getmClockDate() {
        return mClockDate;
    }
    public void setmClockDate(String mClockDate) {
        this.mClockDate = mClockDate;
    }
    public String getmActionType() {
        return mActionType;
    }
    public void setmActionType(String mActionType) {
        this.mActionType = mActionType;
    }
    public String getmComments() {
        return mComments;
    }
    public void setmComments(String mComments) {
        this.mComments = mComments;
    }
    public String getmLatitude() {
        return mLatitude;
    }
    public void setmLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }
    public String getmLongitude() {
        return mLongitude;
    }
    public void setmLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }
    public String getmActionImage() {
        return mActionImage;
    }
    public void setmActionImage(String mActionImage) {
        this.mActionImage = mActionImage;
    }
    public String getIsPushed() {
        return isPushed;
    }
    public void setIsPushed(String isPushed) {
        this.isPushed = isPushed;
    }
    static public TimeInOutDetails fromCursor(Cursor cursor) {
        TimeInOutDetails data = new TimeInOutDetails();
        if (cursor != null && cursor.getCount() != 0) {
            data.setId(cursor.getInt(cursor.getColumnIndex(SqliteHelper.COLUMN_ID)));
            data.setmUsername(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_USERNAME)));
            data.setmClockDate(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_CLOCKDATE)));
            data.setmActionType(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_ACTIONTYPE)));
            data.setmComments(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_COMMENTS)));
            data.setmLatitude(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_LATITUDE)));
            data.setmLongitude(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_LONGITUDE)));
            data.setmActionImage(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_ACTIONIMAGE)));
            data.setIsPushed(cursor.getString(cursor.getColumnIndex(SqliteHelper.COLUMN_ISPHUSHED)));
        }
        return data;
    }
}
