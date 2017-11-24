package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 14-09-2017.
 */

public class HeadsItemModel implements Serializable {
    private String HeadDesc;
    private String HeadID;
    private int PolicyID;

    public String getHeadDesc() {
        return HeadDesc;
    }

    public void setHeadDesc(String headDesc) {
        HeadDesc = headDesc;
    }

    public String getHeadID() {
        return HeadID;
    }

    public void setHeadID(String headID) {
        HeadID = headID;
    }

    public int getPolicyID() {
        return PolicyID;
    }

    public void setPolicyID(int policyID) {
        PolicyID = policyID;
    }

    static public HeadsItemModel create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, HeadsItemModel.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
