package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 15-09-2017.
 */

public class VisibilityExpenseItem implements Serializable{
    private int ForEmpID;
    private String ReqID;
    private String Description;
    private String CurrencyCode;

    public int getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(int forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getReqID() {
        return ReqID;
    }

    public void setReqID(String reqID) {
        ReqID = reqID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }
    static public VisibilityExpenseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, VisibilityExpenseItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
