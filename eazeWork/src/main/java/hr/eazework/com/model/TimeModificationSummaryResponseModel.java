package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 07-02-2018.
 */

public class TimeModificationSummaryResponseModel implements Serializable {
    private GetAttendanceReqDetailResultModel GetAttendanceReqDetailResult;

    public GetAttendanceReqDetailResultModel getGetAttendanceReqDetailResult() {
        return GetAttendanceReqDetailResult;
    }

    public void setGetAttendanceReqDetailResult(GetAttendanceReqDetailResultModel getAttendanceReqDetailResult) {
        GetAttendanceReqDetailResult = getAttendanceReqDetailResult;
    }

    static public TimeModificationSummaryResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TimeModificationSummaryResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
