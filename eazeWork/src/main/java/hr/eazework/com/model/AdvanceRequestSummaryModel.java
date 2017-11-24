package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

import hr.eazework.parsers.SecurityMaster;

/**
 * Created by Dell3 on 29-08-2017.
 */

public class AdvanceRequestSummaryModel implements Serializable{
    private AdvanceRequestSummaryResultModel GetAdvanceRequestSummaryResult;

    public AdvanceRequestSummaryResultModel getGetAdvanceRequestSummaryResult() {
        return GetAdvanceRequestSummaryResult;
    }

    public void setGetAdvanceRequestSummaryResult(AdvanceRequestSummaryResultModel getAdvanceRequestSummaryResult) {
        GetAdvanceRequestSummaryResult = getAdvanceRequestSummaryResult;
    }
    static public AdvanceRequestSummaryModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceRequestSummaryModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
