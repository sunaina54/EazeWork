package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-09-2017.
 */

public class ProjectExpenseModel implements Serializable {
    private int ForEmpID;
    private String ClaimTypeID;

    public int getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getClaimTypeID() {
        return ClaimTypeID;
    }

    public void setClaimTypeID(String claimTypeID) {
        ClaimTypeID = claimTypeID;
    }

    static public ProjectExpenseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ProjectExpenseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
