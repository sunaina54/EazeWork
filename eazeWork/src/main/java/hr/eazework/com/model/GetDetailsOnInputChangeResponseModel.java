package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class GetDetailsOnInputChangeResponseModel implements Serializable {
   private GetDetailsOnInputChangeResultModel GetDetailsOnInputChangeResult;

    public GetDetailsOnInputChangeResultModel getGetDetailsOnInputChangeResult() {
        return GetDetailsOnInputChangeResult;
    }

    public void setGetDetailsOnInputChangeResult(GetDetailsOnInputChangeResultModel getDetailsOnInputChangeResult) {
        GetDetailsOnInputChangeResult = getDetailsOnInputChangeResult;
    }

    static public GetDetailsOnInputChangeResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, GetDetailsOnInputChangeResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
