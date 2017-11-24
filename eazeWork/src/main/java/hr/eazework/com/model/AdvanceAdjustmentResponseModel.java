package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 26-09-2017.
 */

public class AdvanceAdjustmentResponseModel implements Serializable {
    private GetAdvanceListForExpenseResult GetAdvanceListForExpenseResult;

    public hr.eazework.com.model.GetAdvanceListForExpenseResult getGetAdvanceListForExpenseResult() {
        return GetAdvanceListForExpenseResult;
    }

    public void setGetAdvanceListForExpenseResult(hr.eazework.com.model.GetAdvanceListForExpenseResult getAdvanceListForExpenseResult) {
        GetAdvanceListForExpenseResult = getAdvanceListForExpenseResult;
    }
    static public AdvanceAdjustmentResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdvanceAdjustmentResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
