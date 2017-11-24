package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 11/12/2017.
 */

public class EmployResponse implements Serializable {

    private GetLeaveEmpListResult GetLeaveEmpListResult;

    public hr.eazework.com.model.GetLeaveEmpListResult getGetLeaveEmpListResult() {
        return GetLeaveEmpListResult;
    }

    public void setGetLeaveEmpListResult(hr.eazework.com.model.GetLeaveEmpListResult getLeaveEmpListResult) {
        GetLeaveEmpListResult = getLeaveEmpListResult;
    }

    static public EmployResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, EmployResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
