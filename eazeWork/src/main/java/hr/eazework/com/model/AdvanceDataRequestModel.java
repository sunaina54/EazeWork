package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 01-09-2017.
 */

public class AdvanceDataRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private AdvanceItemModel advance;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public AdvanceItemModel getAdvance() {
        return advance;
    }

    public void setAdvance(AdvanceItemModel advance) {
        this.advance = advance;
    }

    static public AdvanceDataRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceDataRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
