package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-01-2018.
 */

public class WFHSummaryResponse implements Serializable {
    private GetWFHRequestDetailResult GetWFHRequestDetailResult;

    public hr.eazework.com.model.GetWFHRequestDetailResult getGetWFHRequestDetailResult() {
        return GetWFHRequestDetailResult;
    }

    public void setGetWFHRequestDetailResult(hr.eazework.com.model.GetWFHRequestDetailResult getWFHRequestDetailResult) {
        GetWFHRequestDetailResult = getWFHRequestDetailResult;
    }


    static public WFHSummaryResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, WFHSummaryResponse.class);
    }

    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
