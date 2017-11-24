package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class AdvanceSummaryRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private int reqID;
    private int advanceID;

    public int getAdvanceID() {
        return advanceID;
    }

    public void setAdvanceID(int advanceID) {
        this.advanceID = advanceID;
    }

    public int getReqID() {
        return reqID;
    }

    public void setReqID(int reqID) {
        this.reqID = reqID;
    }

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    static public AdvanceSummaryRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceSummaryRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
