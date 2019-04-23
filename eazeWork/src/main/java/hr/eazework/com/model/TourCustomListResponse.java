package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 16-01-2018.
 */

public class TourCustomListResponse implements Serializable {
   private GetTourRequestCustomFieldListResult GetTourRequestCustomFieldListResult;

    public hr.eazework.com.model.GetTourRequestCustomFieldListResult getGetTourRequestCustomFieldListResult() {
        return GetTourRequestCustomFieldListResult;
    }

    public void setGetTourRequestCustomFieldListResult(hr.eazework.com.model.GetTourRequestCustomFieldListResult getTourRequestCustomFieldListResult) {
        GetTourRequestCustomFieldListResult = getTourRequestCustomFieldListResult;
    }


    static public TourCustomListResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TourCustomListResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
