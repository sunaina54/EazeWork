package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 26-09-2017.
 */

public class SearchOnBehalfItem implements Serializable {
    private String fromCount;
    private String toCount;
    private String matchStr;
    private AdvanceLoginDataRequestModel loginData;

    public AdvanceLoginDataRequestModel getLoginData() {
        return loginData;
    }

    public void setLoginData(AdvanceLoginDataRequestModel loginData) {
        this.loginData = loginData;
    }

    public String getFromCount() {
        return fromCount;
    }

    public void setFromCount(String fromCount) {
        this.fromCount = fromCount;
    }

    public String getToCount() {
        return toCount;
    }

    public void setToCount(String toCount) {
        this.toCount = toCount;
    }

    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }

    static public SearchOnBehalfItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, SearchOnBehalfItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
