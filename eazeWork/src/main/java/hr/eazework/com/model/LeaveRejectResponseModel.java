package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 25-01-2018.
 */

public class LeaveRejectResponseModel implements Serializable {
    private RejectWFHRequestResult RejectWFHRequestResult;
    private RejectWFHRequestResult RejectODRequestResult;
    private RejectWFHRequestResult RejectTourRequestResult;
    private RejectWFHRequestResult RejectLeaveRequestResult;

    public hr.eazework.com.model.RejectWFHRequestResult getRejectLeaveRequestResult() {
        return RejectLeaveRequestResult;
    }

    public void setRejectLeaveRequestResult(hr.eazework.com.model.RejectWFHRequestResult rejectLeaveRequestResult) {
        RejectLeaveRequestResult = rejectLeaveRequestResult;
    }

    public hr.eazework.com.model.RejectWFHRequestResult getRejectWFHRequestResult() {
        return RejectWFHRequestResult;
    }

    public void setRejectWFHRequestResult(hr.eazework.com.model.RejectWFHRequestResult rejectWFHRequestResult) {
        RejectWFHRequestResult = rejectWFHRequestResult;
    }

    public hr.eazework.com.model.RejectWFHRequestResult getRejectODRequestResult() {
        return RejectODRequestResult;
    }

    public void setRejectODRequestResult(hr.eazework.com.model.RejectWFHRequestResult rejectODRequestResult) {
        RejectODRequestResult = rejectODRequestResult;
    }

    public hr.eazework.com.model.RejectWFHRequestResult getRejectTourRequestResult() {
        return RejectTourRequestResult;
    }

    public void setRejectTourRequestResult(hr.eazework.com.model.RejectWFHRequestResult rejectTourRequestResult) {
        RejectTourRequestResult = rejectTourRequestResult;
    }

    static public LeaveRejectResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, LeaveRejectResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
