package hr.eazework.com.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Manjunath on 28-03-2017.
 */

public class StoreLocationModel extends BaseAppResponseModel {
    private String mOfficeName;
    private boolean mActive;
    private String mLatitude;
    private String mLongitude;
    private String mOfficeCode;
    private String mOfficeID;
    private String mStatus;
    private String mEmpCount;
    private ArrayList<StoreLocationModel> mStoreList = new ArrayList<>();

    public boolean ismActive() {
        return mActive;
    }

    public ArrayList<StoreLocationModel> getmStoreList() {
        return mStoreList;
    }

    public void setmStoreList(ArrayList<StoreLocationModel> mStoreList) {
        this.mStoreList = mStoreList;
    }

    public StoreLocationModel(String response) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            mOfficeName = jsonObject.optString("OfficeName", "");
            mActive =jsonObject.optString("Active", "N").equalsIgnoreCase("Y");
            mLatitude =jsonObject.optString("Longitude","");
            mLongitude =jsonObject.optString("LeaveDesc", "N");
            mOfficeCode =jsonObject.optString("OfficeCode", "N");
            mOfficeID =jsonObject.optString("OfficeID", "N");
            mEmpCount = jsonObject.optString("EmpCount","");
            mStatus =jsonObject.optString("Status", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public StoreLocationModel(JSONArray array) {

        if(array!=null){
            for (int i = 0; i < array.length(); i++) {
                try {
                    mStoreList.add(new StoreLocationModel(array.getJSONObject(i).toString()));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public String getmEmpCount() {
        return mEmpCount;
    }

    public void setmEmpCount(String mEmpCount) {
        this.mEmpCount = mEmpCount;
    }

    public String getmOfficeName() {
        return mOfficeName;
    }

    public void setmOfficeName(String mOfficeName) {
        this.mOfficeName = mOfficeName;
    }

    public void setmActive(boolean mActive) {
        this.mActive = mActive;
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

    public String getmOfficeCode() {
        return mOfficeCode;
    }

    public void setmOfficeCode(String mOfficeCode) {
        this.mOfficeCode = mOfficeCode;
    }

    public String getmOfficeID() {
        return mOfficeID;
    }

    public void setmOfficeID(String mOfficeID) {
        this.mOfficeID = mOfficeID;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public StoreLocationModel() {}



}
