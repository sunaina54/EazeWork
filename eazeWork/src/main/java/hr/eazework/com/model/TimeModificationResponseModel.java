package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 31-01-2018.
 */

public class TimeModificationResponseModel implements Serializable {
    private TimeModificationResultModel TimeModificationResult;
    private TimeModificationResultModel BackDateAttendanceResult;

    public TimeModificationResultModel getBackDateAttendanceResult() {
        return BackDateAttendanceResult;
    }

    public void setBackDateAttendanceResult(TimeModificationResultModel backDateAttendanceResult) {
        BackDateAttendanceResult = backDateAttendanceResult;
    }

    public TimeModificationResultModel getTimeModificationResult() {
        return TimeModificationResult;
    }

    public void setTimeModificationResult(TimeModificationResultModel timeModificationResult) {
        TimeModificationResult = timeModificationResult;
    }


    static public TimeModificationResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TimeModificationResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
