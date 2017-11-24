package hr.eazework.com.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemberApprovalModel {
    private String mApprovalLevel;
    private String mInitiator;
    private String mName;
    private String mReqCode;
    private String mReqDate;
    private String mReqID;
    private String mReqStatus;
    private ArrayList<MemberApprovalModel> mPendingLeaveList;
    public MemberApprovalModel(JSONArray jsonArray) {
        mPendingLeaveList=new ArrayList<>();
        if(jsonArray!=null){
            for (int i = 0; i < jsonArray.length(); i++) {
                mPendingLeaveList.add(new MemberApprovalModel(jsonArray.optJSONObject(i)));
            }
        }
    }

    public MemberApprovalModel(JSONObject jsonObject) {
                mApprovalLevel = jsonObject.optString("ApprovalLevel","");
                mInitiator = jsonObject.optString("Initiator","");
                mName = jsonObject.optString("Name","");
                mReqCode = jsonObject.optString("ReqCode","");
                mReqDate = jsonObject.optString("ReqDate","");
                mReqID = jsonObject.optString("ReqID","");
                mReqStatus = jsonObject.optString("ReqStatus","");
    }


    public String getmApprovalLevel() {
        return mApprovalLevel;
    }

    public void setmApprovalLevel(String mApprovalLevel) {
        this.mApprovalLevel = mApprovalLevel;
    }

    public String getmInitiator() {
        return mInitiator;
    }

    public void setmInitiator(String mInitiator) {
        this.mInitiator = mInitiator;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmReqCode() {
        return mReqCode;
    }

    public void setmReqCode(String mReqCode) {
        this.mReqCode = mReqCode;
    }

    public String getmReqDate() {
        return mReqDate;
    }

    public void setmReqDate(String mReqDate) {
        this.mReqDate = mReqDate;
    }

    public String getmReqID() {
        return mReqID;
    }

    public void setmReqID(String mReqID) {
        this.mReqID = mReqID;
    }

    public String getmReqStatus() {
        return mReqStatus;
    }

    public void setmReqStatus(String mReqStatus) {
        this.mReqStatus = mReqStatus;
    }

    public ArrayList<MemberApprovalModel> getmPendingLeaveList() {
        return mPendingLeaveList;
    }

    public void setmPendingLeaveList(ArrayList<MemberApprovalModel> mPendingLeaveList) {
        this.mPendingLeaveList = mPendingLeaveList;
    }

}
