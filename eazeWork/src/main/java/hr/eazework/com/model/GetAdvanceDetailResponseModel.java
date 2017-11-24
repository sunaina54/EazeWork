package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 30-08-2017.
 */

public class GetAdvanceDetailResponseModel implements Serializable {
    private GetAdvanceDetailResultModel GetAdvanceDetailResult;

    public GetAdvanceDetailResultModel getGetAdvanceDetailResult() {
        return GetAdvanceDetailResult;
    }

    public void setGetAdvanceDetailResult(GetAdvanceDetailResultModel getAdvanceDetailResult) {
        GetAdvanceDetailResult = getAdvanceDetailResult;
    }

    static public GetAdvanceDetailResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetAdvanceDetailResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
