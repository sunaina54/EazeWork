package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 01-02-2018.
 */

public class LeaveRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private LeaveReqDetailModel leaveReqDetail;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public LeaveReqDetailModel getLeaveReqDetail() {
        return leaveReqDetail;
    }

    public void setLeaveReqDetail(LeaveReqDetailModel leaveReqDetail) {
        this.leaveReqDetail = leaveReqDetail;
    }

    static public LeaveRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LeaveRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
