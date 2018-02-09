package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 18-01-2018.
 */

public class WithdrawWFHResponse implements Serializable {
    private WithdrawWFHRequestResult WithdrawWFHRequestResult;
    private WithdrawWFHRequestResult WithdrawODRequestResult;
    private WithdrawWFHRequestResult WithdrawTourRequestResult;
    private WithdrawWFHRequestResult WithdrawLeaveRequestResult;

    public hr.eazework.com.model.WithdrawWFHRequestResult getWithdrawLeaveRequestResult() {
        return WithdrawLeaveRequestResult;
    }

    public void setWithdrawLeaveRequestResult(hr.eazework.com.model.WithdrawWFHRequestResult withdrawLeaveRequestResult) {
        WithdrawLeaveRequestResult = withdrawLeaveRequestResult;
    }

    public hr.eazework.com.model.WithdrawWFHRequestResult getWithdrawTourRequestResult() {
        return WithdrawTourRequestResult;
    }

    public void setWithdrawTourRequestResult(hr.eazework.com.model.WithdrawWFHRequestResult withdrawTourRequestResult) {
        WithdrawTourRequestResult = withdrawTourRequestResult;
    }

    public hr.eazework.com.model.WithdrawWFHRequestResult getWithdrawODRequestResult() {
        return WithdrawODRequestResult;
    }

    public void setWithdrawODRequestResult(hr.eazework.com.model.WithdrawWFHRequestResult withdrawODRequestResult) {
        WithdrawODRequestResult = withdrawODRequestResult;
    }

    public hr.eazework.com.model.WithdrawWFHRequestResult getWithdrawWFHRequestResult() {
        return WithdrawWFHRequestResult;
    }

    public void setWithdrawWFHRequestResult(hr.eazework.com.model.WithdrawWFHRequestResult withdrawWFHRequestResult) {
        WithdrawWFHRequestResult = withdrawWFHRequestResult;
    }

    static public WithdrawWFHResponse create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, WithdrawWFHResponse.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
