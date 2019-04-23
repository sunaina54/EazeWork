package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 08-02-2018.
 */

public class AttendanceRejectRequestModel implements Serializable {
    private AttendanceRejectItem request;
    private AdvanceLoginDataRequestModel loginData;

    public AttendanceRejectItem getRequest() {
        return request;
    }

    public void setRequest(AttendanceRejectItem request) {
        this.request = request;
    }

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    static public AttendanceRejectRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AttendanceRejectRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
