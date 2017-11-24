package hr.eazework.com.model;

import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class History {
    private String mAttendType;
    private String mMarkDate;
    private String mStatus;
    private String mStatusDesc;
    private String mTimeIn = "";
    private String mTimeOut = "";
    private ArrayList<History> mHistoryList = new ArrayList<>();

    private static History currentHistoryItem = null;

    public History(JSONArray array) {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    mHistoryList.add(new History(array.getJSONObject(i).toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public History(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            mAttendType = object.optString("AttendType", "");
            mMarkDate = object.optString("MarkDate", "");
            mStatus = object.optString("Status", "");
            mStatusDesc = object.optString("StatusDesc", "");
            mTimeIn = object.optString("TimeIn", "");
            mTimeOut = object.optString("TimeOut", "");
        } catch (JSONException e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }

    }


    public String getmAttendType() {
        return mAttendType;
    }

    public void setmAttendType(String mAttendType) {
        this.mAttendType = mAttendType;
    }

    public String getmMarkDate() {
        return mMarkDate;
    }

    public void setmMarkDate(String mMarkDate) {
        this.mMarkDate = mMarkDate;
    }

    public String getmStatus() {
        if(TextUtils.isEmpty(mStatus) || "null".equals(mStatus)) {
            return "";
        } else {
            return mStatus;
        }

    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmStatusDesc() {
        return mStatusDesc;
    }

    public void setmStatusDesc(String mStatusDesc) {
        this.mStatusDesc = mStatusDesc;
    }

    public String getmTimeIn() {
        if(TextUtils.isEmpty(mTimeIn) || mTimeIn.equals("null")){
            return "";
        } else {
            return mTimeIn;
        }
    }

    public void setmTimeIn(String mTimeIn) {
        this.mTimeIn = mTimeIn;
    }

    public String getmTimeOut() {

        if(TextUtils.isEmpty(mTimeOut) || mTimeOut.equals("null")){
            return "";
        } else {
            return mTimeOut;
        }

    }

    public void setmTimeOut(String mTimeOut) {
        this.mTimeOut = mTimeOut;
    }

    public ArrayList<History> getmHistoryList() {
        return mHistoryList;
    }

    public void setmHistoryList(ArrayList<History> mHistoryList) {
        this.mHistoryList = mHistoryList;
    }

    public static History getCurrentHistoryItem() {
        return currentHistoryItem;
    }

    public static void setCurrentHistoryItem(History currentHistoryItem) {
        History.currentHistoryItem = currentHistoryItem;
    }

}
