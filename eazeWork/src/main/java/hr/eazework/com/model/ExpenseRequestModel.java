package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 05-09-2017.
 */

public class ExpenseRequestModel implements Serializable {
    private int ReqID;

    public int getReqID() {
        return ReqID;
    }

    public void setReqID(int reqID) {
        ReqID = reqID;
    }

    static public ExpenseRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ExpenseRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
