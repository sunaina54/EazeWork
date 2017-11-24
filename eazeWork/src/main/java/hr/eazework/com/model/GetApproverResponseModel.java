package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 12-09-2017.
 */

public class GetApproverResponseModel implements Serializable {
    private ApproverItemModel GetApproverDetailsResult;

    public ApproverItemModel getGetApproverDetailsResult() {
        return GetApproverDetailsResult;
    }

    public void setGetApproverDetailsResult(ApproverItemModel getApproverDetailsResult) {
        GetApproverDetailsResult = getApproverDetailsResult;
    }

    static public GetApproverResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetApproverResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
