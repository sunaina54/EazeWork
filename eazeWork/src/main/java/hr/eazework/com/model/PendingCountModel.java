package hr.eazework.com.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Manjunath on 05-04-2017.
 */

public class PendingCountModel {
    private String mCount;
    private String mPendingCount;
    private String mReqDesc;
    private String mReqType;
    private ArrayList<PendingCountModel> pendingList;
    private Map<String,String> mCounts = new HashMap<>();

    public PendingCountModel(JSONArray jsonArray) {
        pendingList=new ArrayList<>();
        if(jsonArray!=null){
            for (int i = 0; i < jsonArray.length(); i++) {
                PendingCountModel pendingCountModel = new PendingCountModel(jsonArray.optJSONObject(i));
                pendingList.add(pendingCountModel);
                mCounts.put(pendingCountModel.getmReqDesc(),pendingCountModel.getmCount());
            }
        }
    }

    public PendingCountModel(JSONObject jsonObject) {
        mCount = jsonObject.optString("Count","");
        mReqDesc = jsonObject.optString("ReqDesc","");
        mReqType = jsonObject.optString("ReqType","");
    }

    public String getmPendingCount() {
        return mPendingCount;
    }

    public void setmPendingCount(String mPendingCount) {
        this.mPendingCount = mPendingCount;
    }

    public String getmCount() {
        return mCount;
    }

    public void setmCount(String mCount) {
        this.mCount = mCount;
    }

    public String getmReqDesc() {
        return mReqDesc;
    }

    public void setmReqDesc(String mReqDesc) {
        this.mReqDesc = mReqDesc;
    }

    public String getmReqType() {
        return mReqType;
    }

    public void setmReqType(String mReqType) {
        this.mReqType = mReqType;
    }

    public ArrayList<PendingCountModel> getPendingList() {
        return pendingList;
    }

    public void setPendingList(ArrayList<PendingCountModel> pendingList) {
        this.pendingList = pendingList;
    }

    public Map<String, String> getmCounts() {
        return mCounts;
    }
}
