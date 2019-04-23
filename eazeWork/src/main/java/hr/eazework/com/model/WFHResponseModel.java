package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 11-01-2018.
 */

public class WFHResponseModel implements Serializable {
    private SaveWFHReqResultModel SaveWFHReqResult;
    private SaveWFHReqResultModel ApproveWFHRequestResult;

    public SaveWFHReqResultModel getApproveWFHRequestResult() {
        return ApproveWFHRequestResult;
    }

    public void setApproveWFHRequestResult(SaveWFHReqResultModel approveWFHRequestResult) {
        ApproveWFHRequestResult = approveWFHRequestResult;
    }

    public SaveWFHReqResultModel getSaveWFHReqResult() {
        return SaveWFHReqResult;
    }

    public void setSaveWFHReqResult(SaveWFHReqResultModel saveWFHReqResult) {
        SaveWFHReqResult = saveWFHReqResult;
    }

    static public WFHResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, WFHResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
