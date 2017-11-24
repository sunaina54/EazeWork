package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 28-09-2017.
 */

public class ApprovalRoleResponseModel implements Serializable {
    private GetAdvanceApprovalRoleResult GetAdvanceApprovalRoleResult;

    public GetAdvanceApprovalRoleResult getGetAdvanceApprovalRoleResult() {
        return GetAdvanceApprovalRoleResult;
    }

    public void setGetAdvanceApprovalRoleResult(GetAdvanceApprovalRoleResult getAdvanceApprovalRoleResult) {
        GetAdvanceApprovalRoleResult = getAdvanceApprovalRoleResult;
    }
    static public ApprovalRoleResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ApprovalRoleResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
