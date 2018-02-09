package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 22-01-2018.
 */

public class TourSummaryResponse implements Serializable {

    private GetTourRequestDetailResult GetTourRequestDetailResult;

    public hr.eazework.com.model.GetTourRequestDetailResult getGetTourRequestDetailResult() {
        return GetTourRequestDetailResult;
    }

    public void setGetTourRequestDetailResult(hr.eazework.com.model.GetTourRequestDetailResult getTourRequestDetailResult) {
        GetTourRequestDetailResult = getTourRequestDetailResult;
    }

    static public TourSummaryResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TourSummaryResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
