package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 25-01-2018.
 */

public class WFHRejectRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private String reqID;
    private String comments;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    static public WFHRejectRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, WFHRejectRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
