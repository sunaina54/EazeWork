package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 20-02-2018.
 */

public class UploadProfilePicResponseModel implements Serializable {
    private UploadProfilePicResult UploadProfilePicResult;

    public hr.eazework.com.model.UploadProfilePicResult getUploadProfilePicResult() {
        return UploadProfilePicResult;
    }

    public void setUploadProfilePicResult(hr.eazework.com.model.UploadProfilePicResult uploadProfilePicResult) {
        UploadProfilePicResult = uploadProfilePicResult;
    }
    static public UploadProfilePicResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, UploadProfilePicResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
