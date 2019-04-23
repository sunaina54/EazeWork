package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 01-02-2018.
 */

public class LeaveDetailResponseModel implements Serializable {
    private GetLeaveRequestDetailsResultModel GetLeaveRequestDetailsResult;

    public GetLeaveRequestDetailsResultModel getGetLeaveRequestDetailsResult() {
        return GetLeaveRequestDetailsResult;
    }

    public void setGetLeaveRequestDetailsResult(GetLeaveRequestDetailsResultModel getLeaveRequestDetailsResult) {
        GetLeaveRequestDetailsResult = getLeaveRequestDetailsResult;
    }


    static public LeaveDetailResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LeaveDetailResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
