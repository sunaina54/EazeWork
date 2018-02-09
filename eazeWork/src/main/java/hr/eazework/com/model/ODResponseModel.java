package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class ODResponseModel implements Serializable {
    private SaveODReqResultModel SaveODReqResult;
    private SaveODReqResultModel ApproveODRequestResult;

    public SaveODReqResultModel getApproveODRequestResult() {
        return ApproveODRequestResult;
    }

    public void setApproveODRequestResult(SaveODReqResultModel approveODRequestResult) {
        ApproveODRequestResult = approveODRequestResult;
    }

    public SaveODReqResultModel getSaveODReqResult() {
        return SaveODReqResult;
    }

    public void setSaveODReqResult(SaveODReqResultModel saveODReqResult) {
        SaveODReqResult = saveODReqResult;
    }
    static public ODResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ODResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
