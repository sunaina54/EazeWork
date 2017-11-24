package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 01-09-2017.
 */

public class AdvanceDataResponseModel implements Serializable {
    private SaveAdvanceResultResponseModel SaveAdvanceResult;

    public SaveAdvanceResultResponseModel getSaveAdvanceResult() {
        return SaveAdvanceResult;
    }

    public void setSaveAdvanceResult(SaveAdvanceResultResponseModel saveAdvanceResult) {
        SaveAdvanceResult = saveAdvanceResult;
    }
    static public AdvanceDataResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceDataResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
