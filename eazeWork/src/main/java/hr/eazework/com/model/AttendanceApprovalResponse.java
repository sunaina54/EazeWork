package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-02-2018.
 */

public class AttendanceApprovalResponse implements Serializable {
    private GetEmpPendingApprovalAttendReqsResult GetEmpPendingApprovalAttendReqsResult;

    public hr.eazework.com.model.GetEmpPendingApprovalAttendReqsResult getGetEmpPendingApprovalAttendReqsResult() {
        return GetEmpPendingApprovalAttendReqsResult;
    }

    public void setGetEmpPendingApprovalAttendReqsResult(hr.eazework.com.model.GetEmpPendingApprovalAttendReqsResult getEmpPendingApprovalAttendReqsResult) {
        GetEmpPendingApprovalAttendReqsResult = getEmpPendingApprovalAttendReqsResult;
    }

    static public AttendanceApprovalResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AttendanceApprovalResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
