package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by PSQ on 11/12/2017.
 */

public class EmployResponse implements Serializable {

    private GetLeaveEmpListResult GetLeaveEmpListResult;
    private GetWFHEmpListResult GetWFHEmpListResult;
    private GetWFHEmpListResult GetODEmpListResult;
    private GetWFHEmpListResult GetTourEmpListResult;

    public hr.eazework.com.model.GetWFHEmpListResult getGetODEmpListResult() {
        return GetODEmpListResult;
    }

    public void setGetODEmpListResult(hr.eazework.com.model.GetWFHEmpListResult getODEmpListResult) {
        GetODEmpListResult = getODEmpListResult;
    }

    public hr.eazework.com.model.GetWFHEmpListResult getGetTourEmpListResult() {
        return GetTourEmpListResult;
    }

    public void setGetTourEmpListResult(hr.eazework.com.model.GetWFHEmpListResult getTourEmpListResult) {
        GetTourEmpListResult = getTourEmpListResult;
    }

    public hr.eazework.com.model.GetWFHEmpListResult getGetWFHEmpListResult() {
        return GetWFHEmpListResult;
    }

    public void setGetWFHEmpListResult(hr.eazework.com.model.GetWFHEmpListResult getWFHEmpListResult) {
        GetWFHEmpListResult = getWFHEmpListResult;
    }

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
