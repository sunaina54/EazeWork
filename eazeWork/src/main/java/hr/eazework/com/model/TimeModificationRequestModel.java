package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 31-01-2018.
 */

public class TimeModificationRequestModel implements Serializable {

    private AdvanceLoginDataRequestModel loginData;
    private TimeModificationItem request;

    public TimeModificationItem getRequest() {
        return request;
    }

    public void setRequest(TimeModificationItem request) {
        this.request = request;
    }

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }


    static public TimeModificationRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TimeModificationRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
