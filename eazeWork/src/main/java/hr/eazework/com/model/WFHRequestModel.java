package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 10-01-2018.
 */

public class WFHRequestModel implements Serializable {
    private AdvanceLoginDataRequestModel loginData;
    private WFHRequestDetailModel wfhRequestDetail;
    private WFHRequestDetailModel wfhReqDetail;

    public WFHRequestDetailModel getWfhReqDetail() {
        return wfhReqDetail;
    }

    public void setWfhReqDetail(WFHRequestDetailModel wfhReqDetail) {
        this.wfhReqDetail = wfhReqDetail;
    }

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public WFHRequestDetailModel getWfhRequestDetail() {
        return wfhRequestDetail;
    }

    public void setWfhRequestDetail(WFHRequestDetailModel wfhRequestDetail) {
        this.wfhRequestDetail = wfhRequestDetail;
    }

    static public WFHRequestModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, WFHRequestModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
