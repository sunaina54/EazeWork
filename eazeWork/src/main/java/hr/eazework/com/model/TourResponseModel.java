package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 22-01-2018.
 */

public class TourResponseModel implements Serializable {
    private SaveTourReqResultModel SaveTourReqResult;
    private SaveTourReqResultModel ApproveTourRequestResult;

    public SaveTourReqResultModel getApproveTourRequestResult() {
        return ApproveTourRequestResult;
    }

    public void setApproveTourRequestResult(SaveTourReqResultModel approveTourRequestResult) {
        ApproveTourRequestResult = approveTourRequestResult;
    }

    public SaveTourReqResultModel getSaveTourReqResult() {
        return SaveTourReqResult;
    }

    public void setSaveTourReqResult(SaveTourReqResultModel saveTourReqResult) {
        SaveTourReqResult = saveTourReqResult;
    }

    static public TourResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, TourResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
