package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class AdvanceApprovalResponseModel implements Serializable {
    private GetEmpAdvanceApprovalResult GetEmpAdvanceApprovalResult;

    public GetEmpAdvanceApprovalResult getGetEmpAdvanceApprovalResult() {
        return GetEmpAdvanceApprovalResult;
    }

    public void setGetEmpAdvanceApprovalResult(GetEmpAdvanceApprovalResult getEmpAdvanceApprovalResult) {
        GetEmpAdvanceApprovalResult = getEmpAdvanceApprovalResult;
    }
    static public AdvanceApprovalResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceApprovalResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
