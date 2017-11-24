package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 13-09-2017.
 */

public class ProjectListResponseModel implements Serializable {
    private EvtClaimTypeChangeResult EvtClaimTypeChangeResult;

    public EvtClaimTypeChangeResult getEvtClaimTypeChangeResult() {
        return EvtClaimTypeChangeResult;
    }

    public void setEvtClaimTypeChangeResult(hr.eazework.com.model.EvtClaimTypeChangeResult evtClaimTypeChangeResult) {
        EvtClaimTypeChangeResult = evtClaimTypeChangeResult;
    }
    static public ProjectListResponseModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, ProjectListResponseModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
