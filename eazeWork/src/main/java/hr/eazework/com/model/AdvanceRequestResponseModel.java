package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell3 on 01-09-2017.
 */

public class AdvanceRequestResponseModel implements Serializable {
   private GetAdvancePageInitResultResponseModel GetAdvancePageInitResult;

    public GetAdvancePageInitResultResponseModel getGetAdvancePageInitResult() {
        return GetAdvancePageInitResult;
    }

    public void setGetAdvancePageInitResult(GetAdvancePageInitResultResponseModel getAdvancePageInitResult) {
        GetAdvancePageInitResult = getAdvancePageInitResult;
    }

    static public AdvanceRequestResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceRequestResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
