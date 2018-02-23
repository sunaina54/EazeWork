package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 20-02-2018.
 */

public class UploadProfilePicModel implements Serializable {
    private FileInfo FileInfo;
    private AdvanceLoginDataRequestModel loginData;

    public hr.eazework.com.model.FileInfo getFileInfo() {
        return FileInfo;
    }

    public void setFileInfo(hr.eazework.com.model.FileInfo fileInfo) {
        FileInfo = fileInfo;
    }

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }
    static public UploadProfilePicModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, UploadProfilePicModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
