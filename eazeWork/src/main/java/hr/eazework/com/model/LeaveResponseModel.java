package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 01-02-2018.
 */

public class LeaveResponseModel implements Serializable {
    private ApproveLeaveRequestResult ApproveLeaveRequestResult;
    private ApproveLeaveRequestResult SaveLeaveReqResult;

    public hr.eazework.com.model.ApproveLeaveRequestResult getSaveLeaveReqResult() {
        return SaveLeaveReqResult;
    }

    public void setSaveLeaveReqResult(hr.eazework.com.model.ApproveLeaveRequestResult saveLeaveReqResult) {
        SaveLeaveReqResult = saveLeaveReqResult;
    }

    public hr.eazework.com.model.ApproveLeaveRequestResult getApproveLeaveRequestResult() {
        return ApproveLeaveRequestResult;
    }

    public void setApproveLeaveRequestResult(hr.eazework.com.model.ApproveLeaveRequestResult approveLeaveRequestResult) {
        ApproveLeaveRequestResult = approveLeaveRequestResult;
    }


    static public LeaveResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LeaveResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
