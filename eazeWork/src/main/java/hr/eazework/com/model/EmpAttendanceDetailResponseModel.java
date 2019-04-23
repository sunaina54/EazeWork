package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 23-02-2018.
 */

public class EmpAttendanceDetailResponseModel implements Serializable
{
   private GetEmpAttendanceDetailResult GetEmpAttendanceDetailResult;

    public hr.eazework.com.model.GetEmpAttendanceDetailResult getGetEmpAttendanceDetailResult() {
        return GetEmpAttendanceDetailResult;
    }

    public void setGetEmpAttendanceDetailResult(hr.eazework.com.model.GetEmpAttendanceDetailResult getEmpAttendanceDetailResult) {
        GetEmpAttendanceDetailResult = getEmpAttendanceDetailResult;
    }

    static public EmpAttendanceDetailResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, EmpAttendanceDetailResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
