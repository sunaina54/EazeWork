package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 08-02-2018.
 */

public class AttendanceRejectResponseModel implements Serializable {
    private RejectRequestResult RejectRequestResult;
    private RejectRequestResult ApproveRequestResult;

    public hr.eazework.com.model.RejectRequestResult getApproveRequestResult() {
        return ApproveRequestResult;
    }

    public void setApproveRequestResult(hr.eazework.com.model.RejectRequestResult approveRequestResult) {
        ApproveRequestResult = approveRequestResult;
    }

    public hr.eazework.com.model.RejectRequestResult getRejectRequestResult() {
        return RejectRequestResult;
    }

    public void setRejectRequestResult(hr.eazework.com.model.RejectRequestResult rejectRequestResult) {
        RejectRequestResult = rejectRequestResult;
    }

    static public AttendanceRejectResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AttendanceRejectResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
