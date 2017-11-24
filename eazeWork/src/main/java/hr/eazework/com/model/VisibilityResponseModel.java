package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-09-2017.
 */

public class VisibilityResponseModel implements Serializable {
    private GetHeadDetailsWithPolicyResultModel GetHeadDetailsWithPolicyResult;

    public GetHeadDetailsWithPolicyResultModel getGetHeadDetailsWithPolicyResult() {
        return GetHeadDetailsWithPolicyResult;
    }

    public void setGetHeadDetailsWithPolicyResult(GetHeadDetailsWithPolicyResultModel getHeadDetailsWithPolicyResult) {
        GetHeadDetailsWithPolicyResult = getHeadDetailsWithPolicyResult;
    }
    static public VisibilityResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VisibilityResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
