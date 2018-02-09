package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 17-01-2018.
 */

public class ODSummaryResponse implements Serializable {
    private GetODRequestDetailResult GetODRequestDetailResult;

    public hr.eazework.com.model.GetODRequestDetailResult getGetODRequestDetailResult() {
        return GetODRequestDetailResult;
    }

    public void setGetODRequestDetailResult(hr.eazework.com.model.GetODRequestDetailResult getODRequestDetailResult) {
        GetODRequestDetailResult = getODRequestDetailResult;
    }

    static public ODSummaryResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ODSummaryResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
