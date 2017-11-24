package hr.eazework.com.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Dell3 on 26-09-2017.
 */

public class AdjustmentExpenseItem implements Serializable {
    private String ForEmpID;
    private String CurrencyCode;

    public String getForEmpID() {
        return ForEmpID;
    }

    public void setForEmpID(String forEmpID) {
        ForEmpID = forEmpID;
    }

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }
    static public AdjustmentExpenseItem create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation of the state
        Gson gson = new Gson();
        return gson.fromJson(serializedData, AdjustmentExpenseItem.class);
    }
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
