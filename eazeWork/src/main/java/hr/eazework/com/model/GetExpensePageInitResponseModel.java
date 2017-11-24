package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 06-09-2017.
 */

public class GetExpensePageInitResponseModel implements Serializable {
    private GetExpensePageInitResultModel GetExpensePageInitResult;

    public GetExpensePageInitResultModel getGetExpensePageInitResult() {
        return GetExpensePageInitResult;
    }

    public void setGetExpensePageInitResult(GetExpensePageInitResultModel getExpensePageInitResult) {
        GetExpensePageInitResult = getExpensePageInitResult;
    }
    static public GetExpensePageInitResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetExpensePageInitResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
