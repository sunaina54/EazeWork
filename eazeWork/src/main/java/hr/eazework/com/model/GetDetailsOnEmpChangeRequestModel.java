package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-01-2018.
 */

public class GetDetailsOnEmpChangeRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private String forEmpID;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public String getForEmpID() {
        return forEmpID;
    }

    public void setForEmpID(String forEmpID) {
        this.forEmpID = forEmpID;
    }


    static public GetDetailsOnEmpChangeRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetDetailsOnEmpChangeRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
