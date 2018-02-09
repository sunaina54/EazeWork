package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-01-2018.
 */

public class GetDetailsOnEmpChangeResponseModel implements Serializable {
    private GetDetailsOnEmpChangeResultModel GetDetailsOnEmpChangeResult;

    public GetDetailsOnEmpChangeResultModel getGetDetailsOnEmpChangeResult() {
        return GetDetailsOnEmpChangeResult;
    }

    public void setGetDetailsOnEmpChangeResult(GetDetailsOnEmpChangeResultModel getDetailsOnEmpChangeResult) {
        GetDetailsOnEmpChangeResult = getDetailsOnEmpChangeResult;
    }

    static public GetDetailsOnEmpChangeResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetDetailsOnEmpChangeResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
