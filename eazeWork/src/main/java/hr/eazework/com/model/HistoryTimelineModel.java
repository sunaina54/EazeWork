package hr.eazework.com.model;

import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import hr.eazework.com.MainActivity;

/**
 * Created by allsmartlt218 on 16-12-2016.
 */

public class HistoryTimelineModel {
    private String mAttendID;
    private String mDate;
    private String mTime;
    private String mTypeDesc;
    private String mTypeId;
    private ArrayList<HistoryTimelineModel> mTimelineList = new ArrayList<>();

    public HistoryTimelineModel(String json) {
        JSONObject object;
        try {
            object = new JSONObject(json);
            mAttendID = object.optString("AttendID", "");

            // set flag 'D' for mDate, mTime otherwise
            this.mDate = getFormattedDate(object.optString("TimeStamp",""),"D");
            this.mTime = getFormattedDate(object.optString("TimeStamp",""),"");

            Log.d(MainActivity.TAG, mDate + "        " + mTime);
            mTypeDesc = object.optString("TypeDesc", "");
            mTypeId = object.optString("TypeID", "");

        } catch (JSONException e)

        {
            e.printStackTrace();
        }
    }

    public HistoryTimelineModel(JSONArray array) {
        if (array != null) {
            for (int i = 0; i < array.length(); i++) {
                try {
                    mTimelineList.add(new HistoryTimelineModel(array.getJSONObject(i).toString()));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static String getFormattedDate(String timeStamp,String flag) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa ZZZ", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(flag.equalsIgnoreCase("D")) {
            return DateFormat.format("dd-MM-yyyy",date).toString();
        } else {
            return DateFormat.format("hh:mm aaa",date).toString();
        }

    }

    public ArrayList<HistoryTimelineModel> getmTimelineList() {
        return mTimelineList;
    }

    public void setmTimelineList(ArrayList<HistoryTimelineModel> mTimelineList) {
        this.mTimelineList = mTimelineList;
    }

    public String getmAttendID() {
        return mAttendID;
    }

    public void setmAttendID(String mAttendID) {
        this.mAttendID = mAttendID;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmTypeDesc() {
        return mTypeDesc;
    }

    public void setmTypeDesc(String mTypeDesc) {
        this.mTypeDesc = mTypeDesc;
    }

    public String getmTypeId() {
        return mTypeId;
    }

    public void setmTypeId(String mTypeId) {
        this.mTypeId = mTypeId;
    }
}
