package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 17-11-2017.
 */

public class GetCorpEmpParamResultResponse implements Serializable {

    private GetCorpEmpParamResult GetCorpEmpParamResult;

    public hr.eazework.com.model.GetCorpEmpParamResult getGetCorpEmpParamResult() {
        return GetCorpEmpParamResult;
    }

    public void setGetCorpEmpParamResult(hr.eazework.com.model.GetCorpEmpParamResult getCorpEmpParamResult) {
        GetCorpEmpParamResult = getCorpEmpParamResult;
    }

    static public GetCorpEmpParamResultResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetCorpEmpParamResultResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
