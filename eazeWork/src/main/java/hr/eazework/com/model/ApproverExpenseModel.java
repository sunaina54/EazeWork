package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-09-2017.
 */

public class ApproverExpenseModel implements Serializable {
    private int ForEmpID;
    private int ReqID;
    private String ProjectID;
    private String ClaimTypeID;

    public int getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public int getReqID() {
        return ReqID;
    }

    public void setReqID(int reqID) {
        ReqID = reqID;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getClaimTypeID() {
        return ClaimTypeID;
    }

    public void setClaimTypeID(String claimTypeID) {
        ClaimTypeID = claimTypeID;
    }

    static public ApproverExpenseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ApproverExpenseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
