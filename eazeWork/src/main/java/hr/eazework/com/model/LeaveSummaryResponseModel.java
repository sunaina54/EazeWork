package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 13-02-2018.
 */

public class LeaveSummaryResponseModel implements Serializable {
    private GetEmpLeaveRequestsResult GetEmpLeaveRequestsResult;

    public hr.eazework.com.model.GetEmpLeaveRequestsResult getGetEmpLeaveRequestsResult() {
        return GetEmpLeaveRequestsResult;
    }

    public void setGetEmpLeaveRequestsResult(hr.eazework.com.model.GetEmpLeaveRequestsResult getEmpLeaveRequestsResult) {
        GetEmpLeaveRequestsResult = getEmpLeaveRequestsResult;
    }

    static public LeaveSummaryResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LeaveSummaryResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
